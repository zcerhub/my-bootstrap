package com.dap.paas.console.basic.controller.v1;

import com.dap.paas.console.basic.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @Description: ApplicationControllerTest
 * @CreateTime: 2024/1/25
 */
@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {
    
    @InjectMocks
    private ApplicationController controller;
    
    @Mock
    private ApplicationService applicationService;
    
    @Test
    void should_success_when_syncApplication() throws Exception {
        controller.syncApplication();
        Mockito.verify(applicationService, Mockito.times(1)).syncApplication();
    }
}