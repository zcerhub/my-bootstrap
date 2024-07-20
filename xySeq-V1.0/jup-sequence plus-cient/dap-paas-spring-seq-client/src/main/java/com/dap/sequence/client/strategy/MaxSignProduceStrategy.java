package com.dap.sequence.client.strategy;

import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.SeqParamsDto;

import java.util.LinkedList;

/**
 * 达到最大值后，标记SeqParamsDto，然后返回
 *
 * @author lyf
 * @date 2024/3/26
 */
public class MaxSignProduceStrategy implements NumberProduceStrategy {

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
        Long maxNum = numberRule.getNumberEnd();
        for (int i = 0; i < size; i++) {
            //达到最大值后，设置标识，返回数据
            if (startNum > maxNum) {
                seqParamsDto.setMax(true);
                break;
            }
            numList.add(startNum);
            startNum += step;
        }
        return numList;
    }
}
