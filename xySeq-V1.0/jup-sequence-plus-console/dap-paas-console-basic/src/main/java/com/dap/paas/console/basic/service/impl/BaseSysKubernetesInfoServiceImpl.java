package com.dap.paas.console.basic.service.impl;

import cn.hutool.json.JSONObject;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.basic.constant.ContainerizationConstant;
import com.dap.paas.console.basic.entity.BaseSysKubernetesInfo;
import com.dap.paas.console.basic.enums.AgentTypeEnum;
import com.dap.paas.console.basic.service.BaseSysKubernetesInfoService;
import com.dap.paas.console.common.exception.DapWebServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * (BaseSysKubernetesInfo)表服务实现类
 *
 * @author dzf
 * @since 2023-05-18 11:04:08
 */
@Primary
@Service("baseSysKubernetesInfoService")
public class BaseSysKubernetesInfoServiceImpl extends AbstractBaseServiceImpl<BaseSysKubernetesInfo, String> implements BaseSysKubernetesInfoService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JSONObject getNamespaces(String agentId) {
        BaseSysKubernetesInfo agentInfo = this.getObjectById(agentId);
        String requestUrl = ContainerizationConstant.HTTP_REQUEST + agentInfo.getAgentIp() + ":" + agentInfo.getAgentPort();
        if(AgentTypeEnum.REDIS.getValue().equalsIgnoreCase(agentInfo.getType())){
            requestUrl+=ContainerizationConstant.QUERY_REDIS_CONTAINER_NAMESPACES_URL;
        }else {
            requestUrl+=ContainerizationConstant.QUERY_CONTAINER_NAMESPACES_URL;
        }
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl, String.class);
        JSONObject jsonObject = null;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            jsonObject = new JSONObject(responseEntity.getBody());
        } else {
            throw new DapWebServerException("getNamespacesError:" + responseEntity.getStatusCode());
        }
        return jsonObject;
    }

    @Override
    public JSONObject getStorageclasses(String agentId) {
        BaseSysKubernetesInfo agentInfo = this.getObjectById(agentId);
        String requestUrl = ContainerizationConstant.HTTP_REQUEST + agentInfo.getAgentIp() + ":" + agentInfo.getAgentPort() ;
        if(AgentTypeEnum.REDIS.getValue().equalsIgnoreCase(agentInfo.getType())){
            requestUrl+=ContainerizationConstant.QUERY_REDIS_CONTAINER_STORAGE_CLASSES;
        }else {
            requestUrl+=ContainerizationConstant.QUERY_CONTAINER_STORAGE_CLASSES;
        }
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl, String.class);
        JSONObject jsonObject = null;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            jsonObject = new JSONObject(responseEntity.getBody());
        } else {
            throw new DapWebServerException("getStorageclasses:" + responseEntity.getStatusCode());
        }
        return jsonObject;
    }

    @Override
    public JSONObject getImagePullSecrets(String agentId, String namespace) {
        BaseSysKubernetesInfo agentInfo = this.getObjectById(agentId);
        String requestUrl = ContainerizationConstant.HTTP_REQUEST + agentInfo.getAgentIp() + ":" + agentInfo.getAgentPort() + ContainerizationConstant.QUERY_IMAGE_PULL_SECRETS;
        String baseUrl = requestUrl.replace("{namespace}", namespace);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(baseUrl, String.class);
        return new JSONObject(responseEntity.getBody());
    }

    @Override
    public JSONObject queryContainerEventLog(String agentId, String namespace, String podName) {
        BaseSysKubernetesInfo agentInfo = this.getObjectById(agentId);
        String baseUrl = ContainerizationConstant.QUERY_CONTAINER_EVENT_LOG.replace("{namespace}", namespace).replace("{pod_name}", podName);
        String requestUrl = ContainerizationConstant.HTTP_REQUEST + agentInfo.getAgentIp() + ":" + agentInfo.getAgentPort() + baseUrl;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl, String.class);
        return new JSONObject(responseEntity.getBody());
    }

    @Override
    public String queryPodStartLog(String agentId, String namespace, String podName) {
        BaseSysKubernetesInfo agentInfo = this.getObjectById(agentId);
        String baseUrl = ContainerizationConstant.QUERY_POD_START_LOG.replace("{namespace}", namespace).replace("{pod_name}", podName);
        String requestUrl = ContainerizationConstant.HTTP_REQUEST + agentInfo.getAgentIp() + ":" + agentInfo.getAgentPort() + baseUrl;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl, String.class);
        return responseEntity.getBody().toString();
    }

    @Override
    public void checkAgentState(String agentId) {
        BaseSysKubernetesInfo agentInfo = this.getObjectById(agentId);
        String requestUrl = ContainerizationConstant.HTTP_REQUEST + agentInfo.getAgentIp() + ":" + agentInfo.getAgentPort() + ContainerizationConstant.CHECK_AGENT_STATE;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl, String.class);
        agentInfo.setAgentState(ContainerizationConstant.AGENT_STATE_HEALTH);
        this.updateObject(agentInfo);
    }

}
