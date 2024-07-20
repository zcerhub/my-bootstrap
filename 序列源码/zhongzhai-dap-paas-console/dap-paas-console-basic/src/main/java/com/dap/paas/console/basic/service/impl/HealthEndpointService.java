package com.dap.paas.console.basic.service.impl;

import com.dap.paas.console.basic.constant.EndpointConsts;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HealthEndpointService extends AbstractEndpointService<Map>{

    @Override
    public String endpoint() {
        return EndpointConsts.HEALTH;
    }
}
