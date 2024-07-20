package com.dap.sequence.server.factory;


import com.dap.sequence.api.core.SeqFlowEngine;
import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.SequenceUtils;
import com.dap.sequence.server.entity.SeqDesignPo;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @className AbstractSeqFlowEngine
 * @description 序列引擎类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public abstract class AbstractSeqFlowEngine implements SeqFlowEngine {

    private AtomicReference<SeqProducer> seqProducer;

    /**
     * 序列配置对象
     */
    private SeqObj seqObj;

    /**
     * 序列标识
     */
    private String seqCode;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 序列核心处理方法
     */
    @Override
    public void run() {
        loadConf();
        seqObj.setTenantId(tenantId);
        generateSeq(seqObj);
    }

    /**
     * 刷新引擎配置对象调用方法
     */
    @Override
    public void refresh() {
        loadConf();
    }

    /**
     * 刷新序列配置到SeqObj
     *
     * @param seqDesignPo seqDesignPo
     */
    public void refresh(SeqDesignPo seqDesignPo) {
        loadConf(seqDesignPo);
    }

    private void generateSeq(SeqObj seqObj) {
        seqObj.setTenantId(tenantId);
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqObj.getSequenceCode());
        seqParamsDto.setDay(SequenceUtils.getSeqDay(seqObj.getCallbackMode(), new Date()));
        seqProducer.get().createSeqAndCache(seqObj, seqParamsDto);
    }

    /**
     * 对应唯一的引擎加载所需的序列配置
     */
    public abstract void loadConf();

    public abstract void loadConf(SeqDesignPo seqDesignPo);


    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    public void setSeqProducer(AtomicReference<SeqProducer> seqProducer) {
        this.seqProducer = seqProducer;
    }

    public AtomicReference<SeqProducer> getSeqProducer() {
        return seqProducer;
    }

    public SeqObj getSeqObj() {
        return seqObj;
    }

    public void setSeqObj(SeqObj seqObj) {
        this.seqObj = seqObj;
    }


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
