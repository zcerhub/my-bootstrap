package com.dap.paas.console.basic.utils.encrypt;

public class SM4_Context {
	public int mode;

	public int[] sk;

	public boolean isPadding;

	public SM4_Context() {
		this.mode = 1;
		this.isPadding = true;
		this.sk = new int[32];
	}
}
