package com.dap.outboundSdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("outbound-gateway-sdk.remoting.http.request")
@Data
public class OutboundClientHttpRequestProperties {

    private String httpMethod;
    private String httpPath;

}
