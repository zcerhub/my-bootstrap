package ctbiyi.com.service.impl;


import ctbiyi.com.constant.K8sResourceConstant;
import ctbiyi.com.service.K8sPodInfoService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class K8sPodInfoServiceImpl implements K8sPodInfoService {

    @Value("${yy.license-server.ns}")
    private String licenseServerNs;

    @Autowired
    private ApiClient apiClient;


    @Override
    public List<String> getPodIPsByLable(String lableKey,String lableValue)  {
        V1PodList podList = getPodsByLable(lableKey, lableValue);
        List<String> podIPs = new ArrayList<>();

        for (V1Pod pod:podList.getItems()) {
//            String podIP=pod.getStatus().getPodIP();
            String podIP=pod.getStatus().getHostIP();
            podIPs.add(podIP);
        }

        return podIPs;
    }


    public V1PodList getPodsByLable(String lableKey,String lableValue)  {
        String labelSelector =getLabelSelector(lableKey,lableValue);

        /*
        * apiClient和CoreV1Api是线程安全的，此处可以优化为将CoreV1Api交给spring管理
        * 参考：1)https://www.cnblogs.com/bolingcavalry/p/14260523.html；2）chatGPL
        * */
        CoreV1Api api = new CoreV1Api(apiClient);
        try {
            return api.listNamespacedPod(licenseServerNs, null, null, null, null,
                    labelSelector,null, null, null, null);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String getLabelSelector(String lableKey, String lableValue) {
        return lableKey+ K8sResourceConstant.EQUALITY_OPERATOR+lableValue;
    }



}
