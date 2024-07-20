package com.dap.paas.console.seq.entity.base;

/**
 * @className CheckRuleInfo
 * @description 校验位规则
 * @author renle
 * @date 2023/10/24 09:58:44 
 * @version: V23.06
 */
public class CheckRuleInfo extends BaseRuleInfo {

    /**
     * 校验位数
     */
    private Integer checkLength;

    /**
     * 补位规则
     */
    private Integer checkPosition;

    public Integer getCheckLength() {
        return checkLength;
    }

    public void setCheckLength(Integer checkLength) {
        this.checkLength = checkLength;
    }

    public Integer getCheckPosition() {
        return checkPosition;
    }

    public void setCheckPosition(Integer checkPosition) {
        this.checkPosition = checkPosition;
    }

    @Override
    public String toString() {
        return "CheckRuleInfo{" +
                "checkLength=" + checkLength +
                ", checkPosition='" + checkPosition + '\'' +
                '}';
    }
}
