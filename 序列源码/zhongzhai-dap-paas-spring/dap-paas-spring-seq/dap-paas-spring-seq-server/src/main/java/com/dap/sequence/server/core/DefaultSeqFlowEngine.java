package com.dap.sequence.server.core;

import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqDesignService;
import com.dap.sequence.server.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @Description: 默认处理引擎
 * @Author: liu
 * @Date: 2022/2/11
 */
public class DefaultSeqFlowEngine extends AbstractSeqFlowEngine {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSeqFlowEngine.class);

    private SeqDesignService seqDesignService = SpringUtils.getApplicationContext().getBean(SeqDesignService.class);

    /**
     * 对应唯一的引擎加载所需的序列配置
     */
    @Override
    public void loadConf() {
        SeqObj seqObj = Optional.ofNullable(this.getSeqObj()).orElseGet(SeqObj::new);
        Map<String, String> map = new HashMap<>();
        map.put("seqCode", this.getSeqCode());
        map.put("sequenceStatus", SequenceConstant.DESIGN_STATE_ON);
        SeqDesignPo seqDesignPo = seqDesignService.getObjectByCode(map);
        BeanUtils.copyProperties(seqDesignPo, seqObj);
        try {
            setCacheThreshold(seqDesignPo, seqObj);
            seqObj.setRuleInfos(SeqHolder.getRulesMap().get(seqObj.getId()));
        } catch (Exception e) {
            LOG.error("loadConf error.msg = {}", e.getMessage(), e);
            return;
        }
        this.setSeqObj(seqObj);
        LOG.info("{}配置加载完成!!", seqObj.getSequenceName());
    }

    @Override
    public void loadConf(SeqDesignPo seqDesignPo) {
        SeqObj seqObj = Optional.ofNullable(this.getSeqObj()).orElseGet(SeqObj::new);
        BeanUtils.copyProperties(seqDesignPo, seqObj);
        seqObj.setRuleInfos(SeqHolder.getRulesMap().get(seqObj.getId()));
        setCacheThreshold(seqDesignPo, seqObj);
        this.setSeqObj(seqObj);
        LOG.info("{}配置加载完成!!", seqObj.getSequenceName());
    }

    private void setCacheThreshold(SeqDesignPo seqDesignPo, SeqObj seqObj) {
        seqObj.setServerCacheThreshold(Integer.parseInt(seqDesignPo.getServerCacheThreshold()));
        seqObj.setClientCacheThreshold(Integer.parseInt(seqDesignPo.getClientCacheThreshold()));
    }
}
