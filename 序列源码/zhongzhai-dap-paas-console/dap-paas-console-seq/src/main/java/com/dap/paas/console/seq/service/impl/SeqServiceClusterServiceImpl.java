package com.dap.paas.console.seq.service.impl;


import com.base.api.exception.ServiceException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.seq.config.DatasourcePro;
import com.dap.paas.console.seq.constant.SeqConstant;
import com.dap.paas.console.seq.dao.SeqServiceClusterDao;
import com.dap.paas.console.seq.dao.SeqServiceNodeDao;
import com.dap.paas.console.seq.dto.SeqServiceClusterVo;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import com.dap.paas.console.seq.entity.SeqServiceNode;
import com.dap.paas.console.seq.plugin.SeqMachineOperation;
import com.dap.paas.console.seq.service.SeqServiceClusterService;
import com.dap.paas.console.seq.util.SequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @className SeqServiceClusterServiceImpl
 * @description 序列服务集群实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqServiceClusterServiceImpl extends AbstractBaseServiceImpl<SeqServiceCluster, String> implements SeqServiceClusterService {

    @Autowired
    private SeqServiceNodeDao nodeDao;

    @Autowired
    private DatasourcePro datasourcePro;

    @Autowired
    private SeqServiceClusterDao clusterDao;

    @Autowired
    private SeqMachineOperation machineOperation;

    @Override
    public Result delete(String id) {
        SeqServiceCluster cluster = clusterDao.getObjectById(id);
        String status = cluster.getStatus();
        if (status.equals("1")) {
            return ResultUtils.error(ResultEnum.INSTANCE_RUN);
        }
        Integer integer = clusterDao.delObjectById(id);
        if (integer > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @Override
    public Result update(SeqServiceCluster centervCluster) {
        String dbType = centervCluster.getDbType();
        if (dbType.equals("0")) {
            centervCluster.setDbUrl(datasourcePro.getUrl());
            centervCluster.setDbDriver(datasourcePro.getDriverClassName());
            centervCluster.setDbUser(datasourcePro.getUsername());
            centervCluster.setDbPassword(datasourcePro.getPassword());
        }
        Integer integer = clusterDao.updateObject(centervCluster);
        if (integer > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @Override
    public Result insert(SeqServiceCluster centervCluster) {
        Map<String, String> map = new HashMap<>();
        map.put("name", centervCluster.getName());
        SeqServiceCluster registerCluster = clusterDao.getObjectByMap(map);
        if (null != registerCluster) {
            return ResultUtils.error(ResultEnum.EXIST);
        }
        String dbType = centervCluster.getDbType();
        if (dbType.equals("0")) {
            centervCluster.setDbUrl(datasourcePro.getUrl());
            centervCluster.setDbDriver(datasourcePro.getDriverClassName());
            centervCluster.setDbUser(datasourcePro.getUsername());
            centervCluster.setDbPassword(datasourcePro.getPassword());
        }
        String id = SnowflakeIdWorker.getID();
        centervCluster.setId(id);
        centervCluster.setStatus("0");
        centervCluster.setCreateDate(new Date());
        centervCluster.setUpdateDate(new Date());
        Integer integer = clusterDao.saveObject(centervCluster);
        if (integer > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @Override
    public Result start(SeqServiceCluster cluster) {
        if (StringUtils.isBlank(cluster.getId())) {
            return ResultUtils.error("集群id不能为空");
        }
        boolean start = machineOperation.start(cluster);
        if (start) {
            cluster.setStatus(SeqConstant.SEQ_SERVER_ONE);
            clusterDao.updateObject(cluster);
            return ResultUtils.success();
        } else {
            return ResultUtils.error("cluster start error");
        }
    }

    @Override
    public Result stop(SeqServiceCluster cluster) {
        if (StringUtils.isBlank(cluster.getId())) {
            return ResultUtils.error("集群id不能为空");
        }
        boolean stop = machineOperation.stop(cluster);
        if (stop) {
            cluster.setStatus(SeqConstant.SEQ_SERVER_ZERO);
            clusterDao.updateObject(cluster);
            return ResultUtils.success();
        } else {
            return ResultUtils.error("cluster stop error");
        }
    }

    @Override
    public SeqServiceClusterVo getClusterInfo(SeqServiceCluster cluster) {
        if (StringUtils.isBlank(cluster.getTenantId())) {
            SequenceUtil.getTenantId().filter(StringUtils::isNotBlank).ifPresent(cluster::setTenantId);
        }
        SeqServiceClusterVo vo = Optional.ofNullable(clusterDao.getClusterInfo(cluster)).orElseThrow(() -> new ServiceException("查询集群信息为空"));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("clusterId", vo.getId());
        hashMap.put(SeqConstant.SEQ_SERVER_STATUS, SeqConstant.SEQ_SERVER_ONE);
        List<SeqServiceNode> seqServiceNodes = nodeDao.queryList(hashMap);
        vo.setHealthy(seqServiceNodes.size());
        return vo;
    }
}
