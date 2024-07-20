package com.dap.sequence.client.strategy;

import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.SeqParamsDto;

import java.util.LinkedList;

/**
 * @author lyf
 * @date 2024/3/26
 */
public class ContinueGrowProduceStrategy implements NumberProduceStrategy {

    /**
     * 生产指定数量序号的数值
     *
     * @param startNum     起始序号的数值
     * @param size         指定数量
     * @param numberRule   序列配置中的数字规则
     * @param seqParamsDto 请求参数
     * @return 返回指定数量的序号数值
     */
    @Override
    public LinkedList<Long> produceNum(long startNum, int size, NumberRuleInfo numberRule, SeqParamsDto seqParamsDto) {
        LinkedList<Long> numList = new LinkedList<>();
        long step = numberRule.getNumberStep();
        for (int i = 0; i < size; i++) {
            numList.add(startNum);
            startNum += step;

        }
        return numList;
    }
}
