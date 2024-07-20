package com.dap.paas.console.seq.rules;

import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.BaseRuleInfo;
import com.dap.paas.console.seq.entity.base.NumberRuleInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className NumberRuleHandler
 * @description 序列数字规则Handler
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class NumberRuleHandler implements BaseRuleHandler {
    private static final Logger LOG = LoggerFactory.getLogger(NumberRuleHandler.class);

    /**
     * 创建 一个整数格式
     */
    private DecimalFormat integerNumber = new DecimalFormat();

    private static Map<String, Long> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Object generateRule(String id, String message, BaseRuleInfo ruleInfo) {
        NumberRuleInfo numberRuleInfo = (NumberRuleInfo) ruleInfo;
        long currentData = cacheMap.containsKey(id) ?
                cacheMap.get(id) + numberRuleInfo.getNumberStep() :
                numberRuleInfo.getNumberStart();

        if (Math.abs(currentData) > Math.abs(numberRuleInfo.getNumberEnd())) {
            if (numberRuleInfo.getNumberCircle() == null || Objects.equals(numberRuleInfo.getNumberCircle(), 0)) {
                numberRuleInfo.setNowValue(currentData);
            } else if (Objects.equals(numberRuleInfo.getNumberCircle(), 1)) {
                currentData = numberRuleInfo.getNumberStart();
                numberRuleInfo.setNowValue(currentData);
            } else if (Objects.equals(numberRuleInfo.getNumberCircle(), 2)) {
                currentData = numberRuleInfo.getNumberEnd();
                numberRuleInfo.setNowValue(currentData);
            } else {
                LOG.error("序列：{} 超过最大值：{}", message, numberRuleInfo.getNumberEnd());
            }
        } else {
            numberRuleInfo.setNowValue(currentData);
        }

        cacheMap.put(id, currentData);
        if (StringUtils.isNotBlank(numberRuleInfo.getNumberFormat())) {
            integerNumber.applyPattern(numberRuleInfo.getNumberFormat());
            return integerNumber.format(currentData);
        }
        return currentData;

    }

    /**
     * 规则类型  4 数字
     *
     * @return String
     */
    @Override
    public String getRuleName() {
        return "4";
    }
}
