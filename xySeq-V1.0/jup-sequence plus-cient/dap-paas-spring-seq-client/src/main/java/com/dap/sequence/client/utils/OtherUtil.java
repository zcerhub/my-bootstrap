package com.dap.sequence.client.utils;


import com.dap.sequence.client.core.SequenceQueue;
import org.springframework.util.CollectionUtils;

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
     * 客户端定时刷新规则
     */
    public static final String REFRESH_RULES = "/v1/seq/design/pushRules";
    public static boolean isQueueEmpty(SequenceQueue sequenceQueue) {
        return null == sequenceQueue || CollectionUtils.isEmpty(sequenceQueue.getCacheBlockingQueue());
    }
}
