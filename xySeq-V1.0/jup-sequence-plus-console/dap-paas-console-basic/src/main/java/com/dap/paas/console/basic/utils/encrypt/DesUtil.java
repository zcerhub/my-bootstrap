package com.dap.paas.console.basic.utils.encrypt;

import org.apache.commons.net.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

/**
 * 
 * creator：sjw 
 * createTime：2018年9月4日上午10:42:31
 * useful：    
 * modify
 * ===================================================
 *  modifier            modifytime                description
 * ===================================================
 */
public class DesUtil {

	/**
	 * 此根据自己需要调整
	 */
	private String keyStr = Keys.keyStr;// 密钥
	private String iv = Keys.iv;// iv加密向量
	private String algorithm = Keys.algorithm;// 填充方式(加密方式)DESede/CBC/PKCS5Padding和 DESede/CBC/PKCS7Padding
	private String charset = Keys.charset;// 编码方式
	
	private static DesUtil instance;
	/**
	 * 屏蔽构造方法
	 */
	private DesUtil() {
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static DesUtil getInstance() {
		if (instance == null) {
			synchronized (DesUtil.class) {
				if (instance == null) {
					//Security.addProvider(new com.sun.crypto.provider.SunJCE());
					Security.addProvider(new BouncyCastleProvider());
					//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());// 添加PKCS7Padding支持
					instance = new DesUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * 加密字节数组
	 * 
	 * @param arrB
	 *            需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] arrB) throws Exception {
		IvParameterSpec ivspec = new IvParameterSpec(HexUtils.strToByte(iv));
		Cipher encryptCipher = Cipher.getInstance(algorithm);
		encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(keyStr), ivspec);
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public String encrypt(String strIn) throws Exception {
		IvParameterSpec ivspec = new IvParameterSpec(HexUtils.strToByte(iv));
		Cipher encryptCipher = Cipher.getInstance(algorithm);
		encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(keyStr), ivspec);
//		return HexUtils.toHexByte(encrypt(strIn.getBytes(charset)));
		return new String(Base64.encodeBase64(strIn.getBytes()), "UTF-8");
	}

	/**
	 * 解密字节数组
	 * 
	 * @param arrB
	 *            需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
//	public byte[] decrypt(byte[] arrB) throws Exception {
//		Cipher decryptCipher = Cipher.getInstance(algorithm);
//
//		IvParameterSpec ivspec = new IvParameterSpec(HexUtils.strToByte(iv));
//		decryptCipher.init(Cipher.DECRYPT_MODE, getKey(keyStr), ivspec);
//		return decryptCipher.doFinal(arrB);
//	}

	/**
	 * 解密字符串
	 *            需解密的字符串
	 * @return 解密后的字符串
	 */
//	public String decrypt(String strIn) throws Exception {
//		return HexUtils.toHexString(decrypt(HexUtils.fromHexString(strIn)));
//	}

	private Key getKey(String str) throws InvalidKeyException, InvalidKeySpecException,
	NoSuchAlgorithmException,Exception {
		
		//Key key = new SecretKeySpec(str.getBytes(), algorithm);
		Key key = new SecretKeySpec(str.getBytes("ASCII"), algorithm);
		
		/*DESedeKeySpec dks = new DESedeKeySpec(str.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		SecretKey key = keyFactory.generateSecret(dks);*/
		return key;
	}

	
	
	/**
	 * 从指定字符串生成密钥
//	 * @param s 构成该字符串的字节数组
	 * @return 生成的密钥
	 */
//	public byte[] HexStringToByteArray(String s) {
//		byte[] buf = new byte[s.length() / 2];
//		for (int i = 0; i < buf.length; i++) {
//			buf[i] = (byte) (chr2hex(s.substring(i * 2, i * 2 + 1)) * 0x10 + chr2hex(s
//					.substring(i * 2 + 1, i * 2 + 2)));
//		}
//		return buf;
//	}
	
//	private byte chr2hex(String chr) {
//		if (chr.equals("0")) {
//			return 0x00;
//		} else if (chr.equals("1")) {
//			return 0x01;
//		} else if (chr.equals("2")) {
//			return 0x02;
//		} else if (chr.equals("3")) {
//			return 0x03;
//		} else if (chr.equals("4")) {
//			return 0x04;
//		} else if (chr.equals("5")) {
//			return 0x05;
//		} else if (chr.equals("6")) {
//			return 0x06;
//		} else if (chr.equals("7")) {
//			return 0x07;
//		} else if (chr.equals("8")) {
//			return 0x08;
//		} else if (chr.equals("9")) {
//			return 0x09;
//		} else if (chr.equals("A")) {
//			return 0x0a;
//		} else if (chr.equals("B")) {
//			return 0x0b;
//		} else if (chr.equals("C")) {
//			return 0x0c;
//		} else if (chr.equals("D")) {
//			return 0x0d;
//		} else if (chr.equals("E")) {
//			return 0x0e;
//		} else if (chr.equals("F")) {
//			return 0x0f;
//		}
//		return 0x00;
//	}
//
//	public String getKeyStr() {
//		return keyStr;
//	}
//
//	/**
//	 * 动态修改key初始值
//	 *
//	 * @param keyStr
//	 */
//	public void setKeyStr(String keyStr) {
//		this.keyStr = keyStr;
//		//this.key = getKey(keyStr);
//	}

	public String getIv() {
		return iv;
	}

	/**
	 * 动态修改加密向量
	 * 
	 * @param desStr
	 */
	public void setIv(String desStr) {
		this.iv = desStr;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * 动态修改加密方式/填充方式
	 * 
	 * @param algorithm
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	
	public String getCharset() {
		return charset;
	}
	/**
	 * 
	 * getCharset:设置编码方式
	 * @return 
	 * String
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

}
