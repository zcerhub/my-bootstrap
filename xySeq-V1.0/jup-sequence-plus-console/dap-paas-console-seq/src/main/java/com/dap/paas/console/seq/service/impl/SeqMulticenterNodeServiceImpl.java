package com.dap.paas.console.seq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dao.SeqMulticenterNodeDao;
import com.dap.paas.console.seq.dao.SeqServiceClusterDao;
import com.dap.paas.console.seq.dao.SeqServiceNodeDao;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqMulticenterNode;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.service.SeqMulticenterNodeService;
import com.dap.paas.console.seq.util.SequenceUtil;
import com.dap.paas.console.seq.util.http.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className SeqMulticenterNodeServiceImpl
 * @description 序列多中心节点实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqMulticenterNodeServiceImpl extends AbstractBaseServiceImpl<SeqMulticenterNode, String> implements SeqMulticenterNodeService {

    @Autowired
    private SeqMulticenterNodeDao nodeDao;

    @Autowired
    private SeqServiceNodeDao seqServicNodeDao;

    @Autowired
    private SeqServiceClusterDao clusterDao;

    @Override
    public List<SeqMulticenterNode> queryNodes(SeqMulticenterNode node) {
        if (StringUtils.isBlank(node.getTenantId())) {
            SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(node::setTenantId);
        }
        return nodeDao.queryNodes(node);
    }

    @Override
    public Integer switchCurrentDb(JSONObject json) {
        String currentId = (String) json.get(SeqConstant.SEQ_SERVER_CURRENT);
        String switchId = (String) json.get(SeqConstant.SEQ_SERVER_SWICH);
        String id = (String) json.get("id");
        SeqMulticenterNode multicenterNode = nodeDao.getObjectById(id);
        SeqServiceCluster cluster = clusterDao.getObjectById(switchId);
        json.remove(currentId);
        json.remove(switchId);
        json.remove(id);
        json.put(SeqConstant.SEQ_SERVER_URL_ID, switchId);
        json.put(SeqConstant.SEQ_SERVER_DRIVER, cluster.getDbDriver());
        json.put(SeqConstant.SEQ_SERVER_URL, cluster.getDbUrl());
        json.put(SeqConstant.SEQ_SERVER_USERNAME, cluster.getDbUser());
        json.put(SeqConstant.SEQ_SERVER_PASSWORD, cluster.getDbPassword());
        SeqServiceNodeVo seqServiceNodeVo = new SeqServiceNodeVo();
        seqServiceNodeVo.setClusterId(multicenterNode.getSeqClusterId());
        SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(seqServiceNodeVo::setTenantId);
        List<SeqServiceNodeVo> seqServiceNodeVos = seqServicNodeDao.queryNodes(seqServiceNodeVo);
        for (SeqServiceNodeVo seqServiceNodeVo1 : seqServiceNodeVos) {
            if (seqServiceNodeVo1.getStatus().equals(SeqConstant.SEQ_SERVER_ONE)) {
                String s = HttpClientUtil.sendPostByJson(SeqConstant.SEQ_SERVER_SWITCH_HTTP + seqServiceNodeVo1.getHostIp() + ":" + seqServiceNodeVo1.getPort() + SeqConstant.SEQ_SERVER_SWITCH_URL, json.toJSONString(), 3);
                if (s.contains(SeqConstant.SEQ_SERVER_TWO_HUNDRED)) {
                    multicenterNode.setCurrentDb(switchId);
                    multicenterNode.setStatus(SeqConstant.SEQ_SERVER_ONE);
                    nodeDao.updateObject(multicenterNode);
                }
            }
        }
        return 1;
    }
}
