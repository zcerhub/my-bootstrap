package com.dap.paas.console.seq.entity.base;

/**
 * @className SpecialCharRuleInfo
 * @description 特殊字符串规则
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SpecialCharRuleInfo extends BaseRuleInfo {

    /**
     * 占位规则  0:实际长度   1:固定长度
     */
    private Integer placeholderRule;

    /**
     * 占位长度
     */
    private Integer placeholderLength;

    public SpecialCharRuleInfo() {
    }

    public SpecialCharRuleInfo(Integer placeholderRule, Integer placeholderLength) {
        this.placeholderRule = placeholderRule;
        this.placeholderLength = placeholderLength;
    }

    public Integer getPlaceholderRule() {
        return placeholderRule;
    }


    public void setPlaceholderRule(Integer placeholderRule) {
        this.placeholderRule = placeholderRule;
    }

    public Integer getPlaceholderLength() {
        return placeholderLength;
    }

    public void setPlaceholderLength(Integer placeholderLength) {
        this.placeholderLength = placeholderLength;
    }

}
