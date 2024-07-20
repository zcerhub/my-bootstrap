package com.dap.paas.console.seq.service.impl;

import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dao.SeqServiceClusterDao;
import com.dap.paas.console.seq.dao.SeqServiceNodeDao;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.enums.NodeRunState;
import com.dap.paas.console.seq.plugin.SeqMachineOperation;
import com.dap.paas.console.seq.service.SeqServiceClusterService;
import com.dap.paas.console.seq.service.SeqServiceNodeService;
import com.dap.paas.console.seq.util.SequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className SeqServiceNodeServiceImpl
 * @description 序列服务集群实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqServiceNodeServiceImpl extends AbstractBaseServiceImpl<SeqServiceNode, String> implements SeqServiceNodeService {

    @Autowired
    private SeqServiceNodeDao nodeDao;

    @Autowired
    private SeqServiceClusterDao clusterDao;

    @Autowired
    private SeqMachineOperation machineOperation;

    @Autowired
    private MachineService machineService;

    @Autowired
    private SeqServiceClusterService serviceClusterService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(String id) {
        SeqServiceNode node = nodeDao.getObjectById(id);

        Machine machine = machineService.getObjectById(node.getMachineId());
        if (machine == null) {
            nodeDao.delObjectById(id);
            return ResultUtils.success("机器不存在，实际运行服务需要手动删除");
        }

        boolean remove = machineOperation.remove(node, machine);
        if (remove) {
            nodeDao.delObjectById(id);
            // 判断集群中所有节点是否已被删除，集群中没有节点时集群状态设置为“停止”
            String clusterId = node.getClusterId();
            SeqServiceNodeVo nodeVo = new SeqServiceNodeVo();
            nodeVo.setClusterId(clusterId);
            SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(nodeVo::setTenantId);
            List<SeqServiceNodeVo> registerInstanceVos = nodeDao.queryNodes(nodeVo);
            if (registerInstanceVos.isEmpty()) {
                SeqServiceCluster cluster = new SeqServiceCluster();
                cluster.setId(clusterId);
                cluster.setUpdateDate(new Date());
                cluster.setStatus(SeqConstant.SEQ_SERVER_ZERO);
                clusterDao.updateObject(cluster);
            }
            return ResultUtils.success();
        } else {
            return ResultUtils.error("node remove error");
        }
    }

    @Override
    public Result update(SeqServiceNode seqServicenode) {
        Integer integer = nodeDao.updateObject(seqServicenode);
        if (integer > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @Override
    public Result insert(SeqServiceNode seqServicenode) {
        String unitId = serviceClusterService.getObjectById(seqServicenode.getClusterId()).getUnitId();
        if (unitId != null && seqServicenode.getUnitId() != null) {
            return ResultUtils.error(301, "序列已存在全局单元化信息");
        }

        Map<String, String> map = new HashMap<>();
        map.put("machineId", seqServicenode.getMachineId());
        map.put("port", seqServicenode.getPort().toString());
        map.put("clusterId", seqServicenode.getClusterId());
        SeqServiceNode objectByMap = nodeDao.getObjectByMap(map);
        if (null != objectByMap) {
            return ResultUtils.error(ResultEnum.EXIST);
        }
        String id = SnowflakeIdWorker.getID();
        seqServicenode.setId(id);
        seqServicenode.setCreateDate(new Date());
        seqServicenode.setUpdateDate(new Date());
        if (seqServicenode.getUnitId() != null) {
            // 集群内单元为1-逻辑单元
            seqServicenode.setUnitType("1");
        }
        Integer integer = nodeDao.saveObject(seqServicenode);
        if (integer > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @Override
    public void saveClusterNode(List<SeqServiceNode> nodeList, String clusterId) {
        for (SeqServiceNode clusterNode : nodeList) {
            clusterNode.setClusterId(clusterId);
            clusterNode.setStatus(NodeRunState.RUN.getKey() + "");
            saveObject(clusterNode);
        }
    }

    @Override
    public List<SeqServiceNodeVo> queryNodes(SeqServiceNodeVo node) {
        if (StringUtils.isBlank(node.getTenantId())) {
            SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(node::setTenantId);
        }
        return nodeDao.queryNodes(node);
    }

    @Override
    public List<SeqServiceNode> serviceMonitorNumber(Map map) {
        return nodeDao.serviceMonitorNumber(map);
    }

    @Override
    public Result start(SeqServiceNode node) {
        return machineOperation.start(node);
    }

    @Override
    public Result stop(SeqServiceNode node) {
        return machineOperation.stop(node);
    }
}
