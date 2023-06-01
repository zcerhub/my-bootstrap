package ctbiyi.com.service.impl;

import ctbiyi.com.constant.LicenseVerifiedServiceConstant;
import ctbiyi.com.service.K8sPodInfoService;
import ctbiyi.com.service.LicenseVerifiedService;
import ctbiyi.com.vo.NodeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class LicenseVerifiedServiceImpl implements LicenseVerifiedService {

    @Autowired
    private K8sPodInfoService k8sPodInfoService;

    @Value("${yy.license-server.label-key}")
    private String labelKey;

    @Value("${yy.license-server.label-value}")
    private String labelValue;

    @Value("${yy.license-server.nodeinfo-service-url-template}")
    private String nodeinfoServiceUrlTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void licenseVerified() {
        //1）查找biyi命名空间下带有app=license-server label的pod
        //TODO
        List<String> podIPs=k8sPodInfoService.getPodIPsByLable(labelKey, labelValue);
        log.info("带有app=license-server label的podip为：{}", podIPs);

        //2）调用pod对应的接口获得所在node的物理网卡
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        for (String podIP : podIPs) {
            String  nodeinfoServiceUrl=nodeinfoServiceUrlTemplate.replace(LicenseVerifiedServiceConstant.NODEINFO_SERVICE_URL_TARGET, podIP);
            log.info("替换前的字符串：{}，pod的ip为：{}，替换后的字符串：{}",nodeinfoServiceUrlTemplate,podIP,nodeinfoServiceUrl);

            NodeInfo nodeInfo = getNodeInfoBy(nodeinfoServiceUrl);
            nodeInfoList.add(nodeInfo);
        }

        //3)解析证书中的物理地址





    }

    private NodeInfo getNodeInfoBy(String nodeinfoServiceUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        return restTemplate.getForObject(nodeinfoServiceUrl, NodeInfo.class,headers);
    }


}
