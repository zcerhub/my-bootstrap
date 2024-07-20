package com.dap.sequence.api.dto;

import com.dap.sequence.api.dto.base.BaseRuleInfo;


/**
 * @className OptionalRuleInfo
 * @description 自选规则类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class OptionalRuleInfo extends BaseRuleInfo {

    /**
     * 自选位数
     */
    private Integer optionalBit;

    /**
     * 截取策略  1.向前截取   2.向后截取
     */
    private Integer InterceptionStrategy;

    /**
     * 补位策略  1.数字递增  2.随机数  3.自定义补位
     */
    private Integer paddindStategy;

    /**
     * 自定义补位值
     */
    private String padValue;

    public Integer getOptionalBit() {
        return optionalBit;
    }

    public void setOptionalBit(Integer optionalBit) {
        this.optionalBit = optionalBit;
    }

    public Integer getInterceptionStrategy() {
        return InterceptionStrategy;
    }

    public void setInterceptionStrategy(Integer interceptionStrategy) {
        InterceptionStrategy = interceptionStrategy;
    }

    public Integer getPaddindStategy() {
        return paddindStategy;
    }

    public void setPaddindStategy(Integer paddindStategy) {
        this.paddindStategy = paddindStategy;
    }

    public String getPadValue() {
        return padValue;
    }

    public void setPadValue(String padValue) {
        this.padValue = padValue;
    }

    @Override
    public String toString() {
        return "OptionalRuleInfo{" +
                "optionalBit=" + optionalBit +
                ", InterceptionStrategy=" + InterceptionStrategy +
                ", paddindStategy=" + paddindStategy +
                ", padValue='" + padValue + '\'' +
                '}';
    }
}
