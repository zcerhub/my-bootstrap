package com.dap.sequence.server.service.impl;

import static com.dap.sequence.server.utils.EnvUtils.getAddress;

import java.util.List;

import com.dap.sequence.server.service.SeqPlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.server.entity.SeqDesignPo;
import com.dap.sequence.server.entity.SeqUseCondition;
import com.dap.sequence.server.service.SeqDesignService;
import com.dap.sequence.server.service.SeqUseConditionService;
import com.dap.sequence.server.utils.SequenceUtil;


/**
 * @author: liu
 * @date: 2022/2/22
 * @description:
 */
@Service
public class SeqUseConditionServiceImpl extends AbstractBaseServiceImpl<SeqUseCondition, String> implements SeqUseConditionService {

    private static final Logger LOG = LoggerFactory.getLogger(SeqServerProducerImpl.class);

    @Autowired
    private SeqPlatformService seqFromPlatformService;

    @Value("${server.port}")
    private int port;

    @Override
    public void saveUseCondition(List<Object> obj, String sequenceCode, SeqDesignPo seqDesignPo, String clientInfo) {
        if (obj.isEmpty()) {
            LOG.error("序列{}数量为空", sequenceCode);
            return;
        }
        SeqUseCondition seqUseCondition = new SeqUseCondition();

        seqUseCondition.setClientInfo(clientInfo);
        seqUseCondition.setNumberStart(obj.get(0).toString());
        seqUseCondition.setNumberEnd(obj.get(obj.size() - 1).toString());
        seqUseCondition.setSeqNo(obj.size());

        seqUseCondition.setDesignId(seqDesignPo.getId());
        seqUseCondition.setTenantId(seqDesignPo.getTenantId());
        seqUseCondition.setSeqName(seqDesignPo.getSequenceName());
        String hostAddress = null;
        try {
            hostAddress = getAddress() + ":" + port;
        } catch (SequenceException e) {
            LOG.error("saveUseCondition error.msg = {}", e.getMessage(), e);
        }
        seqUseCondition.setServerInfo(hostAddress);
        seqUseCondition.setId(SequenceUtil.getUUID32());
        if (seqFromPlatformService.isUsePlatform()) {
            seqFromPlatformService.saveUseCondition(seqUseCondition);
        } else {
            this.saveObject(seqUseCondition);
        }
    }
}
