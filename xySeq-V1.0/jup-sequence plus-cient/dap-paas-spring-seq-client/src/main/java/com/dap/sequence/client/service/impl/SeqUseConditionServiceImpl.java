package com.dap.sequence.client.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.entity.SeqUseCondition;
import com.dap.sequence.client.service.SeqUseConditionService;
import com.dap.sequence.client.utils.SequenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dap.sequence.client.utils.EnvUtils.getAddress;


/**
 * @author: liu
 * @date: 2022/2/22
 * @description:
 */
@Service
public class SeqUseConditionServiceImpl extends AbstractBaseServiceImpl<SeqUseCondition, String> implements SeqUseConditionService {

    private static final Logger LOG = LoggerFactory.getLogger(SeqLocalProducerImpl.class);

    //@Autowired
    //private SeqPlatformService seqFromPlatformService;

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
       /* if (seqFromPlatformService.isUsePlatform()) {
            seqFromPlatformService.saveUseCondition(seqUseCondition);
        } else {*/
            this.saveObject(seqUseCondition);
    }
}
