package com.dap.sequence.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.dap.sequence.api.util.SeqKeyUtils;

/**
 * @author: liu
 * @date: 2022/3/1 14:00
 * @description:
 */
public class SeqParamsDto {

    /**
     * 序列设计器中配置的 序列编号
     */
    private String seqCode;

    /**
     * 用于回拨模式的 日期拼接串
     */
    private String day;

    private String tenantId;

    /**
     * 自选参数
     */
    private String seqVal;

    /**
     * 占位符参数
     */
    private List<String> params = new ArrayList<String>();

    private String clientPort;

    private Long seqDate;

    private String seqDesignId;

    /**
     * 序列全量字段
     */
    private String serialNumber;

    /**
     * 请求数量
     */
    private Integer requestNumber;

    /**
     * 新增数量
     */
    private Integer createNumber;

    /**
     * 是否同步生产
     */
    private boolean isAsync;

    private boolean isMax;

    private String curNumId;

    private boolean enableDatePlaceholder;

    public SeqParamsDto() {
    }

    public SeqParamsDto(String seqCode) {
        this.seqCode = seqCode;
    }

    public SeqParamsDto(String seqCode, String day) {
        this.seqCode = seqCode;
        this.day = day;
    }

    public boolean isEnableDatePlaceholder() {
        return enableDatePlaceholder;
    }

    public void setEnableDatePlaceholder(boolean enableDatePlaceholder) {
        this.enableDatePlaceholder = enableDatePlaceholder;
    }

    public String clientCacheKey() {
        return SeqKeyUtils.getSeqClientCacheKey(seqCode, day);
    }

    public String engineCacheKey() {
        return SeqKeyUtils.getSeqEngineKey(seqCode, tenantId);
    }

    public String seqCacheKey() {
        return SeqKeyUtils.getSeqCacheKey(seqCode, day, tenantId);
    }

    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSeqVal() {
        return seqVal;
    }

    public void setSeqVal(String seqVal) {
        this.seqVal = seqVal;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSeqDate() {
        return seqDate;
    }

    public void setSeqDate(Long seqDate) {
        this.seqDate = seqDate;
    }

    public String getSeqDesignId() {
        return seqDesignId;
    }

    public void setSeqDesignId(String seqDesignId) {
        this.seqDesignId = seqDesignId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Integer requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Integer getCreateNumber() {
        return createNumber;
    }

    public void setCreateNumber(Integer createNumber) {
        this.createNumber = createNumber;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public boolean isMax() {
        return isMax;
    }

    public void setMax(boolean max) {
        isMax = max;
    }

    public String getCurNumId() {
        return curNumId;
    }

    public void setCurNumId(String curNumId) {
        this.curNumId = curNumId;
    }
}
