package com.dap.sequence.api.dto;


import com.dap.sequence.api.dto.base.BaseRuleInfo;


/**
 * @className NumberRuleInfo
 * @description 数字规则类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class NumberRuleInfo extends BaseRuleInfo {

    private Integer numberStep;
    private Long numberStart;
    private Long numberEnd;
    private String numberFormat;

    /**
     * 达到最大时 0继续增长  1从头开始 2最大值
     */
    private Integer numberCircle;

    private Long nowValue = 0L;

    /**
     * 阈值
     */
    private Integer threshold;

    /**
     * 是否开启自选
     */
    private Boolean isOptional;

    public Integer getNumberStep() {
        return numberStep;
    }

    public void setNumberStep(Integer numberStep) {
        this.numberStep = numberStep;
    }

    public Long getNumberStart() {
        return numberStart;
    }

    public void setNumberStart(Long numberStart) {
        this.numberStart = numberStart;
    }

    public Long getNumberEnd() {
        return numberEnd;
    }

    public void setNumberEnd(Long numberEnd) {
        this.numberEnd = numberEnd;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public Integer getNumberCircle() {
        return numberCircle;
    }

    public void setNumberCircle(Integer numberCircle) {
        this.numberCircle = numberCircle;
    }

    public Long getNowValue() {
        return nowValue;
    }

    public void setNowValue(Long nowValue) {
        this.nowValue = nowValue;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Boolean getOptional() {
        return isOptional;
    }

    public void setOptional(Boolean optional) {
        isOptional = optional;
    }
}
