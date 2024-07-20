package com.dap.paas.console.basic.utils.encrypt;

public class Keys {

	/**
	 * DES 加密信息
	 */
	static String keyStr = "Dvxchuy8qwertnBk";// 密钥
	static String iv = "12345678";// iv加密向量
	static String algorithm = "DESede/CBC/PKCS7Padding";// 填充方式(加密方式)DESede/CBC/PKCS5Padding和 DESede/CBC/PKCS7Padding
	static String charset = "utf-8";// 编码方式
	
	/**
	 * sm4加密信息
	 * 
	 */
	
	static String  Sm4_iv="";
	static String Sm4_SecretKey="ugDzswUgvxcW9167";
}
