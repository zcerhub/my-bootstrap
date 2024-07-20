package com.dap.sequence.api.util;

import com.dap.sequence.api.constant.SequenceConstant;


/**
 * @className SeqKeyUtils
 * @description 序列Key工具类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SeqKeyUtils {

    /**
     * 获取序列号在缓存中 使用的key
     *
     * @param seqCode seqCode
     * @param day day
     * @param tenantId tenantId
     * @return String
     */
    public static String getSeqCacheKey(String seqCode, String day, String tenantId) {
        return seqCode + SequenceConstant.DAY_SWITCH_SPLIT + day + SequenceConstant.TENANT_POSTFIX + tenantId;
    }

    /**
     * 获取客户端 序列号在缓存中 使用的key
     *
     * @param seqCode seqCode
     * @param day day
     * @return String
     */
    public static String getSeqClientCacheKey(String seqCode, String day) {
        return seqCode + SequenceConstant.DAY_SWITCH_SPLIT + day;
    }

    /**
     * 获取序列设计器、处理器的 key
     *
     * @param seqCode seqCode
     * @param tenantId tenantId
     * @return String
     */
    public static String getSeqEngineKey(String seqCode, String tenantId) {
        return seqCode + SequenceConstant.TENANT_POSTFIX + tenantId;
    }
}
