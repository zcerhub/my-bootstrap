package com.dap.paas.console.seq.rules;


import com.dap.paas.console.seq.entity.base.BaseRuleHandler;
import com.dap.paas.console.seq.entity.base.BaseRuleInfo;
import com.dap.paas.console.seq.entity.base.EnumRuleInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className EnumRuleHandler
 * @description 序列枚举规则Handler
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class EnumRuleHandler implements BaseRuleHandler {

    private static Map<String, Integer> enumcacheMap = new ConcurrentHashMap<>();

    @Override
    public Object generateRule(String id, String message, BaseRuleInfo ruleInfo) {
        EnumRuleInfo enumRuleInfo = (EnumRuleInfo) ruleInfo;

        int index = enumcacheMap.getOrDefault(id, 0);
        if (index + enumRuleInfo.getNumberStep() > enumRuleInfo.getEnumStore().size()) {
            index = (index + enumRuleInfo.getNumberStep() - enumRuleInfo.getEnumStore().size()) % enumRuleInfo.getEnumStore().size();
        } else {
            index = index + enumRuleInfo.getNumberStep();
        }
        enumcacheMap.put(id, index);
        if (index == 0) {
            return enumRuleInfo.getEnumStore().get(index - 1);
        }

        if (enumRuleInfo.getEnumStore().size() < index - 1) {
            System.out.println("error");
        }
        return enumRuleInfo.getEnumStore().get(index - 1);

    }

    /**
     * 规则类型  2 枚举
     *
     * @return String
     */
    @Override
    public String getRuleName() {
        return "2";
    }

    public static void main(String args[]) {
        System.out.println(1 % 6);
    }
}
