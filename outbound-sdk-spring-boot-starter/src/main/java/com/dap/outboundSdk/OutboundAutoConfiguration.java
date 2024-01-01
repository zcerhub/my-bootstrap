package com.dap.outboundSdk;


import com.dap.gateway.client.OutboundNetworkClient;
import com.dap.gateway.client.http.OutboundHttpClient;
import com.dap.gateway.intercept.RequestInterceptor;
import com.dap.gateway.intercept.request.ObjectHeaderRequestInterceptor;
import com.dap.gateway.remoting.http.OutboundRestOperations;
import com.dap.gateway.remoting.http.feign.DefaultOutboundFeignRestOperations;
import com.dap.gateway.remoting.http.feign.OutboundFeignRestOperations;
import com.dap.gateway.remoting.http.feign.fault.DefaultOutboundApiFallbackFactory;
import com.dap.gateway.remoting.http.feign.fault.OutboundApiFallbackFactory;
import com.dap.gateway.remoting.http.feign.fault.OutboundFeignRetry;
import com.dap.gateway.request.http.HttpRequest;
import com.dap.gateway.request.http.HttpRequestConfig;
import com.dap.outboundSdk.entity.DefaultRetryer;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@ConditionalOnClass(OutboundNetworkClient.class)
@EnableConfigurationProperties({OutboundClientHttpRequestConfigProperties.class,
        OutboundClientHttpRequestProperties.class,
        OutboundClientHttpFeignConfigProperties.class})
public class OutboundAutoConfiguration {


    @Autowired
    private OutboundClientHttpFeignConfigProperties outboundClientHttpFeignConfigProperties;


    @Configuration
    @ConditionalOnClass(OutboundHttpClient.class)
    @ConditionalOnMissingBean(OutboundNetworkClient.class)
    @ConditionalOnProperty(value = "outbound-gateway-sdk.remoting.http.enabled",
            havingValue = "true", matchIfMissing = true)
    public class OutboundHttpClientConfiguration {

        @Autowired(required = false)
        private List<RequestInterceptor> requestInterce;

        @Autowired
        private OutboundRestOperations outboundRestOperations;

        @Autowired
        private OutboundClientHttpRequestConfigProperties outboundClientHttpRequestConfigProperties;

        @Autowired
        private OutboundClientHttpRequestProperties outboundClientHttpRequestProperties;



        @Bean
        public OutboundNetworkClient outboundNetworkClient() {
            HttpRequestConfig httpRequestConfig =  HttpRequestConfig.Builder.newBuilder()
                .connectTimeout(outboundClientHttpRequestConfigProperties.getConnectTimeout())
                .socketTimeout(outboundClientHttpRequestConfigProperties.getSocketTimeout()).build();

            String httpMethod = outboundClientHttpRequestProperties.getHttpMethod();
            HttpRequest httpRequest = HttpRequest.Builder.newBuilder().method(HttpMethod.resolve(httpMethod)).build();

            return OutboundHttpClient.Builder.newBuilder().requestInterceptors(requestInterce)
                    .requestConfig(httpRequestConfig).httpRequest(httpRequest)
                    .outboundRestOperations(outboundRestOperations).build();
        }


        @Bean
        @ConditionalOnMissingBean(OutboundFeignRestOperations.class)
        @ConditionalOnProperty(value = "outbound-gateway-sdk.remoting.http.feign.enabled",
                havingValue = "true", matchIfMissing = true)
        public OutboundRestOperations outboundFeignRestOperations() {
            return new DefaultOutboundFeignRestOperations();
        }

        @Bean
        @ConditionalOnProperty(value = "outbound-gateway-sdk.request-interceptor.sys-header.enable",
        havingValue = "true",matchIfMissing = true)
        public RequestInterceptor objectHeaderRequestInterceptor() {
            return new ObjectHeaderRequestInterceptor();
        }

        @Bean
        @ConditionalOnMissingBean(OutboundApiFallbackFactory.class)
        @ConditionalOnProperty(name = "outbound-gateway-sdk.remoting.http.feign.default-fuse.enable",
                havingValue = "true",matchIfMissing = true)
        public DefaultOutboundApiFallbackFactory defaultOutboundApiFallbackFactory() {
            return new DefaultOutboundApiFallbackFactory();
        }


        @Bean
        @ConditionalOnMissingBean(Retryer.class)
        @ConditionalOnProperty(name = "outbound-gateway-sdk.remoting.http.feign.default-retryer.enable",
                havingValue = "true",matchIfMissing = true)
        public Retryer outboundFeignRetry(){
            DefaultRetryer outBounddefaultRetryer = outboundClientHttpFeignConfigProperties.getDefaultRetryer();
            return new OutboundFeignRetry(outBounddefaultRetryer.getPeriod(),
                    outBounddefaultRetryer.getMaxPeriod(),outBounddefaultRetryer.getMaxAttempts());
        }


    }





}
