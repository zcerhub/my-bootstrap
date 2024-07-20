package com.dap.sequence.server.entity;

import com.base.api.entity.BaseEntity;

import java.util.List;


/**
 * (SeqOptionalRecord)实体类
 */
public class SeqOptionalRecord extends BaseEntity {

    private static final long serialVersionUID = 199556413971963378L;
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
     * 乐观锁字段
     */
    private Integer seqLock;
    /**
     * 序列规则实例ID
     */
    private String instanceRuleId;

    /**
     * 自选状态1.已使用   2.已回收
     */
    private String optionalStatus;

    /**
     * 序列全量字段
     */
    private String serialNumber;

    private Integer queryNum;

    /**
     * 自选位全量值
     */
    private Long optionalValue;

    private Long start;

    private Long end;

    /**
     * 状态0.未过滤   1.已过滤
     */
    private String filterStatus;

    private List<Long> optionalList;

    private String curNumId;

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


    public String getInstanceRuleId() {
        return instanceRuleId;
    }

    public void setInstanceRuleId(String instanceRuleId) {
        this.instanceRuleId = instanceRuleId;
    }

    public String getOptionalStatus() {
        return optionalStatus;
    }

    public void setOptionalStatus(String optionalStatus) {
        this.optionalStatus = optionalStatus;
    }

    public Long getPaddindValue() {
        return paddindValue;
    }

    public void setPaddindValue(Long paddindValue) {
        this.paddindValue = paddindValue;
    }

    public Integer getSeqLock() {
        return seqLock;
    }

    public void setSeqLock(Integer seqLock) {
        this.seqLock = seqLock;
    }

    public Integer getQueryNum() {
        return queryNum;
    }

    public void setQueryNum(Integer queryNum) {
        this.queryNum = queryNum;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        this.filterStatus = filterStatus;
    }

    public List<Long> getOptionalList() {
        return optionalList;
    }

    public void setOptionalList(List<Long> optionalList) {
        this.optionalList = optionalList;
    }

    public String getCurNumId() {
        return curNumId;
    }

    public void setCurNumId(String curNumId) {
        this.curNumId = curNumId;
    }
}

