package com.third.IntelPlat.common;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.third.IntelPlat.entity.TestLogEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFReport
{
  Document document = new Document();
  private static Font headfont;
  private static Font keyfont;
  private static Font textfont;
  
  static
  {
    try
    {
      BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
      headfont = new Font(bfChinese, 10.0F, 1);
      keyfont = new Font(bfChinese, 8.0F, 1);
      textfont = new Font(bfChinese, 8.0F, 0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public PDFReport(File file)
  {
    this.document.setPageSize(PageSize.A4);
    try
    {
      PdfWriter.getInstance(this.document, new FileOutputStream(file));
      this.document.open();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  int maxWidth = 520;
  
  public PdfPCell createCell(String value, Font font, int align)
  {
    PdfPCell cell = new PdfPCell();
    cell.setVerticalAlignment(5);
    cell.setHorizontalAlignment(align);
    cell.setPhrase(new Phrase(value, font));
    return cell;
  }
  
  public PdfPCell createCell(String value, Font font)
  {
    PdfPCell cell = new PdfPCell();
    cell.setVerticalAlignment(5);
    cell.setHorizontalAlignment(1);
    cell.setPhrase(new Phrase(value, font));
    return cell;
  }
  
  public PdfPCell createCell(String value, Font font, int align, int colspan)
  {
    PdfPCell cell = new PdfPCell();
    cell.setVerticalAlignment(5);
    cell.setHorizontalAlignment(align);
    cell.setColspan(colspan);
    cell.setPhrase(new Phrase(value, font));
    return cell;
  }
  
  public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag)
  {
    PdfPCell cell = new PdfPCell();
    cell.setVerticalAlignment(5);
    cell.setHorizontalAlignment(align);
    cell.setColspan(colspan);
    cell.setPhrase(new Phrase(value, font));
    cell.setPadding(3.0F);
    if (!boderFlag)
    {
      cell.setBorder(0);
      cell.setPaddingTop(15.0F);
      cell.setPaddingBottom(8.0F);
    }
    return cell;
  }
  
  public PdfPTable createTable(int colNumber)
  {
    PdfPTable table = new PdfPTable(colNumber);
    try
    {
      table.setTotalWidth(this.maxWidth);
      table.setLockedWidth(true);
      table.setHorizontalAlignment(1);
      table.getDefaultCell().setBorder(1);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return table;
  }
  
  public PdfPTable createTable(float[] widths)
  {
    PdfPTable table = new PdfPTable(widths);
    try
    {
      table.setTotalWidth(this.maxWidth);
      table.setLockedWidth(true);
      table.setHorizontalAlignment(1);
      table.getDefaultCell().setBorder(1);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return table;
  }
  
  public PdfPTable createBlankTable()
  {
    PdfPTable table = new PdfPTable(1);
    table.getDefaultCell().setBorder(0);
    table.addCell(createCell("", keyfont));
    table.setSpacingAfter(20.0F);
    table.setSpacingBefore(20.0F);
    return table;
  }
  
  public void generatePDF(List<TestLogEntity> testLogs)
    throws Exception
  {
    PdfPTable table = createTable(12);
    table.addCell(createCell("学生日志信息表", keyfont, 0, 12, false));
    
    table.addCell(createCell("序号", keyfont, 1));
    table.addCell(createCell("学生姓名", keyfont, 1));
    table.addCell(createCell("知识点编号", keyfont, 1));
    table.addCell(createCell("知识点名称", keyfont, 1));
    table.addCell(createCell("试题内容", keyfont, 1));
    table.addCell(createCell("试题类型", keyfont, 1));
    table.addCell(createCell("正确答案", keyfont, 1));
    table.addCell(createCell("开始答题时间", keyfont, 1));
    table.addCell(createCell("结束答题时间", keyfont, 1));
    table.addCell(createCell("学生答题时长", keyfont, 1));
    table.addCell(createCell("学生答案", keyfont, 1));
    table.addCell(createCell("试题结果", keyfont, 1));
    for (TestLogEntity log : testLogs)
    {
      table.addCell(createCell(String.valueOf(log.getUid()), textfont));
      table.addCell(createCell(log.getUserName(), textfont));
      table.addCell(createCell(log.getTutorialNo(), textfont));
      table.addCell(createCell(log.getTutorialName(), textfont));
      
      table.addCell(createCell(log.getChoiceContent(), textfont));
      table.addCell(createCell(log.getQuestionType(), textfont));
      table.addCell(createCell(log.getAnsw(), textfont));
      table.addCell(createCell(toStringByFormat(log.getStartTime(), "yyyy-MM-dd HH:mm:ss"), textfont));
      table.addCell(createCell(toStringByFormat(log.getEndTime(), "yyyy-MM-dd HH:mm:ss"), textfont));
      table.addCell(createCell(new BigDecimal(log.getDuration().longValue()).toString(), textfont));
      table.addCell(createCell(log.getStudentOptAnsw(), textfont));
      table.addCell(createCell(String.valueOf(log.getResult()), textfont));
    }
    this.document.add(table);
    
    this.document.close();
  }
  
  public static String toStringByFormat(Date date, String format)
  {
    if (date != null)
    {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
    }
    return "";
  }
}
