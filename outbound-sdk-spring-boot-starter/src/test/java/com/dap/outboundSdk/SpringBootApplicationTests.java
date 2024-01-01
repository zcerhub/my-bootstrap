package com.dap.outboundSdk;

import com.dap.gateway.client.OutboundNetworkClient;
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
public class SpringBootApplicationTests {

    @Autowired
    private OutboundNetworkClient outboundNetworkClient;

    @Test
    public void baseSendHeaderObjectTest(){

        HelloDto helloDto = outboundNetworkClient
                .sendHeaderObject("aaa", HelloDto.class);
        System.out.println(helloDto);

    }

    @Test
    public void baseSendStrTest(){

        HelloDto helloDto = outboundNetworkClient.send("aaa", HelloDto.class);
        System.out.println(helloDto);

    }

    @Test
    public void baseSendObjectTest(){

        HelloDto helloDto = outboundNetworkClient.send(new HelloDto("hello world"), HelloDto.class);
        System.out.println(helloDto);

    }

/*    @Test
    public void baseSendObjectHeaderTest(){
        OutboundReqDto outboundReqDto = OutboundReqDto.Builder.newBuilder().body(new HelloDto("hello world"))
                .localHead(localHeadReqDto.Builder.newBuilder().build())
                .systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("hello").consumerSeqNo("hello").serviceScene("hello").build())
                .body(new HelloDto("hello world"))
                .appHead(appHeadReqDto.Builer.newBuilder().bizSeqNo("hello").branchId("hello").build()).build();

        HelloDto helloDto = outboundNetworkClient.send(outboundReqDto, HelloDto.class);
        System.out.println(helloDto);
    }*/

    @Test
    public void baseSendObjectHeaderNoHeaderTest(){
        OutboundReqDto outboundReqDto = OutboundReqDto.Builder.newBuilder()
                .body(new HelloDto("hello world")).build();

        HelloDto helloDto = outboundNetworkClient.send(outboundReqDto, HelloDto.class);
        System.out.println(helloDto);

    }

/*    @Test
    public void baseSendObjectHeaderNoHeaderHasappHeadTest(){
        OutboundReqDto outboundReqDto = OutboundReqDto.Builder.newBuilder()
                .body(new HelloDto("hello world")).
                appHead(appHeadReqDto.Builer.newBuilder().branchId("hello").userId("hello").build()).build();

        HelloDto helloDto = outboundNetworkClient.send(outboundReqDto, HelloDto.class);
        System.out.println(helloDto);
    }*/


    @Test
    public void sendHeaderObjectCustomRequestMethodTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).build();

        HelloDto helloDto = outboundNetworkClient.sendHeaderObject("aaa", HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }

    @Test
    public void sendHeaderObjectCustomRequestPathTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).uri("http://localhost:11113/hellosdkpath").build();

        HelloDto helloDto = outboundNetworkClient.sendHeaderObject("aaa", HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }

    @Test
    public void sendHeaderObjectCustomRequestParamsTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).uri("http://localhost:11113/hellosdkpath")
                .queryParam("hello1","param1").queryParam("hello2","param2").build();

        HelloDto helloDto = outboundNetworkClient.sendHeaderObject("aaa", HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }

    @Test
    public void sendHeaderObjectCustomRequestHeaderTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundNetworkClient.sendHeaderObject("aaa", HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }


    @Test
    public void sendObjectCustomRequestHeaderTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundNetworkClient.send("aaa", HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }

    @Test
    public void sendDtoObjectCustomRequestHeaderTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundNetworkClient.send(OutboundReqDto.Builder.newBuilder().body("aaa").build(), HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }

    @Test
    public void sendDtoObjectSysHeaderCustomRequestHeaderTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.GET).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundNetworkClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }

    @Test
    public void sendDtoObjectSysHeaderCustomRequestMethodTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.DELETE).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundNetworkClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class,httpRequest);
        System.out.println(helloDto);

    }


    @Test
    public void sendDtoObjectSysHeaderCustomRequestMethodPutTest(){

        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.PUT).uri("http://localhost:11113/hellosdkpath")
                .header("hello1","header1").header("hello2","header2").build();

        HelloDto helloDto = outboundNetworkClient.send(OutboundReqDto.Builder.newBuilder().
                systemHead(SysHeaderReqDto.Builder.newBuilder().consumerId("aaaa").build())
                .body("aaa").build(), HelloDto.class,httpRequest);
        System.out.println(helloDto);
    }


    private OutboundNetworkClient outboundHttpGetClient;

    @Autowired
    private OutboundRestOperations outboundRestOperations;

    @Autowired(required = false)
    private List<RequestInterceptor> requestInterceptors;
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


}
