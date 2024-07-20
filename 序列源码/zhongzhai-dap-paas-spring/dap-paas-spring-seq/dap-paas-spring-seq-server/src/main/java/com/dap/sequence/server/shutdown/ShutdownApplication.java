package com.dap.sequence.server.shutdown;

import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.server.entity.SeqRecycleInfo;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqRecycleInfoService;
import com.dap.sequence.server.utils.NetUtils;
import com.dap.sequence.server.utils.SequenceUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import static com.dap.sequence.api.constant.SequenceConstant.TENANT_POSTFIX;

/**
 * @className ShutdownApplication
 * @description 关机操作
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Component
public class ShutdownApplication implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(ShutdownApplication.class);

    @Autowired
    private SeqRecycleInfoService seqRecycleInfoService;

    @Override
    public void afterPropertiesSet() throws Exception {

        /**
         * 回收对应序列号
         */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    ConcurrentHashMap<String, BlockingQueue<Object>> map = SeqHolder.getMap();
                    for (String seqCode : map.keySet()) {
                        String sequenceCode = StringUtils.substringBefore(seqCode, SequenceConstant.DAY_SWITCH_SPLIT);
                        String tenantId = StringUtils.substringAfterLast(seqCode, TENANT_POSTFIX);
                        String rqDay = StringUtils.substringBetween(seqCode, SequenceConstant.DAY_SWITCH_SPLIT, TENANT_POSTFIX);
                        // 判断是否需要回收
                        String engineCacheKey = SeqKeyUtils.getSeqEngineKey(sequenceCode, tenantId);
                        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(engineCacheKey);
                        if (seqFlowEngine == null) {
                            LOG.warn("序列【{}】引擎无法找到，缓存不回收", engineCacheKey);
                            continue;
                        }
                        SeqObj seqObj = seqFlowEngine.getSeqObj();
                        if (seqObj == null) {
                            LOG.warn("序列【{}】无法找到，缓存不回收", engineCacheKey);
                            continue;
                        }
                        String serverRecoverySwitch = seqObj.getServerRecoverySwitch();
                        if (!StringUtils.equals(serverRecoverySwitch, SequenceConstant.RECOVERY_SWITCH_ON)) {
                            LOG.warn("序列【{}】未开启回收功能，缓存不回收", engineCacheKey);
                            continue;
                        }
                        BlockingQueue<Object> sequenceQueue = map.get(seqCode);
                        if (!CollectionUtils.isEmpty(sequenceQueue)) {
                            List<SeqRecycleInfo> seqRecycleInfos = new ArrayList<>();
                            List<Object> list = new ArrayList<>();
                            sequenceQueue.drainTo(list);
                            for (Object seq : list) {
                                // 存储数据库刷库
                                System.out.println(seqCode + "==" + seq);
                                SeqRecycleInfo seqRecycleInfo = new SeqRecycleInfo();
                                seqRecycleInfo.setTenantId(tenantId);
                                seqRecycleInfo.setRqDay(rqDay);
                                seqRecycleInfo.setSeqDesignId(seqObj.getId());
                                seqRecycleInfo.setRecycleCode(seq.toString());
                                seqRecycleInfo.setCreateDate(new Date());
                                seqRecycleInfo.setId(SequenceUtil.getUUID32());
                                seqRecycleInfo.setIp(NetUtils.ip());
                                seqRecycleInfo.setSeqCode(sequenceCode);
                                seqRecycleInfos.add(seqRecycleInfo);
                            }
                            seqRecycleInfoService.insertBatch(seqRecycleInfos);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("afterPropertiesSet error.msg = {}", e.getMessage(), e);
                }
            }
        });
    }
}
