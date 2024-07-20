package com.dap.paas.console.seq.rules;

import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.BaseRuleInfo;
import com.dap.paas.console.seq.entity.base.StringRuleInfo;

/**
 * @className StringRuleHandler
 * @description 序列字符串规则Handler
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class StringRuleHandler implements BaseRuleHandler {

    @Override
    public Object generateRule(String id, String message, BaseRuleInfo ruleInfo)  {
        StringRuleInfo stringRuleInfo=(StringRuleInfo)ruleInfo;
        return stringRuleInfo.getInitData();
    }

    /**
     *  规则类型  1 固定字符串
     * @return
     */
    @Override
    public String getRuleName() {
        return "1";
    }
}
