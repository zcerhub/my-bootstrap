package com.dap.sequence.server.core;

import com.dap.sequence.api.core.AbstractSeqFlowEngine;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dto.AlertEntity;
import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.lock.LockImpl;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqUseConditionService;
import com.dap.sequence.server.utils.CommonUtils;
import com.dap.sequence.server.utils.EnvUtils;
import com.dap.sequence.server.utils.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import static com.dap.sequence.server.utils.CommonUtils.restTemplate;

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
    @Value("${gientech.dap.sequence.alertPushUrl}")
    private String baseAlertPushUrl;
    @Value("${gientech.dap.sequence.apikey}")
    private String apikey;
    @Value("${gientech.dap.sequence.appkey}")
    private String appKey;

    @Autowired
    private SeqUseConditionService seqUseConditionService;

    @Async("seqTaskExecutor")
    public void asyncCreate(SeqParamsDto seqParamsDto, SeqProducer seqProducer, com.dap.sequence.server.factory.AbstractSeqFlowEngine
        seqFlowEngine) {
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
    public void asyncScheduledCreate(SeqParamsDto seqParamsDto, SeqProducer seqProducer, com.dap.sequence.server.factory.AbstractSeqFlowEngine seqFlowEngine) {
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
    @Async("seqScheduledExecutor")
    public Future<ResponseEntity<Map>> postAlertMsg(AlertEntity alert) {
        try {
            setDefaultValue(alert);
            LOGGER.debug("告警对象:{}", alert);
            RestTemplate restTemplate = restTemplate(50000, 50000);
            ResponseEntity<Map> mapResponseEntity = CommonUtils.tryTimes(3, 3, () -> restTemplate.postForEntity(alertPushUrl(), alert, Map.class));
            return new AsyncResult<>(mapResponseEntity);
        } catch (Exception e) {
            LOGGER.warn("调用告警服务异常:{}", e.getMessage());
        }
        return null;
    }

    private String alertPushUrl() {
        return baseAlertPushUrl + "?" + "apikey" + "=" + apikey + "&" + "app_key" + appKey;
    }

    /**
     * 拼接 entity_name,entity_addr,app_key,name,properties
     *
     * @param alert 告警对象
     * @return 拼接字串
     */
    private String mergeKey(AlertEntity alert) {
        String split = ",";
        return alert.getEntityName().concat(split).concat(alert.getEntityAddr()).concat(split).concat(appKey).concat(split).concat(alert.getName());
    }

    /**
     * 拼接 entity_addr, app_key, name, properties
     *
     * @param alert 告警对象
     * @return 拼接字串
     */
    private String identifyKey(AlertEntity alert) {
        String split = ",";
        return alert.getEntityName().concat(split).concat(alert.getEntityAddr());
    }

    /**
     * 设置通用值
     *
     * @param alert 告警对象
     */
    private void setDefaultValue(AlertEntity alert) {
        String ip = NetUtils.ip();
        alert.setEntityName("序列服务");
        alert.setEntityAddr(ip);
        alert.setOccurTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        alert.setNetworkDomain("defaultZone");
        alert.setMergeKey(mergeKey(alert));
        alert.setIdentifyKey(identifyKey(alert));
    }
}
