package com.dap.paas.console.seq.entity.base;

/**
 * @className StringRuleInfo
 * @description 特字符串规则
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class StringRuleInfo extends BaseRuleInfo {

    private String initData;

    public StringRuleInfo() {
    }

    public StringRuleInfo(String initData) {
        this.initData = initData;
    }

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }

    @Override
    public String toString() {
        return "{" + "\"initData\":\"" +
                initData + '\"' +
                '}';
    }
}
