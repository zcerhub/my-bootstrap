package com.dap.paas.console.seq.rules;

import com.dap.paas.console.basic.utils.RandomUtil;
import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.BaseRuleInfo;
import com.dap.paas.console.seq.entity.base.SpecialCharRuleInfo;
import com.dap.paas.console.seq.util.SequenceUtil;

import java.util.Optional;

/**
 * @className SpecialCharRuleHandler
 * @description 序列特殊字符规则Handler
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SpecialCharRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(String id, String message, BaseRuleInfo ruleInfo) {
        SpecialCharRuleInfo specialCharRuleInfo = (SpecialCharRuleInfo) ruleInfo;
        return SequenceUtil.getRandomLong(Optional.ofNullable(specialCharRuleInfo.getPlaceholderLength()).orElse(6));
    }

    @Override
    public String getRuleName() {
        return "5";
    }
}
