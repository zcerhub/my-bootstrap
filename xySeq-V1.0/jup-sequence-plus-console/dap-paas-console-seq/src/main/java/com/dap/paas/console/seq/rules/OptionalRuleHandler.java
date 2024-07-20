package com.dap.paas.console.seq.rules;

import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.BaseRuleInfo;
import com.dap.paas.console.seq.entity.base.OptionalRuleInfo;
import com.dap.paas.console.seq.util.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className OptionalRuleHandler
 * @description 序列自选规则Handler
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Slf4j
public class OptionalRuleHandler implements BaseRuleHandler {

    private static final int INT_1 = 1;

    private static final Long LONG_1L = 1L;

    private static Map<String, Long> cacheMap = new ConcurrentHashMap<>();


    @Override
    public Object generateRule(String id, String message, BaseRuleInfo ruleInfo) {
        OptionalRuleInfo optionalRuleInfo = (OptionalRuleInfo) ruleInfo;

        // 最初始入参
        String param = "#";

        // 计算补位长度
        Integer paddindLength = optionalRuleInfo.getOptionalBit() - param.length();

        if (paddindLength <= 0) {
            //序列的入参 (解析后的参数)
            String seqValue = "";
            //截取前面,保留尾部
            if (optionalRuleInfo.getInterceptionStrategy() == INT_1) {
                seqValue = StringUtils.substring(param, param.length() - optionalRuleInfo.getOptionalBit());
            }
            //截取后面，保留头部
            else {
                seqValue = StringUtils.substring(param, 0, optionalRuleInfo.getOptionalBit());
            }
            return seqValue;
        } else {
            Long paddindValue;

            // 补位数字递增
            if (optionalRuleInfo.getPaddindStategy() == INT_1) {
                paddindValue = Optional.ofNullable(cacheMap.get(id)).map(num -> {
                    num = num + INT_1;
                    cacheMap.put(id, num);
                    return num;
                }).orElseGet(() -> {
                    cacheMap.put(id, LONG_1L);
                    return LONG_1L;
                });
            } else {
                // 随机数
                paddindValue = SequenceUtil.getRandomLong(paddindLength);
            }
            return StringUtils.leftPad(paddindValue + param, optionalRuleInfo.getOptionalBit(), "0");
        }
    }


    @Override
    public String getRuleName() {
        return "6";
    }
}
