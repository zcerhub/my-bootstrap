package com.dap.outboundSdk;

import com.dap.gateway.client.OutboundNetworkClient;
import com.dap.gateway.request.http.HttpRequest;
import com.dap.gateway.request.http.HttpRequestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomRequestConfigApplicationTests {

    @Autowired
    private OutboundNetworkClient outboundNetworkClient;


    @Test
    public void baseSendHeaderObjectTest(){

        HttpRequestConfig httpRequestConfig = HttpRequestConfig.Builder.newBuilder().socketTimeout(1000).connectTimeout(1000).build();

        HelloDto helloDto = outboundNetworkClient.sendHeaderObject("aaa", HelloDto.class,null,httpRequestConfig);
        System.out.println(helloDto);

    }

    @Test
    public void baseSendHeaderObjectHttpRequestTest(){

        HttpRequestConfig httpRequestConfig = HttpRequestConfig.Builder.newBuilder().socketTimeout(1000).connectTimeout(1000).build();
        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).build();
        HelloDto helloDto = outboundNetworkClient.sendHeaderObject("aaa", HelloDto.class,httpRequest,httpRequestConfig);
        System.out.println(helloDto);

    }


}
