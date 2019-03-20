package com.third.IntelPlat.common;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Base64;

public class FileUpload 
{
	/**
	 * 上传图片
	 * @param imgStr
	 * @param imgPath
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static boolean GenerateImage(String imgStr, String imagePath) {// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
	
		try {
			// Base64解码
			byte[] b = Base64.getDecoder().decode(imgStr);
//			for (int i = 0; i < b.length; ++i) {
//				if (b[i] < 0) {// 调整异常数据
//					b[i] += 256;
//				}
//			}
			// 生成jpeg图片
			String imgFilePath = imagePath;// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * 对字节数组字符串进行Base64解码
	 * @param imgStr
	 * @param imagePath
	 * @return
	 * @throws IOException 
	 */
	public static InputStream Decoder(String imgStr) throws IOException 
	{
		if (imgStr == null) // 图像数据为空
			return null;
		
		// 对字节数组字符串进行Base64解码		
		byte[] buf = Base64.getDecoder().decode(imgStr);
//		for (int i = 0; i < buf.length; ++i) {
//			if (buf[i] < 0) {// 调整异常数据
//				buf[i] += 256;
//			}
//		}
		
		InputStream in = new ByteArrayInputStream(buf); 
		
		return in;
	}

}
