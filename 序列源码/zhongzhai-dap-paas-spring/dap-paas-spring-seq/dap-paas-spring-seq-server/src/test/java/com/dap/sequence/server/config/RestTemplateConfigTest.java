package com.dap.sequence.server.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @className RestTemplateConfigTest
 * @description TODO
 * @author renle
 * @date 2024/02/06 09:14:41 
 * @version: V23.06
 */
@RunWith(MockitoJUnitRunner.class)
public class RestTemplateConfigTest {

    @InjectMocks
    private RestTemplateConfig restTemplateConfig;

    @Test
    public void restTemplate() {
        RestTemplate restTemplate = restTemplateConfig.restTemplate();
        Assert.assertNotNull(restTemplate);
    }

    @Test
    public void simpleClientHttpRequestFactory() {
        ClientHttpRequestFactory restTemplate = restTemplateConfig.simpleClientHttpRequestFactory();
        Assert.assertNotNull(restTemplate);
    }
}