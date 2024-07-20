package com.dap.paas.console.seq.entity.base;

/**
 * @className OptionalRuleInfo
 * @description 自选规则信息
 * @date 2023/12/07 10:14:29
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
     * 补位策略  1.数字递增  2.随机数
     */
    private Integer paddindStategy;

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

    @Override
    public String toString() {
        return "OptionalRuleInfo{" +
                "optionalBit=" + optionalBit +
                ", InterceptionStrategy='" + InterceptionStrategy + '\'' +
                ", paddindStategy='" + paddindStategy + '\'' +
                '}';
    }
}
