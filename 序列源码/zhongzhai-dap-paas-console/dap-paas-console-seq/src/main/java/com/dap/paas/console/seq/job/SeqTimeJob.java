package com.dap.paas.console.seq.job;

import com.dap.paas.console.basic.job.BaseJob;
import com.dap.paas.console.basic.utils.SpringContext;
import com.dap.paas.console.common.util.NetUtils;
import com.dap.paas.console.seq.dao.SeqServiceClusterDao;
import com.dap.paas.console.seq.dao.SeqServiceNodeDao;
import com.dap.paas.console.seq.dto.SeqServiceNodeVo;
import com.dap.paas.console.seq.entity.SeqServiceCluster;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @className SeqTimeJob
 * @description 序列定时任务
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public class SeqTimeJob implements BaseJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqTimeJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LOGGER.warn("SeqTimeJob job monitor excute start ");
        SeqServiceClusterDao registerCluster = SpringContext.getBeanNonnull(SeqServiceClusterDao.class);
        SeqServiceNodeDao seqServiceNodeDao = SpringContext.getBeanNonnull(SeqServiceNodeDao.class);
        try {
            List<SeqServiceCluster> clusters = registerCluster.queryListJob(new SeqServiceCluster());
            for (SeqServiceCluster cluster : clusters) {
                checkClusterJob(registerCluster, seqServiceNodeDao, cluster);
            }
        } catch (Exception e) {
            LOGGER.debug("SeqTimeJob job monitor error {}", e.getMessage());
        }
    }

    /**
     * 检测集群是否可用
     *
     * @param registerCluster registerCluster
     * @param seqServiceNodeDao seqServiceNodeDao
     * @param cluster cluster
     * @throws Exception Exception
     */
    public static void checkClusterJob(SeqServiceClusterDao registerCluster, SeqServiceNodeDao seqServiceNodeDao, SeqServiceCluster cluster) throws Exception {
        SeqServiceNodeVo instance1 = new SeqServiceNodeVo();
        String tenantId = cluster.getTenantId();
        instance1.setClusterId(cluster.getId());
        instance1.setTenantId(tenantId);
        List<SeqServiceNodeVo> instances = seqServiceNodeDao.queryNodes(instance1);
        String clusterStatus = "2";
        for (SeqServiceNodeVo instance : instances) {
            String hostIp = instance.getHostIp();
            Integer port = instance.getPort();
            boolean telnet = NetUtils.telnet(hostIp, port);
            if (telnet) {
                clusterStatus = "1";
                instance.setStatus("1");
            } else {
                instance.setStatus("2");
            }
            seqServiceNodeDao.updateJob(instance);
        }
        cluster.setStatus(clusterStatus);
        registerCluster.updateJob(cluster);
    }

    @Override
    public String getJobName() {
        return "seq";
    }

    @Override
    public String getJobGroup() {
        return "seq";
    }

    @Override
    public String getTriggerName() {
        return "seq";
    }

    @Override
    public String getTriggerGroup() {
        return "seq";
    }
}
