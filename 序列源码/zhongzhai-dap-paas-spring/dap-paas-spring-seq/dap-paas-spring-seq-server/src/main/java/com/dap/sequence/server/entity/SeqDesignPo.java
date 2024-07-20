package com.dap.sequence.server.entity;

import com.base.api.entity.BaseEntity;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.util.SequenceUtils;

import java.util.Date;

/**
 * @Author: liu
 * Data: 2022/1/19 10:05
 * @Descricption:
 */
public class SeqDesignPo extends BaseEntity {

    /**
     * 序列名称
     */
    private String sequenceName;
    /**
     *序列编号
     */
    private String sequenceCode;
    /**
     *应用id
     */
    private String sequenceApplicationId;
    /**
     *描述
     */
    private String sequenceDesc;
    /**
     * 应用名称
     */
    private String sequenceApplicationName;
    /**
     * 缓存数量
     */
    private Integer sequenceNumber;

    /**
     * 请求数量
     */
    private Integer requestNumber;

    /**
     * 状态  1未完成 2完成
     */
    private String sequenceStatus;
    /**
     * 规则
     */
    private String sequenceRule;
    /**
     * 回拨模式  0 全局序列 1：按日回拨 （到下一日时序号中数字从头开始）  2：按月回拨 3：按年回拨
     */
    private String callbackMode;

    /**
     * 服务缓存阈值
     */
    private String serverCacheThreshold;

    /**
     * 客户缓存阈值
     */
    private String clientCacheThreshold;

    /**
     * 服务端回收：0-不回收 1-回收
     */
    private String serverRecoverySwitch;

    /**
     * 客户端回收：0-不回收 1-回收
     */
    private String clientRecoverySwitch;

    public String getCallbackMode() {
        return callbackMode;
    }

    public void setCallbackMode(String callbackMode) {
        this.callbackMode = callbackMode;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getSequenceCode() {
        return sequenceCode;
    }

    public void setSequenceCode(String sequenceCode) {
        this.sequenceCode = sequenceCode;
    }

    public String getSequenceApplicationId() {
        return sequenceApplicationId;
    }

    public void setSequenceApplicationId(String sequenceApplicationId) {
        this.sequenceApplicationId = sequenceApplicationId;
    }

    public String getSequenceDesc() {
        return sequenceDesc;
    }

    public void setSequenceDesc(String sequenceDesc) {
        this.sequenceDesc = sequenceDesc;
    }

    public String getSequenceApplicationName() {
        return sequenceApplicationName;
    }

    public void setSequenceApplicationName(String sequenceApplicationName) {
        this.sequenceApplicationName = sequenceApplicationName;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Integer getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Integer requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getSequenceStatus() {
        return sequenceStatus;
    }

    public void setSequenceStatus(String sequenceStatus) {
        this.sequenceStatus = sequenceStatus;
    }

    public String getSequenceRule() {
        return sequenceRule;
    }

    public void setSequenceRule(String sequenceRule) {
        this.sequenceRule = sequenceRule;
    }

    public String getServerCacheThreshold() {
        return serverCacheThreshold;
    }

    public void setServerCacheThreshold(String serverCacheThreshold) {
        this.serverCacheThreshold = serverCacheThreshold;
    }

    public String getClientCacheThreshold() {
        return clientCacheThreshold;
    }

    public void setClientCacheThreshold(String clientCacheThreshold) {
        this.clientCacheThreshold = clientCacheThreshold;
    }

    public String getServerRecoverySwitch() {
        return serverRecoverySwitch;
    }

    public void setServerRecoverySwitch(String serverRecoverySwitch) {
        this.serverRecoverySwitch = serverRecoverySwitch;
    }

    public String getClientRecoverySwitch() {
        return clientRecoverySwitch;
    }

    public void setClientRecoverySwitch(String clientRecoverySwitch) {
        this.clientRecoverySwitch = clientRecoverySwitch;
    }

    /**
     * 序列转DTO
     *
     * @param seqDesignPo seqDesignPo
     * @return SeqParamsDto
     */
    public SeqParamsDto fromSeqDesignPo(SeqDesignPo seqDesignPo) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(getSequenceCode());
        seqParamsDto.setTenantId(getTenantId());
        seqParamsDto.setDay(SequenceUtils.getSeqDay(getCallbackMode(), new Date()));
        seqParamsDto.setSeqDesignId(getId());
        return seqParamsDto;
    }

    @Override
    public String toString() {
        return "SeqDesignPo{" +
                "sequenceName='" + sequenceName + '\'' +
                ", sequenceCode='" + sequenceCode + '\'' +
                ", sequenceApplicationId='" + sequenceApplicationId + '\'' +
                ", sequenceDesc='" + sequenceDesc + '\'' +
                ", sequenceApplicationName='" + sequenceApplicationName + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", requestNumber=" + requestNumber +
                ", sequenceStatus='" + sequenceStatus + '\'' +
                ", sequenceRule='" + sequenceRule + '\'' +
                ", callbackMode='" + callbackMode + '\'' +
                ", serverCacheThreshold='" + serverCacheThreshold + '\'' +
                ", clientCacheThreshold='" + clientCacheThreshold + '\'' +
                ", serverRecoverySwitch='" + serverRecoverySwitch + '\'' +
                ", clientRecoverySwitch='" + clientRecoverySwitch + '\'' +
                '}';
    }
}
