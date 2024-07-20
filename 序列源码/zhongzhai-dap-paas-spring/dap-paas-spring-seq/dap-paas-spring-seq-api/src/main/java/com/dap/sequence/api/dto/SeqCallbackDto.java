package com.dap.sequence.api.dto;

import java.util.List;


/**
 * @className SeqCallbackDto
 * @description 序列回调DTO
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SeqCallbackDto {


    /**
     * 回收需要回收序列的编号
     */
    private String sequenceCode;

    private String day;

    private String tenantId;

    /**
     *回收编号队列
     */
    private List callbackNumQueue;

    public List getCallbackNumQueue() {
        return callbackNumQueue;
    }

    public void setCallbackNumQueue(List callbackNumQueue) {
        this.callbackNumQueue = callbackNumQueue;
    }

    public String getSequenceCode() {
        return sequenceCode;
    }

    public void setSequenceCode(String sequenceCode) {
        this.sequenceCode = sequenceCode;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


}
