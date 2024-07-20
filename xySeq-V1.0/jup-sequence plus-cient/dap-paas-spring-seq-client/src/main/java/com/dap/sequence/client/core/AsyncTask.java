package com.dap.sequence.client.core;

import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.lock.LockImpl;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.factory.AbstractSeqFlowEngine;
import com.dap.sequence.client.holder.SeqHolder;
import com.dap.sequence.client.service.SeqUseConditionService;
import com.dap.sequence.client.utils.EnvUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

/**
 * @className AsyncTask
 * @description 异步任务
 * @author renle
 * @date 2023/11/30 11:25:15 
 * @version: V23.06
 */

@Service
public class AsyncTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTask.class);

    private final LockImpl<String> lockByKey = new LockImpl<>();

    @Autowired
    private SeqUseConditionService seqUseConditionService;

    @Async("seqTaskExecutor")
    public void asyncCreate(SeqParamsDto seqParamsDto, LocalSeqProducer seqProducer, AbstractSeqFlowEngine seqFlowEngine) {
        LOGGER.warn("seqTaskExecutor异步生产{}序列", seqParamsDto.getSeqCode());
        // 计算是否满足阈值 不满足直接返回
        SeqObj seqObj = seqFlowEngine.getSeqObj();
        if (!isAsyncSupplement(seqObj, seqParamsDto)) {
            LOGGER.info("使用量未达到配置阈值：{}，不进行补仓", seqObj.getServerCacheThreshold());
            return;
        }
        if (!lockByKey.isLocked(seqParamsDto.seqCacheKey())) {
            seqParamsDto.setAsync(true);
            seqProducer.createSeqAndCache(seqObj, seqParamsDto);
        }
    }

    @Async("seqScheduledExecutor")
    public void saveUseInfo(List<Object> obj, SeqParamsDto seqParamsDto, SeqDesignPo seqDesignPo, HttpServletRequest httpServletRequest) {
        String clientInfo = EnvUtils.getIpAddress(httpServletRequest) + ":" + seqParamsDto.getClientPort();
        seqUseConditionService.saveUseCondition(obj, seqDesignPo.getSequenceCode(), seqDesignPo, clientInfo);
    }

    @Async("seqScheduledExecutor")
    public void asyncScheduledCreate(SeqParamsDto seqParamsDto, LocalSeqProducer seqProducer, AbstractSeqFlowEngine seqFlowEngine) {
        LOGGER.warn("seqTaskExecutor异步生产{}序列", seqParamsDto.getSeqCode());
        SeqObj seqObj = seqFlowEngine.getSeqObj();
        if (isAsyncSupplement(seqObj, seqParamsDto)) {
            LOGGER.info("使用量达到配置阈值：{}，周期线程进行补仓", seqObj.getServerCacheThreshold());
            String seqCacheKey = seqParamsDto.seqCacheKey();
            if (!lockByKey.isLocked(seqCacheKey)) {
                seqParamsDto.setAsync(true);
                seqProducer.createSeqAndCache(seqFlowEngine.getSeqObj(), seqParamsDto);
            }
        }
    }

    private boolean isAsyncSupplement(SeqObj seqObj, SeqParamsDto seqParamsDto) {
        String seqCacheKey = seqParamsDto.seqCacheKey();
        BlockingQueue<Object> cache = SeqHolder.getSequenceMapBySequenceCode(seqCacheKey);
        int cacheSize = Optional.ofNullable(cache).map(BlockingQueue::size).orElse(0);
        return seqObj.getSequenceNumber() * (100 - seqObj.getServerCacheThreshold()) > cacheSize * 100;
    }
}
