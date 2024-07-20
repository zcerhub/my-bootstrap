package com.dap.sequence.api.obj;


import com.dap.sequence.api.dto.Rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 序列配置对象
 * @Author: liu
 * @Date: 2022/2/11
 * @Version: 1.0.0
 */
public class SeqObj {

    private String id;

    /**
     * 序列名称
     */
    private String sequenceName;

    /**
     * 序列编号
     */
    private String sequenceCode;

    /**
     * 应用id
     */
    private String sequenceApplicationId;

    /**
     * 描述
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

    private String tenantId;

    /**
     * 序列规则对象链表
     */
    private List<Rule> ruleInfos = new ArrayList<>();

    /**
     * 序列日期
     */
    private Date seqDay;

    /**
     * 回拨模式  0 全局序列 1：按日回拨 （到下一日时序号中数字从头开始）  2：按月回拨 3：按年回拨
     */
    private String callbackMode;

    /**
     * 服务端缓存补仓阈值
     */
    private Integer serverCacheThreshold;

    /**
     * 客户端缓存补仓阈值
     */
    private Integer clientCacheThreshold;

    /**
     * 服务端回收：0-不回收 1-回收
     */
    private String serverRecoverySwitch;

    /**
     * 客户端回收：0-不回收 1-回收
     */
    private String clientRecoverySwitch;

    /**
     * 序列生成类型  0服务端生成 1客户端本地生成
     */
    private String sequenceProdType;

    public String getSequenceProdType() {
        return sequenceProdType;
    }

    public void setSequenceProdType(String sequenceProdType) {
        this.sequenceProdType = sequenceProdType;
    }

    public String getCallbackMode() {
        return callbackMode;
    }

    public void setCallbackMode(String callbackMode) {
        this.callbackMode = callbackMode;
    }

    public Date getSeqDay() {
        return seqDay;
    }

    public void setSeqDay(Date seqDay) {
        this.seqDay = seqDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Rule> getRuleInfos() {
        return ruleInfos;
    }

    public void setRuleInfos(List<Rule> ruleInfos) {
        this.ruleInfos = ruleInfos;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getServerCacheThreshold() {
        return serverCacheThreshold;
    }

    public void setServerCacheThreshold(Integer serverCacheThreshold) {
        this.serverCacheThreshold = serverCacheThreshold;
    }

    public Integer getClientCacheThreshold() {
        return clientCacheThreshold;
    }

    public void setClientCacheThreshold(Integer clientCacheThreshold) {
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

    @Override
    public String toString() {
        return "SeqObj{" +
                "id='" + id + '\'' +
                ", sequenceName='" + sequenceName + '\'' +
                ", sequenceCode='" + sequenceCode + '\'' +
                ", sequenceApplicationId='" + sequenceApplicationId + '\'' +
                ", sequenceDesc='" + sequenceDesc + '\'' +
                ", sequenceApplicationName='" + sequenceApplicationName + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", requestNumber=" + requestNumber +
                ", sequenceStatus='" + sequenceStatus + '\'' +
                ", sequenceRule='" + sequenceRule + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", ruleInfos=" + ruleInfos +
                ", seqDay=" + seqDay +
                ", callbackMode='" + callbackMode + '\'' +
                ", serverCacheThreshold=" + serverCacheThreshold +
                ", clientCacheThreshold=" + clientCacheThreshold +
                ", serverRecoverySwitch='" + serverRecoverySwitch + '\'' +
                ", clientRecoverySwitch='" + clientRecoverySwitch + '\'' +
                '}';
    }
}
