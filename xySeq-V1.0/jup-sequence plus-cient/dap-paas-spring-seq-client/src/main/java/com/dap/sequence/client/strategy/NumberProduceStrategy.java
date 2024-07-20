package com.dap.sequence.client.strategy;

import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.SeqParamsDto;

import java.util.LinkedList;

/**
 * 根据不同序号达到最大值的配置生成序号数值
 *
 * @author lyf
 * @date 2024/3/26
 */
public interface NumberProduceStrategy {

    /**
     * 生产指定数量序号的数值
     *
     * @param startNum 起始序号的数值
     * @param size 指定数量
     * @param numberRule 序列配置中的数字规则
     * @param seqParamsDto 请求参数
     * @return 返回指定数量的序号数值
     */
    LinkedList<Long> produceNum(long startNum, int size, NumberRuleInfo numberRule, SeqParamsDto seqParamsDto);

}
