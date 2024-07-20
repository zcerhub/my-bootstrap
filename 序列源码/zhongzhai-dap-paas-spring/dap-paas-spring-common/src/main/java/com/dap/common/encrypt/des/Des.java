package com.dap.common.encrypt.des;

import com.dap.common.encrypt.HexUtils;

/**
 * 
 * creator：sjw 
 * createTime：2018年9月4日上午10:42:36
 * useful：    
 * modify
 * ===================================================
 *  modifier            modifytime                description
 * ===================================================
 */
public class Des {

	private static DesUtil des =DesUtil.getInstance();
	
	/**
	 * 3DES解密字节数组
	 * @param b 待加密的字节数组
	 * @return 3DES加密后的节节数组
	 * @throws Exception 
	 */
	public synchronized static byte[] decrypt(byte[] b) throws Exception {
		try {
			return des.decrypt(b);
		} catch (Exception ex) {
			throw new Exception("3DES decrypt byte解密出错",ex);
		}
	}

	/**
	 * 解密字符串
	 * @param strIn 需解密的字符串
	 * @return 解密后的字符串,异常返回空
	 * @throws Exception 
	 */
	public synchronized static String decrypt(String strIn) throws Exception{
		try {
			return new String(decrypt(HexUtils.fromHexString(strIn)),des.getCharset());
		} catch (Exception ex) {
			throw new Exception("3DES decrypt String 解密出错",ex);
		}
	}
	
	/**
	 * 加密字节数组
	 * @param arrB 需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception 
	 */
	public synchronized static byte[] encrypt(byte[] arrB) throws Exception{
		try {
			return des.decrypt(arrB);
		} catch (Exception ex) {
			throw new Exception("3DES encrypt byte 加密出错",ex);
		}
	}
	/**
	 * 加密字符串
	 * @param str 待加密的字符串
	 * @return 3DES加密后的字符串
	 * @throws Exception 
	 */
	public synchronized static String encrypt(String str) throws Exception {
		try {
			return des.encrypt(str);
		} catch (Exception ex) {
			throw new Exception("3DES encrypt String 加密出错",ex);
		}
	}
	


}
