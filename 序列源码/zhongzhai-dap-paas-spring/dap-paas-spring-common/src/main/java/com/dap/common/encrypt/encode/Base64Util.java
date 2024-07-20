package com.dap.common.encrypt.encode;


import org.apache.commons.codec.binary.Base64;

/**
 * base64转码
 */
public class Base64Util {
    public static String encrypt(String strIn) throws Exception {
        return new String(Base64.encodeBase64(strIn.getBytes()));
    }
    public static String decrypt(String strIn) throws Exception {
        return new String(Base64.decodeBase64(strIn.getBytes()), "UTF-8");
    }
}
