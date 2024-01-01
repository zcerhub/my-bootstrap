package com.dap.outboundSdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("outbound-gateway-sdk.remoting.http.request-config")
@Data
public class OutboundClientHttpRequestConfigProperties {

    private Integer  connectTimeout;
    private Integer socketTimeout;

}
