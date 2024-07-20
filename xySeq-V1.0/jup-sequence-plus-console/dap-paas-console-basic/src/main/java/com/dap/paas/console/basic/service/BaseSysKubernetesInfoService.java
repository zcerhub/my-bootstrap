package com.dap.paas.console.basic.service;


import cn.hutool.json.JSONObject;
import com.base.core.service.BaseService;
import com.dap.paas.console.basic.entity.BaseSysKubernetesInfo;

/**
 * (BaseSysKubernetesInfo)表服务接口
 *
 * @author makejava
 * @since 2023-05-18 11:04:08
 */
public interface BaseSysKubernetesInfoService extends BaseService<BaseSysKubernetesInfo, String> {

    JSONObject getNamespaces(String agentId);

    JSONObject getStorageclasses(String agentId);

    JSONObject getImagePullSecrets(String agentId, String namespace);

    JSONObject queryContainerEventLog(String agentId, String namespace, String podName);

    String queryPodStartLog(String agentId, String namespace, String podName);

    void checkAgentState(String agentId);

}
