package com.dap.sequence.api.dto;

import com.dap.sequence.api.util.SeqKeyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liu
 * @date: 2022/3/1 14:00
 * @description:
 */
public class SeqOptionalParamsDto {

    /**
     * 序列设计器中配置的 序列编号
     */
    private String seqCode;

    /**
     * 自选参数
     */
    private String seqVal;

    /**
     * 请求数量
     */
    private Integer requestNumber;

    /**
     * 自选序号
     */
    private String optionalValue;

    /**
     * 用于回拨模式的 日期拼接串
     */
    private String day;

    /**
     * 时间戳
     */
    private Long seqDate;

    /**
     * 序列全量字段
     */
    private String serialNumber;

    private String tenantId;

    /**
     * 客户端端口
     */
    private String clientPort;

    public SeqOptionalParamsDto() {
    }

    public SeqOptionalParamsDto(String seqCode) {
        this.seqCode = seqCode;
    }

    public SeqOptionalParamsDto(String seqCode, String day) {
        this.seqCode = seqCode;
        this.day = day;
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

    public String getOptionalValue() {
        return optionalValue;
    }

    public void setOptionalValue(String optionalValue) {
        this.optionalValue = optionalValue;
    }
}
