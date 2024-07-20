package com.dap.sequence.client.constant;

/**
 * @className Constant
 * @description 常量类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class Constant {
    /**
     * 非连续
     */
    public static final int SEQUENCE_TYPE_NOSERIAL = 1;

    /**
     * 连续
     */
    public static final int SEQUENCE_TYPE_SERIAL = 2;

    public static final Boolean DATA_TYPE_STRING = false;
    
    public static final Boolean DATA_TYPE_BIGINT = true;

    /**
     * 目标应用：序列服务端名称 DSE
     */
    public static final String SEQUENCE="dse";
}
