package com.dap.outboundSdk;

import com.dap.gateway.client.http.OutboundHttpClient;
import com.dap.gateway.dto.OutboundReqDto;
import com.dap.gateway.dto.SysHeaderReqDto;
import com.dap.gateway.intercept.RequestInterceptor;
import com.dap.gateway.remoting.http.OutboundRestOperations;
import com.dap.gateway.request.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomClientApplicationTests {

    @Autowired
    private OutboundRestOperations outboundRestOperations;

    @Autowired(required = false)
    private List<RequestInterceptor> requestInterceptors;

    private OutboundHttpClient outboundHttpGetClient;

    @Before
    public void initClient() {
        HttpRequest getRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET)
                .uri("http://localhost:11113/hellosdkpathvoid").build();
        outboundHttpGetClient = OutboundHttpClient.Builder.newBuilder()
                .httpRequest(getRequest).outboundRestOperations(outboundRestOperations)
                .requestInterceptors(requestInterceptors).build();
    }


    @Test
    public void customClientTest(){

        HelloDto helloDto = outboundHttpGetClient.send("aaa", HelloDto.class);
        System.out.println(helloDto);
    }


    @Test
    public void customClientSendDtoObjectSysHeaderCustomRequestMethodPutTest(){

        HelloDto helloDto = outboundHttpGetClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class);
        System.out.println(helloDto);
    }

    @Test
    public void customClientSendResVoidTest(){

         outboundHttpGetClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), Void.class);

    }


    @Test
    public void customClientSendHeaderObjectTest(){

        outboundHttpGetClient.sendHeaderObject("aaa", Void.class);

    }

    @Test
    public void sendDtoObjectSysHeaderCustomRequestMethodPutTest(){


        HelloDto helloDto = outboundHttpGetClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class);
        System.out.println(helloDto);
    }

    @Test
    public void sendDtoObjectSysHeaderCustomRequestMethodTest(){

        HelloDto helloDto = outboundHttpGetClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class);
        System.out.println(helloDto);

    }

    @Test
    public void sendDtoObjectSysHeaderCustomRequestHeaderTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.DELETE).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundHttpGetClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }

    @Test
    public void sendDtoObjectSysHeaderCustomRequestHeaderOutboundTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.DELETE).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundHttpGetClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }


}
