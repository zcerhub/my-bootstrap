package com.dap.sequence.client.balancer;

import com.dap.dmc.client.service.AuthService;
import com.dap.sequence.client.constant.Constant;
import com.dap.sequence.client.properties.ConfigBeanValue;
import com.dap.sequence.client.utils.NetworkUtil;
import com.dap.sequence.client.utils.OtherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @className RestTemplateBalancer
 * @description 请求模板
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Service
public class RestTemplateBalancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateBalancer.class);

    @Autowired
    ConfigBeanValue cb;

    @Autowired
    private AuthService authService;
    public static AtomicInteger nextServerCounter = new AtomicInteger(0);

    /**
     * 获取所有的Server 结合列表
     *
     * @param urls urls
     * @return List
     */
    public static List<Server> getServerList(String urls) {
        LOGGER.info("getServerList param urls = {}", urls);
        List<Server> serverList = new ArrayList<>();
        if (org.apache.commons.lang.StringUtils.isNotBlank(urls)) {
            String[] urlArray = urls.split(",");
            for (String ipAndPorts : urlArray) {
                String[] split = ipAndPorts.split(":");
                serverList.add(new Server(split[0], Integer.parseInt(split[1])));
            }
        }
        return serverList;
    }

    /**
     * 随机获取一个序列服务
     *
     * @return Server
     */
    private Server getServer() {
        String urls = !StringUtils.hasText(OtherUtil.DEFULT_URLS) ? cb.getUrls() : OtherUtil.DEFULT_URLS;
        List<Server> serverList = getServerList(urls);
        if (CollectionUtils.isEmpty(serverList)) {
            LOGGER.error("获取序列服务地址为空！！！");
            return null;
        }
        // 总大小
        int t = serverList.size();
        Set<String> telnetFalse = new HashSet<>();
        while (telnetFalse.size() < t) {
            Server server = serverList.get(nextServerCounter.getAndUpdate((x) -> x == Integer.MAX_VALUE ? 0 : x + 1) % t);
            if (!NetworkUtil.telnet(server.getHost(), server.getPort())) {
                telnetFalse.add(server.getHost() + ":" + server.getPort());
                continue;
            }
            return server;
        }
        LOGGER.error("无法连接节点:{}", telnetFalse);
        return null;
    }
    /**
     * 返回调用信息
     *
     * @param content  调用传递内容
     * @param loadPath loadPath
     * @return 返回调用数据
     */
    public String getRequest(String content, String loadPath) {
        return request(content, loadPath, HttpMethod.GET);
    }

    /**
     * 返回调用信息
     *
     * @param content  调用传递内容
     * @param loadPath 调用地址
     * @return 返回调用数据
     */
    public String postRequest(String content, String loadPath) {
        return request(content, loadPath, HttpMethod.POST);
    }

    RestTemplate restTemplate = null;

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            OkHttp3ClientHttpRequestFactory httpRequestFactory = new OkHttp3ClientHttpRequestFactory();
            httpRequestFactory.setReadTimeout(cb.getReadTimeout());
            httpRequestFactory.setConnectTimeout(cb.getConnectTimeout());
            restTemplate = new RestTemplate(httpRequestFactory);
        }
        return restTemplate;
    }

    /**
     * 发送请求
     *
     * @param content content
     * @param loadPath loadPath
     * @param httpMethod httpMethod
     * @return String
     */
    public String request(String content, String loadPath, HttpMethod httpMethod) {
        Server server = getServer();
        if (server == null) {
            LOGGER.error("没有可用的序列服务");
            return null;
        }


        // Post请求的url，与get不同的是不需要带参数
        String postUrl = OtherUtil.HTTP + server.getHost() + ":" + server.getPort() + loadPath;
        //分布式管控校验鉴权值以及token拼接
        postUrl= Optional.ofNullable(getEnMsgComponentUrl(postUrl, Constant.SEQUENCE)).orElse(postUrl);
        LOGGER.info("seq request url:{} ,content:{} ,tenantId:{}", postUrl, content, cb.getTenantId());


        HttpHeaders headers = new HttpHeaders();
        headers.setConnection("close");
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Charset> charsets = new ArrayList<>();
        charsets.add(StandardCharsets.UTF_8);
        headers.setAcceptCharset(charsets);
        //  headers.setExpires(60000);
        headers.add("tenantId", cb.getTenantId());
        HttpEntity<String> requestEntity = new HttpEntity<String>(content, headers);

        try {
            ResponseEntity<String> exchange = getRestTemplate().exchange(postUrl, HttpMethod.POST, requestEntity, String.class);
            return exchange.getBody();
        } catch (Exception e) {
            LOGGER.error("seq request fail url:{} ,content:{} ,tenantId:{} ,Exception:{} ", postUrl, content, cb.getTenantId(), e.getMessage());
            return null;
        }
    }


    /**
     *
     * 分布式管控应用权限校验生成鉴权值
     *
     * @param url  请求地址
     * @param tarAppName  目标应用名
     * @return 返回调用数据
     */
    private String getEnMsgComponentUrl(String url,String tarAppName) {
        try {
            return authService.getEnMsgComponentUrl(url,tarAppName);
        }catch (RuntimeException e){
            LOGGER.error("seq getEnMsgComponentUrl fail url:{} ,tenantId:{} ,Exception:{} ", url, cb.getTenantId(), e.getMessage());
            return null;
        }
    }
}
