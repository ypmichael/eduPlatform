package com.third.IntelPlat.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.third.IntelPlat.entity.TestObjectEntity;

public class ExcelUtil {

	private static ExcelUtil excelUtil = new ExcelUtil();

	private ExcelUtil() {
	};

	public static ExcelUtil getInstance() {
		return excelUtil;
	}

	@SuppressWarnings("rawtypes")
	public static List<Object> readExcelToObjectByPath(InputStream in,Class clz,int readLine,int tailLine){
		Workbook workbook=null;
		try {
			 workbook=WorkbookFactory.create(in);
			return  handlerExcelToObject(workbook, clz, readLine,tailLine);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * desc：该方法用于处理excel、读取excel中的数据并将转换成clz对应的类
	 * @param workbook
	 * @param clz
	 * @param readLine 从第几行开始读取
	 * @param tailLine 尾部有几行不需要读取
	 */
	
	@SuppressWarnings("rawtypes")
	private static List<Object> handlerExcelToObject(Workbook workbook,Class clz,int readLine,int tailLine){
		List<Object> objects= new ArrayList<Object>();
		
		try {
			Sheet sheet=workbook.getSheetAt(0);
			Row row=sheet.getRow(readLine);
			Map<Integer,String> maps=getExcelHeaderMap(row, clz);
			/***
			 * i = readLine+1说明、第一行读取的时标题行、因此读取数据应从标题的下一行开始 readLine+1
			 * sheet.getLastRowNum()-tailLine 说明：总共需要读取的行数-不需要读取的行数=需要读取的行数
			 */
			for (int i = readLine+1; i <=sheet.getLastRowNum()-tailLine; i++) {
				//实例化对应的对象
				Object obj=	clz.newInstance();
				//得到当前行
				Row currRow=sheet.getRow(i);
				//保存行号
				BeanUtils.copyProperty(obj, "rowIndex", i);
				//循环当前行所有列的数据
				for (Cell cell : currRow) {
					//当前列的坐标
				   int currColIndex=cell.getColumnIndex();
				   //得到当前列对应的方法名称
				   String methodName= maps.get(currColIndex);
				   
				   methodName=methodName.substring(3);
				   methodName=methodName.substring(0,1).toLowerCase()+methodName.substring(1);
				   
				   BeanUtils.copyProperty(obj, methodName, getStringCellValue(cell));
				}
				//将添加到集合中去
				objects.add(obj);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}  catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return objects;
	}
	
	
	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private static String getStringCellValue(Cell cell) {
		String strCell = "";
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				//strCell = DateUtilsl.dateToString(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()),"yyyy-MM-dd HH:mm:ss");
			
			} else {
	        	cell.setCellType(Cell.CELL_TYPE_STRING);
	        	strCell = cell.getStringCellValue();
	        }
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}
	
	/**
	 * 获取单元格数据内容为日期类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getDateCellValue(Cell cell) {
		String result = "";
		try {
			int cellType = cell.getCellType();
			if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
				Date date = cell.getDateCellValue();
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
			} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
				String date = getStringCellValue(cell);
				result = date.replaceAll("[年月]", "-").replace("日", "").trim();
			} else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
				result = "";
			}
		} catch (Exception e) {
			System.out.println("日期格式不正确!");
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * 
	 * @param titleRow 标题行
	 * @param clz 
	 * @return Map<Integer, String> 对应列所对应的方法
	 */
	@SuppressWarnings("rawtypes")
	private static Map<Integer, String> getExcelHeaderMap(Row titleRow,Class clz){
		//首先获取类上上面所注解的标题
		List<ExcelHeader> headers=getExcelHeader(clz);
		//用于存储某一列所对应的方法的值
		Map<Integer, String> maps= new HashMap<Integer, String>();
		//根据header列表找到对应的数据列表
		for (ExcelHeader excelHeader : headers) {
			for (Cell cell : titleRow) {
				if(cell.getStringCellValue().trim().equals(excelHeader.getTilte())){
				   //找到当前列所对应的值将存储起来,而这里我们是得到get方法，需要获取set方法，为对象设置
					maps.put(cell.getColumnIndex(), excelHeader.getMethodName().replace("get", "set"));
					break;
				}
			}
		}
		return maps;
	}
	
	
	/**不通过excel模版的方式,直接将数据对象导入到Excel中
	 * @param template :excel的模板
	 * @param outPath:文件输出路径 输出到那里
	 * @param objs:数据列表、用于要输出的数据对象
	 * @param constantMap:用于输出一些特殊的一些常量 如#tiltle=标题
	 * @param clz:导入那个,其通过反射机制实现
	 * @param isXssF:true=2007excel,false=2003excel
	 * */
	@SuppressWarnings("rawtypes")
	public static void exportObjToExcel(String outPath,List objs,Class clz,boolean isXssF){
		
		FileOutputStream fos=null;
		try {
			 fos= new FileOutputStream(new File(outPath));
			Workbook workbook=HanderExcel(objs, clz, isXssF);
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * @param HanderExcel该方法不通过加载模版的方式,而是直接将对象数据数据写入到excel
	 * @param objs 要写入的对象集合
	 * @param clz 写入的对象
	 * @param isXssF判定是否是2003excel还是2007excel
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * 
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Workbook HanderExcel(List objs,Class clz,boolean isXssF) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Workbook workbook=null;
		if(isXssF){
			//创建2007excel后缀是.xlsx
			workbook= new XSSFWorkbook();
		}else{
			//创建2003excel后缀是.xls
			workbook=new HSSFWorkbook();
		}
		
		//创建sheet
		Sheet sheet=workbook.createSheet();
		//得到第一行,在excel中第一行第一列的坐标是(0,0)
		Row row= sheet.createRow(0);
		//获取传入数据的标题,其就是annotation
		List<ExcelHeader> excelHeaders=getExcelHeader(clz);
		//对头部数据进行排序
		Collections.sort(excelHeaders);
		//将表头插入到第一行
		for (int i=0;i<excelHeaders.size();i++) {
		    Cell cell=row.createCell(i);
		    cell.setCellValue(excelHeaders.get(i).getTilte());
		}
		 //数据不为空的时候执行
		if(null!=objs && objs.size()>0){
			//插入对象数据
			for (int i = 0; i < objs.size(); i++) {
				Object obj=objs.get(i);
				//新建一行
				Row dataRow=sheet.createRow(i+1);
				for (int j = 0; j < excelHeaders.size(); j++) {
					ExcelHeader e=excelHeaders.get(j);
					//得到对应的get方法
					Method method=clz.getMethod(e.getMethodName());
					try {
						Object objectName=method.getReturnType().getName();
						String value=String.valueOf(method.invoke(obj));
						//将对应的值插入进去
						if(objectName.equals("java.sql.Timestamp")|| objectName.equals("java.sql.Date") || objectName.equals("java.util.Date"))
						{
							Cell cell=dataRow.createCell(j);
							if(value==null||value.equals("null")){
								cell.setCellValue("");
							}else{
								    Object dateValue=method.invoke(obj);
								    CellStyle cellStyle= workbook.createCellStyle();
								    DataFormat format=workbook.createDataFormat();
								    cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
								    if(objectName.equals("java.sql.Timestamp"))
								    	cell.setCellValue((java.sql.Timestamp)dateValue);
								    else if(objectName.equals("java.sql.Date"))
								    	cell.setCellValue((java.sql.Date)dateValue);
								    else if(objectName.equals("java.util.Date"))
								    	cell.setCellValue((java.util.Date)dateValue);
								    cell.setCellStyle(cellStyle);  
							}
						}
						else{
							dataRow.createCell(j).setCellValue(value==null||value.equals("null")?"":value);
						}
							
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} 
				
			}
		}
		
		
		return	workbook;
		
	}
	
	
	/**通过反射机制,得到在User类中的注解*/
	@SuppressWarnings("rawtypes")
	public static List<ExcelHeader> getExcelHeader(Class clz){
		List<ExcelHeader> headers= new ArrayList<ExcelHeader>();
		Method[] methods=clz.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			//获取当前方法
			Method method=methods[i];
			//获取方法名称、我们所有的注解都是注解在get方法的,因此只需要获取get有关的方法
			String methodName=method.getName();
			if(methodName.startsWith("get")){
				//获取到get方法中对应的注解
				ExcelResources er=method.getAnnotation(ExcelResources.class);
				//当一个类中存在有些字段的get未添加annotation 就不进行添加
				if(er!=null)
				  headers.add(new ExcelHeader(er.title(), er.order(), methodName));
			}
		}
		
		return headers;
	}
	
	public enum FileType {

		XLSX(".xlsx"), XLS(".xls");

	    private String value;

	    private FileType(String value) {

	        this.value = value;

	    }

	    public String getValue() {
            return value;
        }
	}
	
	
	
	
	public static void main(String[] args) {
		
		List<Object> list = null;
		try {
			list = ExcelUtil.readExcelToObjectByPath(new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\table-1.xlsx")), TestObjectEntity.class, 0, 0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
