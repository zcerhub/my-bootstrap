package com.dap.sequence.server.enums;

import java.util.Arrays;

/**
 * @className MaximumStrategy
 * @description 序列增长到最大值策略
 * @author renle
 * @date 2023/09/16 10:07:53 
 * @version: V23.06
 */
public enum MaximumStrategy {

    /**
     * 继续增长
     */
    CONTINUING_TO_GROW(0, "继续增长"),

    /**
     * 重头开始
     */
    STARTING_FROM_SCRATCH(1, "重头开始"),

    /**
     * 最大值
     */
    MAXIMUM_VALUE(2, "最大值"),

    /**
     * 抛出异常
     */
    THROW_AN_EXCEPTION(3, "抛出异常");

    private int sign;

    private String desc;

    MaximumStrategy(int sign, String desc) {
        this.sign = sign;
        this.desc = desc;
    }

    public int getSign() {
        return sign;
    }

    public String getDesc() {
        return desc;
    }

    public static MaximumStrategy sign(Integer sign) {
        return Arrays.stream(values())
                .filter(maximumStrategy -> sign != null && maximumStrategy.sign == sign)
                .findFirst()
                .orElse(MaximumStrategy.THROW_AN_EXCEPTION);
    }
}
