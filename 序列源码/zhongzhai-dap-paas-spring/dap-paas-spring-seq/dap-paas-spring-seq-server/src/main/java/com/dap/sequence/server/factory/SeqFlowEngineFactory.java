package com.dap.sequence.server.factory;


import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.server.core.DefaultSeqFlowEngine;
import com.dap.sequence.server.core.SerialSeqProducer;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.holder.SeqHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 核心引擎生产工厂类
 * @Author: liu
 * @Date: 2022/2/11
 */
@Component
public class SeqFlowEngineFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SeqFlowEngineFactory.class);

    /**
     * 加载引擎
     *
     * @param seq seq
     */
    public static void loadEngine(SeqDesignPo seq) {
        // 一个引擎对象绑定一个序列发布对象，在此处仅仅完成engine属性注入，创建引擎对象。
        // 具体加载数据在每一个引擎对象的loadConf方法中具体体现。
        String engineKey = SeqKeyUtils.getSeqEngineKey(seq.getSequenceCode(), seq.getTenantId());
        AbstractSeqFlowEngine defaultSeqFlowEngine = Optional.ofNullable(SeqHolder.getEngineMap().get(engineKey)).orElseGet(DefaultSeqFlowEngine::new);

        // 连续性序列引擎构建
        AtomicReference<SeqProducer> seqProducerReference = new AtomicReference<>();
        SerialSeqProducer serialSeqProducer = new SerialSeqProducer();
        seqProducerReference.set(serialSeqProducer);
        defaultSeqFlowEngine.setSeqProducer(seqProducerReference);
        defaultSeqFlowEngine.setSeqCode(seq.getSequenceCode());
        defaultSeqFlowEngine.setTenantId(seq.getTenantId());
        // 完成引擎构建 将构建的引擎存储到集合中
        SeqHolder.getEngineMap().put(engineKey, defaultSeqFlowEngine);
        LOG.info("{}引擎加载完成，开始加载配置信息.....", seq.getSequenceName());
    }
}
