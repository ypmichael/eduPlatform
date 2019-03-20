package com.third.IntelPlat.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	private static final String YAN = "5896t132y9a6sd96f34h59gsdzjwl9fqfnyf3";

	public static String encrypt(String password) {

		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			password = password + YAN;
			byte[] encrypts = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();

			for (byte b : encrypts) {
				int num = b & 0xff;
				String str = Integer.toHexString(num);
				if (str.length() == 1) {
					str = "0" + str;
				}
				sb.append(str);
			}

			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}