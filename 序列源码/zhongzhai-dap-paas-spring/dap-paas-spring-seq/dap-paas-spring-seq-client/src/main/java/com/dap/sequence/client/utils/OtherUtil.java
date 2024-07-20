package com.dap.sequence.client.utils;


/**
 * @className OtherUtil
 * @description 工具类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class OtherUtil {
    private OtherUtil(){

    }


    /**
     * SDK 发送监控数据到 server端地址
     */
    public static final String MONITOR_SDK_URL = "/v1/seq/publish/insert/sdk";

    /**
     * 序列模板获取方法
     */
    public static final String SEQ_CONSUMER_URL = "/v1/seq/publish/getStringSeq";

    /**
     * 序列严格递增接口
     */
    public static final String SEQ_INCREASE_CONSUMER_URL = "/v1/seq/publish/getIncreaseSeq";

    /**
     * 回收序列接口
     */
    public static final String SEQ_RECOVERY_QUERY_URL = "/v1/seq/publish/getRecoverySeq";

    /**
     * 序列回收接口
     */
    public static final String SEQ_RECOVERY_URL = "/v1/seq/publish/recoverySeq";

    /**
     * 自选序列服务端获取接口
     */
    public static final String SEQ_OPTIONAL_URL = "/v1/seq/publish/getOptionalSeq";


    public static final String SEQ_OPTIONAL_SELECTED_URL = "/v1/seq/publish/selectedOptionalSeq";

    /**
     * 自选序列服务端取消接口
     */
    public static final String SEQ_OPTIONAL_CANCEL_URL = "/v1/seq/publish/cancelOptionalSeq";

    /**
     * SDK 请求server端 获取所有 Server 的地址服务器信息
     */
    public static final String GET_URL = "/v1/seq/publish/getStringIps";

    /**
     * SDK 请求server端 获取所有 序列规则信息
     */
    public static final String GET_SEQ_DESIGN_OBJ = "/v1/seq/publish/getSeqDesignObj";

    /**
     * SDK 请求server端 获取所有 序列规则信息
     */
    public static final String CHECK_ACCESSKEY = "/v1/seq/publish/checkAccessKey";

    public static String DEFULT_URLS = "";


    /**
     * SDK 服务端回收客户端缓存的数据
     */
    public static final String CacheCallBack_URL = "/v1/seq/callback/shutdownCallbackSeq";

    public static final String HTTP = "http://";


    /**
     * 客户端启动时，向序列中心申请workId
     */
    public static final String SNOWFLAKE_INIT_WORK_ID = "/v1/seq/publish/snowflakewordid";

    /**
     * 客户端启动后，维护心跳
     */
    public static final String SNOWFLAKE_HEART_BEAT = "/v1/seq/publish/heartbeat";

}
