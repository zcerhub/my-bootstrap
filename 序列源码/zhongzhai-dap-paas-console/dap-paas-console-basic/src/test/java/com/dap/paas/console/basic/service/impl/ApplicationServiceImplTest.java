package com.dap.paas.console.basic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.base.core.dao.BaseDao;
import com.base.sys.api.common.Result;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.tenant.dao.TenantManageDao;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.basic.dao.ApplicationDao;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.vo.UacApplicationVo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description: ApplicationServiceImplTest
 * @CreateTime: 2024/1/25
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ApplicationServiceImplTest {
    
    @InjectMocks
    private ApplicationServiceImpl applicationService;
    
    @Mock
    private ApplicationDao applicationDao;
    
    @Mock
    private TenantManageDao tenantManageDao;
    
    @Mock
    private RestTemplate restTemplate;
    
    @Mock
    private BaseDao baseDao;
    
    private final MockedStatic<UserUtil> mockUserUtil = Mockito.mockStatic(UserUtil.class);
    
    private final MockedStatic<UserUtil> mockAuthorizationUtil = Mockito.mockStatic(UserUtil.class);
    
    @AfterEach
    public void after() {
        mockUserUtil.close();
        mockAuthorizationUtil.close();
    }
    
    @Test
    void should_exception_when_syncApplication_with_ucac_url_empty() {
        try {
            applicationService.syncApplication();
        } catch (Exception e) {
            Assertions.assertTrue(true);
            return;
        }
        Assertions.fail();
    }
    
    @Test
    void should_exception_when_syncApplication_with_token_empty() {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        try {
            applicationService.syncApplication();
        } catch (Exception e) {
            Assertions.assertTrue(true);
            return;
        }
        Assertions.fail();
    }
    
    @Test
    void should_exception_when_syncApplication_with_tenant_empty() {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setTenantId("11");
        mockUserUtil.when(UserUtil::getAuthorization).thenReturn("token");
        mockUserUtil.when(UserUtil::getUser).thenReturn(authenticationUserDto);
        Mockito.when(tenantManageDao.getObjectById(Mockito.anyString())).thenReturn(null);
        try {
            applicationService.syncApplication();
        } catch (Exception e) {
            Assertions.assertTrue(true);
            return;
        }
        Assertions.fail();
    }
    
    @Test
    void should_exception_when_syncApplication_with_rest_exception() {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setTenantId("11");
        mockUserUtil.when(UserUtil::getAuthorization).thenReturn("token");
        mockUserUtil.when(UserUtil::getUser).thenReturn(authenticationUserDto);
        TenantManageEntity tenant = new TenantManageEntity();
        tenant.setTenantCode("paas");
        Mockito.when(tenantManageDao.getObjectById(Mockito.anyString())).thenReturn(tenant);
        Mockito.when(
                restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
                        Mockito.eq(Result.class), Mockito.anyString())).thenThrow(new RestClientException(""));
        try {
            applicationService.syncApplication();
        } catch (Exception e) {
            Assertions.assertTrue(true);
            return;
        }
        Assertions.fail();
    }
    
    @Test
    void should_exception_when_syncApplication_with_resp_400() {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setTenantId("11");
        mockUserUtil.when(UserUtil::getAuthorization).thenReturn("token");
        mockUserUtil.when(UserUtil::getUser).thenReturn(authenticationUserDto);
        TenantManageEntity tenant = new TenantManageEntity();
        tenant.setTenantCode("paas");
        Mockito.when(tenantManageDao.getObjectById(Mockito.anyString())).thenReturn(tenant);
        ResponseEntity<String> responseEntity = ResponseEntity.badRequest().body("");
        Mockito.when(
                restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
                        Mockito.eq(String.class), Mockito.anyString())).thenReturn(responseEntity);
        try {
            applicationService.syncApplication();
        } catch (Exception e) {
            Assertions.assertTrue(true);
            return;
        }
        Assertions.fail();
    }
    
    @Test
    void should_success_when_syncApplication_with_resp_empty() throws Exception {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setTenantId("11");
        mockUserUtil.when(UserUtil::getAuthorization).thenReturn("token");
        mockUserUtil.when(UserUtil::getUser).thenReturn(authenticationUserDto);
        TenantManageEntity tenant = new TenantManageEntity();
        tenant.setTenantCode("paas");
        Mockito.when(tenantManageDao.getObjectById(Mockito.anyString())).thenReturn(tenant);
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("[]");
        Mockito.when(
                restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
                        Mockito.eq(String.class), Mockito.anyString())).thenReturn(responseEntity);
        applicationService.syncApplication();
        Mockito.verify(applicationDao, Mockito.times(0)).updateApplicationBatch(Mockito.any());
        Mockito.verify(applicationDao, Mockito.times(0)).saveApplicationBatch(Mockito.any());
    }
    
    @Test
    void should_success_when_syncApplication_with_save() throws Exception {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setTenantId("11");
        mockUserUtil.when(UserUtil::getAuthorization).thenReturn("token");
        mockUserUtil.when(UserUtil::getUser).thenReturn(authenticationUserDto);
        TenantManageEntity tenant = new TenantManageEntity();
        tenant.setTenantCode("paas");
        Mockito.when(tenantManageDao.getObjectById(Mockito.anyString())).thenReturn(tenant);
        UacApplicationVo uacApplicationVo = new UacApplicationVo();
        uacApplicationVo.setAppCode("test");
        uacApplicationVo.setAppName("testName");
        ResponseEntity<String> responseEntity = ResponseEntity.ok()
                .body(JSONObject.toJSONString(Collections.singletonList(uacApplicationVo)));
        Mockito.when(
                restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
                        Mockito.eq(String.class), Mockito.anyString())).thenReturn(responseEntity);
        
        Mockito.when(applicationDao.queryList(Mockito.anyMap())).thenReturn(new ArrayList<>());
        applicationService.syncApplication();
        Mockito.verify(applicationDao, Mockito.times(0)).updateApplicationBatch(Mockito.any());
        Mockito.verify(applicationDao, Mockito.times(1)).saveApplicationBatch(Mockito.any());
    }
    
    @Test
    void should_success_when_syncApplication_with_update() throws Exception {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setTenantId("11");
        mockUserUtil.when(UserUtil::getAuthorization).thenReturn("token");
        mockUserUtil.when(UserUtil::getUser).thenReturn(authenticationUserDto);
        TenantManageEntity tenant = new TenantManageEntity();
        tenant.setTenantCode("paas");
        Mockito.when(tenantManageDao.getObjectById(Mockito.anyString())).thenReturn(tenant);
        UacApplicationVo uacApplicationVo = new UacApplicationVo();
        uacApplicationVo.setAppCode("test");
        uacApplicationVo.setAppName("testName");
        ResponseEntity<String> responseEntity = ResponseEntity.ok()
                .body(JSONObject.toJSONString(Collections.singletonList(uacApplicationVo)));
        Mockito.when(
                restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
                        Mockito.eq(String.class), Mockito.anyString())).thenReturn(responseEntity);
        Application application = new Application();
        application.setApplicationCode("test");
        application.setApplicationName("testName1");
        Mockito.when(baseDao.queryList(Mockito.anyMap())).thenReturn(Collections.singletonList(application));
        applicationService.syncApplication();
        Mockito.verify(applicationDao, Mockito.times(1)).updateApplicationBatch(Mockito.any());
        Mockito.verify(applicationDao, Mockito.times(0)).saveApplicationBatch(Mockito.any());
    }
    
    @Test
    void should_success_when_syncApplication_with_update_and_save() throws Exception {
        ReflectionTestUtils.setField(applicationService, "ucacAppUrl", "testurl");
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setTenantId("11");
        mockUserUtil.when(UserUtil::getAuthorization).thenReturn("token");
        mockUserUtil.when(UserUtil::getUser).thenReturn(authenticationUserDto);
        TenantManageEntity tenant = new TenantManageEntity();
        tenant.setTenantCode("paas");
        Mockito.when(tenantManageDao.getObjectById(Mockito.anyString())).thenReturn(tenant);
        UacApplicationVo uacApplicationVo1 = new UacApplicationVo();
        uacApplicationVo1.setAppCode("test1");
        uacApplicationVo1.setAppName("testName1");
        UacApplicationVo uacApplicationVo2 = new UacApplicationVo();
        uacApplicationVo2.setAppCode("test2");
        uacApplicationVo2.setAppName("testName2");
        List<UacApplicationVo> uacApplicationVos = new ArrayList<>();
        uacApplicationVos.add(uacApplicationVo1);
        uacApplicationVos.add(uacApplicationVo2);
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body(JSONObject.toJSONString(uacApplicationVos));
        Mockito.when(
                restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class),
                        Mockito.eq(String.class), Mockito.anyString())).thenReturn(responseEntity);
        Application application = new Application();
        application.setApplicationCode("test1");
        application.setApplicationName("testName11");
        Mockito.when(baseDao.queryList(Mockito.anyMap())).thenReturn(Collections.singletonList(application));
        applicationService.syncApplication();
        Mockito.verify(applicationDao, Mockito.times(1)).updateApplicationBatch(Mockito.any());
        Mockito.verify(applicationDao, Mockito.times(1)).saveApplicationBatch(Mockito.any());
    }
}