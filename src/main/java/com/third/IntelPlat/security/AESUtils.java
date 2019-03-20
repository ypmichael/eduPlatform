package com.third.IntelPlat.security;


import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.codec.Base64;


public class AESUtils {
    
     private static final String ALGO = "AES";
     private static final String SecurityKey = "E8 5ecur!ty Key#";

     public static String encrypt(String Data){
    	 
    	 byte[] encVal = null;
    	 try{
	    	// encrypt
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.ENCRYPT_MODE, generateKey());
	        encVal = c.doFinal(Data.getBytes());
    	 }
    	 catch(Exception e){
    		 e.printStackTrace();
    		 throw new AuthenticationException("{Token Encrypt or decrypt error, please contact amdin!}");		
    	 }
	        
        // base64 encode
        String encryptedValue = Base64.encodeToString(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData){

    	// base64 decode
        byte[] decordedValue = Base64.decode(encryptedData);
        byte[] decValue = null;
	   	try{
	        // decrypt
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.DECRYPT_MODE, generateKey());
	        decValue = c.doFinal(decordedValue);
		 }
		 catch(Exception e){
			 e.printStackTrace();
    		 throw new AuthenticationException("{Token Encrypt or decrypt error, please contact amdin!}");		
		 }

        return new String(decValue);
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(SecurityKey.getBytes(), ALGO);
        return key;
    }
    
    /** 
     * 字符串 SHA 加密 
     *  
     * @param strSourceText 
     * @return 
     */  
    public static String SHA(final String strText, final String strType) 
	{
		// 返回值
		String strResult = null;
		// 是否是有效字符串
		if (strText != null && strText.length() > 0) {
			try {
				// SHA 加密开始
				MessageDigest messageDigest = MessageDigest
						.getInstance(strType);
				messageDigest.update(strText.getBytes());
				byte byteBuffer[] = messageDigest.digest();

				StringBuffer strHexString = new StringBuffer();
				for (int i = 0; i < byteBuffer.length; i++) {
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1) {
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回結果
				strResult = strHexString.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return strResult;
	}

}