package com.dap.sequence.server.utils;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.base.api.exception.ServiceException;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.server.enums.MaximumStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dap.sequence.api.core.BaseRuleHandler;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.holder.SeqHolder;
import org.springframework.util.CollectionUtils;

/**
 * @className SequenceUtil
 * @description 序列工具
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SequenceUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SequenceUtil.class);

    /**
     * 测试
     *
     * @param seqObj seqObj
     * @return String
     * @throws Exception Exception
     */
    public static String testSequence(SeqObj seqObj) throws Exception {

        List<Rule> rules = seqObj.getRuleInfos();
        if (!CollectionUtils.isEmpty(rules)) {
            StringBuffer seq = new StringBuffer();
            for (Rule rule : rules) {
                BaseRuleHandler ruleHandler = SeqHolder.getRuleHandlerByName(rule.getRuleType());
                seq.append(Objects.requireNonNull(ruleHandler).generateRule(seqObj, rule, new RuleParams()));
            }
            LOG.info(" test result (seq)={}", seq);
            return seq.toString();
        }
        return null;
    }

    /**
     * UUID生产
     *
     * @return String
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 数字规则达到最大值特殊处理
     *
     * @param currentData currentData
     * @param rule rule
     * @return long
     */
    public static long getSeqNumberCurrentData1(Long currentData, Rule rule) {
        long result = currentData;
        NumberRuleInfo numberRuleInfo = (NumberRuleInfo)rule.getRuleData();
        if (currentData > numberRuleInfo.getNumberEnd()) {
            switch (MaximumStrategy.sign(numberRuleInfo.getNumberCircle())) {
                case CONTINUING_TO_GROW:
                    break;
                case STARTING_FROM_SCRATCH:
                    result = numberRuleInfo.getNumberStart();
                    break;
                case MAXIMUM_VALUE:
                    result = numberRuleInfo.getNumberEnd();
                    break;
                default:
                    throw new ServiceException("序列规则[" + rule.getRuleName() + "]达到最大值");
            }
        }
        return result;
    }

    public static long getSeqNumberCurrentData(Long currentData, NumberRuleInfo numberRuleInfo) {
        long result = currentData;
        switch (numberRuleInfo.getNumberCircle()) {
            case 0:
                break;
            case 1:
                result = numberRuleInfo.getNumberStart();
                break;
            default:
                result = numberRuleInfo.getNumberEnd();
        }
        return result;
    }
}
