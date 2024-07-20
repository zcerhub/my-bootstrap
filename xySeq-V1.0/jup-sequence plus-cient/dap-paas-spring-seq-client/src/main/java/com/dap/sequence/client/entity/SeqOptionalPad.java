package com.dap.sequence.client.entity;

import com.base.api.entity.BaseEntity;


/**
 * (SeqOptionalRecord)实体类
 */
public class SeqOptionalPad extends BaseEntity {

    private static final long serialVersionUID = 199556413971963389L;
    /**
     * 序列编号
     */
    private String seqCode;

    /**
     * 补位值
     */
    private Long paddindValue;

    /**
     * 客户输入参数（如生日、888）
     */
    private String seqValue;

    /**
     * 补位值 + 自选值
     */
    private Long optionalValue;

    private Long start;

    private Long end;

    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    public String getSeqValue() {
        return seqValue;
    }

    public void setSeqValue(String seqValue) {
        this.seqValue = seqValue;
    }

    public Long getPaddindValue() {
        return paddindValue;
    }

    public void setPaddindValue(Long paddindValue) {
        this.paddindValue = paddindValue;
    }

    public Long getOptionalValue() {
        return optionalValue;
    }

    public void setOptionalValue(Long optionalValue) {
        this.optionalValue = optionalValue;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "SeqOptionalPad{" +
                "seqCode='" + seqCode + '\'' +
                ", paddindValue=" + paddindValue +
                ", seqValue='" + seqValue + '\'' +
                ", optionalValue=" + optionalValue +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}

