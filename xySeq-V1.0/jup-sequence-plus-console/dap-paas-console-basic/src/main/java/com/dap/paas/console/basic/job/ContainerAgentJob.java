package com.dap.paas.console.basic.job;

import com.dap.paas.console.basic.constant.ContainerizationConstant;
import com.dap.paas.console.basic.entity.BaseSysKubernetesInfo;
import com.dap.paas.console.basic.service.BaseSysKubernetesInfoService;
import com.dap.paas.console.basic.utils.SpringContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Component
public class ContainerAgentJob implements BaseJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerAgentJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        BaseSysKubernetesInfoService agentInfoService = SpringContext.getBeanNonnull(BaseSysKubernetesInfoService.class);
        RestTemplate restTemplate = SpringContext.getBeanNullable("dapRestTemplate");
        List<BaseSysKubernetesInfo> agentInfos = agentInfoService.queryListByMap(new HashMap());
        if (!CollectionUtils.isEmpty(agentInfos)) {
            agentInfos.stream().forEach(e -> {
                Map<String, String> hashMap = new HashMap();
                hashMap.put("id", e.getId());
                BaseSysKubernetesInfo agentInfo = agentInfoService.getObjectByMap(hashMap);
                try {
                    String requestUrl = ContainerizationConstant.HTTP_REQUEST + agentInfo.getAgentIp() + ":" + agentInfo.getAgentPort() + ContainerizationConstant.CHECK_AGENT_STATE;
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl, String.class);
                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
                        agentInfo.setAgentState(ContainerizationConstant.AGENT_STATE_HEALTH);
                        agentInfoService.updateStatus(agentInfo);
                    } else {
                        agentInfo.setAgentState(ContainerizationConstant.AGENT_STATE_UNHEALTH);
                        agentInfoService.updateStatus(agentInfo);
                    }
                } catch (Exception ex) {
                    LOGGER.error("agent: {} health check  error!", agentInfo.getClusterName(), ex);
                    agentInfo.setAgentState(ContainerizationConstant.AGENT_STATE_UNHEALTH);
                    agentInfoService.updateStatus(agentInfo);
                }
            });
        }
    }

}
