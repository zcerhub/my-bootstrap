package com.dap.common.encrypt.sm4;


public class SM4EncryptException extends RuntimeException {
	
	 /** 
	  * serialVersionUID:TODO 
	  */
	private static final long serialVersionUID = -391397376736943289L;
	protected SM4EncryptException(String message, Throwable cause) {
		super(message, cause);
	}
	protected SM4EncryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
