package com.dap.sequence.client.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @className SequenceProperties
 * @description 序列配置类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SequenceProperties {

    private static final Logger logger = LoggerFactory.getLogger(SequenceProperties.class);
    private static final Double[] EXPANSION_RATIOS = {0.9, 0.5, 0.1};

    /**
     * 计算是否补仓
     *
     * @param total total
     * @param size size
     * @return boolean
     */
    public static boolean isNeedExpansionRatio(Integer total, Integer size) {
        for (double er : EXPANSION_RATIOS) {
            Integer value = new Double(er * total).intValue();
            if (size.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
