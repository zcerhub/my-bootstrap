package com.dap.sequence.client.entity;

import com.base.api.entity.BaseEntity;

/**
 * @className SeqRecoveryRecord
 * @description 回收序列实体类
 * @author renle
 * @date 2024/02/01 10:23:41 
 * @version: V23.06
 */
public class SeqRecoveryRecord extends BaseEntity {

    private static final long serialVersionUID = 199556413971063378L;

    /**
     * 序列编号
     */
    private String seqCode;

    /**
     * 乐观锁字段
     */
    private Integer seqLock;

    /**
     * 回收状态0.未使用   1.已使用
     */
    private String recoveryStatus;

    /**
     * 序列全量字段
     */
    private String serialNumber;

    private Integer limit;

    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    public Integer getSeqLock() {
        return seqLock;
    }

    public void setSeqLock(Integer seqLock) {
        this.seqLock = seqLock;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    public void setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "SeqRecoveryRecord{" +
                "seqCode='" + seqCode + '\'' +
                ", seqLock=" + seqLock +
                ", recoveryStatus='" + recoveryStatus + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", limit=" + limit +
                '}';
    }
}
