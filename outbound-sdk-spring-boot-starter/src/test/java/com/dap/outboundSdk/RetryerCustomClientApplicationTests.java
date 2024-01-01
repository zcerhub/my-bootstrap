package com.dap.outboundSdk;

import com.dap.gateway.client.OutboundNetworkClient;
import com.dap.gateway.dto.OutboundReqDto;
import com.dap.gateway.request.http.HttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RetryerCustomClientApplicationTests {


    @Autowired
    private OutboundNetworkClient outboundNetworkClient;

    @Test
    public void sendDtoObjectCustomRequestHeaderTest() throws InterruptedException {
        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).uri("http://localhost:11113/hellosdkpath503503")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundNetworkClient.send(OutboundReqDto.Builder.newBuilder().body("aaa").build(), HelloDto.class,httpRequest);
        System.out.println(helloDto);
    }


}
