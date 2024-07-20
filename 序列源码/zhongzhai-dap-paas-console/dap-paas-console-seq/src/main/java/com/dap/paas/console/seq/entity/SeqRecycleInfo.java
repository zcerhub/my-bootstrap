package com.dap.paas.console.seq.entity;

import com.base.api.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className SeqRecycleInfo
 * @description 序列回收实体
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SeqRecycleInfo extends BaseEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeqRecycleInfo.class);

    /**
     * 客户端ip
     */
    private String ip;

    /**
     * 回收编号
     */
    private String recycleCode;

    /**
     * 序列id
     */
    private String seqDesignId;

    private Date startTime;

    private Date endTime;

    /**
     * 序列日切
     */
    private String rqDay;

    public String getRqDay() {
        return rqDay;
    }

    public void setRqDay(String rqDay) {
        this.rqDay = rqDay;
    }

    public String getSeqDesignId() {
        return seqDesignId;
    }

    public void setSeqDesignId(String seqDesignId) {
        this.seqDesignId = seqDesignId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        try {
            this.startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        try {
            this.endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRecycleCode() {
        return recycleCode;
    }

    public void setRecycleCode(String recycleCode) {
        this.recycleCode = recycleCode;
    }
}
