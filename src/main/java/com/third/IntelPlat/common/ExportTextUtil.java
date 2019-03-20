package com.third.IntelPlat.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExportTextUtil 
{
    /**
     * 读取文本文件.
     *
     * @throws UnsupportedEncodingException
     *
     */
    public static String readTxtFile(File filenameShip) throws UnsupportedEncodingException {
        String readData = null;
        BufferedReader br = null;
        String readStr = "    ";
        try {
            br = new BufferedReader(new FileReader(filenameShip));
            try {
                while ((readData = br.readLine()) != null) {
//                    System.out.println("readData:" + readData);
                    readStr = readStr + readData + "\r\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return readStr;
    }
 
    /**
     * 导出
     * @param contentList数据，但第一个必须时表头
     * @param filenameShip 文件夹的位置
     * @throws IOException
     */
    public static void writeTxtFileFor(List<Map<String, String>> contentList,File filenameShip) throws IOException {
        // 先读取原有文件内容，然后进行写入操作
        String readStr = "    ";
        FileWriter writer = null;
            for (int i = 0; i < contentList.size(); i++) {
                HashMap<String, String> map=(HashMap<String, String>) contentList.get(i);
                String filein1 = "\r\n" ;
            for (int j = 0; j < map.size()-1; j++) {
                filein1+=map.get((1+j)+"")+ readStr;
            }
            filein1+=map.get((map.size())+"");
            
                filein1+="\r\n";
                try {
                    writer = new FileWriter(filenameShip, true);
                    writer.write(filein1);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                
            }
        readTxtFile(filenameShip);
    }
 
 
    /**
     * 将文件中指定内容的第一行替换为其它内容.
     *
     * @param oldStr
     *            查找内容
     * @param replaceStr
     *            替换内容
     */
    @SuppressWarnings("unused")
    public static void replaceTxtByStr(String oldStr, String replaceStr, String path) {
        String temp = "";
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();
            // 保存该行前面的内容
            for (int j = 1; (temp = br.readLine()) != null
                    && !temp.equals(oldStr); j++) {
                buf = buf.append(temp);
                buf = buf.append(System.getProperty("line.separator"));
            }
            // 将内容插入
            buf = buf.append(replaceStr);
            // 保存该行后面的内容
            while ((temp = br.readLine()) != null) {
                buf = buf.append(System.getProperty("line.separator"));
                buf = buf.append(temp);
            }
            br.close();
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    
    /**
     * 创建文件路径
     * @param destFileName  字符串格式的 文件路径
     * @return
     */
      public static boolean createFile(String destFileName) {  
            File file = new File(destFileName);  
            if(file.exists()) {  
                System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");  
                file.delete();
                return false;  
            }  
            if (destFileName.endsWith(File.separator)) {  
                System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  
                return false;  
            }  
            //判断目标文件所在的目录是否存在  
            if(!file.getParentFile().exists()) {  
                //如果目标文件所在的目录不存在，则创建父目录  
                System.out.println("目标文件所在目录不存在，准备创建它！");  
                if(!file.getParentFile().mkdirs()) {  
                    System.out.println("创建目标文件所在目录失败！");  
                    return false;  
                }  
            }  
            //创建目标文件  
            try {  
                if (file.createNewFile()) {  
                    System.out.println("创建单个文件" + destFileName + "成功！");  
                    return true;  
                } else {  
                    System.out.println("创建单个文件" + destFileName + "失败！");  
                    return false;  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
                System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());  
                return false;  
            }  
        }  

}
