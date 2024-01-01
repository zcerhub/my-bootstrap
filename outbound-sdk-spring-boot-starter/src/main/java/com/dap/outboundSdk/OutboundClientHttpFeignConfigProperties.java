package com.dap.outboundSdk;

import com.dap.outboundSdk.entity.DefaultRetryer;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("outbound-gateway-sdk.remoting.http.feign")
@Data
public class OutboundClientHttpFeignConfigProperties {

    private String  enabled;
    private String name;

    private String url;

    private DefaultRetryer defaultRetryer;

}
