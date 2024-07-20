package com.dap.paas.console.seq.constant;

/**
 * @className SeqConstant
 * @description 序列常量值
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SeqConstant {

    public static final String SEQ_SERVER_SWITCH_URL = "/v1/seq/publish/cutDataSource";

    public static final String SEQ_SERVER_UPDATE_CACHE_URL = "/v1/seq/publish/updateSeqHolder";

    public static final String SEQ_SDK_SEQ_BUFFER_URL = "/seq-client/getSeq/buffer";

    public static final String SEQ_SERVER_SWITCH_HTTP = "http://";

    public static final String SEQ_SERVER_CURRENT = "currentId";

    public static final String SEQ_SERVER_SWICH = "switchId";

    public static final String SEQ_SERVER_ZERO = "0";

    public static final String SEQ_SERVER_ONE = "1";

    public static final String SEQ_SERVER_SIX = "6";

    public static final String SEQ_SERVER_STATUS = "status";

    public static final String SEQ_SERVER_TWO_HUNDRED = "200";

    public static final String SEQ_SERVER_URL = "datasourceUrl";

    public static final String SEQ_SERVER_URL_ID = "datasourceName";

    public static final String SEQ_SERVER_USERNAME = "datasourceUsername";

    public static final String SEQ_SERVER_PASSWORD = "datasourcePassword";

    public static final String SEQ_SERVER_DRIVER = "driverClassName";

    public static final String SEQ_NODE_STATUS_RUN = "1";

    public static final String SEQ_NODE_STATUS_STOP = "0";

    /** 序列设计状态，完成（已发布） */
    public static final String SEQ_DESIGN_STATUS_RUN = "2";

    /** 序列设计状态，未完成（未发布） */
    public static final String SEQ_DESIGN_STATUS_STOP = "1";

    /**
     * 规则类型  1 固定字符串
     */
    public static final String STRING_RULE = "1";

    /**
     * 规则类型  2 枚举
     */
    public static final String ENUM_RUlE = "2";

    /**
     * 规则类型  3 日期
     */
    public static final String DATE_RUlE = "3";

    /**
     * 规则类型  4 数字
     */
    public static final String NUMBER_RULE = "4";

    /**
     * 占位符 5
     * SpecialCharRule
     */
    public static final String SPECIAL_RULE = "5";

    /**
     * 自选序号 6
     * SpecialCharRule
     */
    public static final String OPTIONAL_RULE = "6";

    /**
     * 校验位 7
     * CheckRule
     */
    public static final String CHECK_RULE = "7";

    /**
     * BASE
     */
    public static final String DAY_BASE = "base";
}
