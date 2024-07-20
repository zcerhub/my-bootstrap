package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;

/**
 * @className SeqUseCondition
 * @description 序列使用情况实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SeqUseCondition extends BaseEntity {

    /**
     * 关联id（序列设计主表id）
     */
    private String designId;

    /**
     * 服务端信息
     */
    private String serverInfo;

    /**
     * 客户端信息
     */
    private String clientInfo;

    /**
     * 起始号段
     */
    private String numberStart;

    /**
     * 终止号段
     */
    private String numberEnd;

    /**
     * 号段数量
     */
    private int seqNo;

    /**
     * 序列名称
     */
    private String seqName;

    private String sequenceName;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getNumberStart() {
        return numberStart;
    }

    public void setNumberStart(String numberStart) {
        this.numberStart = numberStart;
    }

    public String getNumberEnd() {
        return numberEnd;
    }

    public void setNumberEnd(String numberEnd) {
        this.numberEnd = numberEnd;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }
}
