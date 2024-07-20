package com.dap.sequence.client.entity;

import com.base.api.entity.BaseEntity;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.util.SequenceUtils;
import com.dap.sequence.client.entity.base.Rule;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @className SeqDesignPo
 * @description 序列配置
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Data
public class SeqDesignPo extends BaseEntity{

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
    private String sequenceNumber;

    /**
     * 请求数量
     */

    private String requestNumber;

    /**
     * 服务缓存阈值
     */
    private String serverCacheThreshold;

    /**
     * 客户缓存阈值
     */
    private String clientCacheThreshold;

    /**
     * 状态  1未完成 2完成
     */
    private String serverRecoverySwitch;

    /**
     * 状态  1未完成 2完成
     */
    private String clientRecoverySwitch;

    /**
     * 序列生成类型  0服务端生成 1客户端本地生成
     */
    private String sequenceProdType;

    /**
     * 状态  1未完成 2完成
     */
    private String sequenceStatus;

    /**
     * 规则
     */
    private String sequenceRule;



    /**
     * 序列规则对象链表
     */
    private List<Rule> ruleInfos = new ArrayList<>();

    /**
     * 回拨模式  0 全局序列 1：按日回拨 （到下一日时序号中数字从头开始）  2：按月回拨 3：按年回拨
     */
    private String callbackMode;

    /**  appcode */
    private String appCode;

    /**  accesskey */
    private String accessKey;

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

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
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

    public String getSequenceProdType() {
        return sequenceProdType;
    }

    public void setSequenceProdType(String sequenceProdType) {
        this.sequenceProdType = sequenceProdType;
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

    public List<Rule> getRuleInfos() {
        return ruleInfos;
    }

    public void setRuleInfos(List<Rule> ruleInfos) {
        this.ruleInfos = ruleInfos;
    }

    public String getCallbackMode() {
        return callbackMode;
    }

    public void setCallbackMode(String callbackMode) {
        this.callbackMode = callbackMode;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
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
        seqParamsDto.setSequenceProdType(getSequenceProdType());
        Integer num = Integer.valueOf(seqDesignPo.getRequestNumber());
        seqParamsDto.setRequestNumber(num);
        return seqParamsDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeqDesignPo)) return false;
        SeqDesignPo that = (SeqDesignPo) o;
        return Objects.equals(getSequenceName(), that.getSequenceName()) && Objects.equals(getSequenceCode(), that.getSequenceCode()) && Objects.equals(getSequenceApplicationId(), that.getSequenceApplicationId()) && Objects.equals(getSequenceDesc(), that.getSequenceDesc()) && Objects.equals(getSequenceApplicationName(), that.getSequenceApplicationName()) && Objects.equals(getSequenceNumber(), that.getSequenceNumber()) && Objects.equals(getRequestNumber(), that.getRequestNumber()) && Objects.equals(getServerCacheThreshold(), that.getServerCacheThreshold()) && Objects.equals(getClientCacheThreshold(), that.getClientCacheThreshold()) && Objects.equals(getServerRecoverySwitch(), that.getServerRecoverySwitch()) && Objects.equals(getClientRecoverySwitch(), that.getClientRecoverySwitch()) && Objects.equals(getSequenceProdType(), that.getSequenceProdType()) && Objects.equals(getSequenceStatus(), that.getSequenceStatus()) && Objects.equals(getSequenceRule(), that.getSequenceRule()) && Objects.equals(getRuleInfos(), that.getRuleInfos()) && Objects.equals(getCallbackMode(), that.getCallbackMode()) && Objects.equals(getAppCode(), that.getAppCode()) && Objects.equals(getAccessKey(), that.getAccessKey());
    }
}
