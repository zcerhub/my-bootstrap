package com.dap.paas.console.basic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.tenant.dao.TenantManageDao;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.basic.config.UcacConfig;
import com.dap.paas.console.basic.dao.ApplicationDao;
import com.dap.paas.console.basic.dao.beijing.AppAppInfoDao;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.service.ApplicationService;
import com.dap.paas.console.basic.utils.RandomUtil;
import com.dap.paas.console.basic.vo.UacApplicationVo;
import com.dap.dmc.client.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationServiceImpl extends AbstractBaseServiceImpl<Application, String> implements ApplicationService {
    
    @Autowired
    private ApplicationDao applicationDao;
    
    @Autowired
    private AppAppInfoDao appInfoDao;
    
    @Autowired
    private TenantManageDao tenantManageDao;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UcacConfig ucacConfig;
    
    private final String ucacAppUrl = "/api/open/data/app/{tenantCode}";
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delObjectById(String s) {
        Integer result = 0;
        try {
            applicationDao.delObjectById(s);
            result = appInfoDao.delObjectById(s);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveObject(Application obj) {
        Integer result = 0;
        try {
            obj.setId(SnowflakeIdWorker.getID());
            result = applicationDao.saveObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateObject(Application obj) {
        Integer result = 0;
        try {
            result = applicationDao.updateObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncApplication() throws Exception {
        if (StringUtils.isEmpty(ucacConfig.getDmcUrl())) {
            throw new ServiceException("DmcUrl is empty");
        }
        String authorization = UserUtil.getAuthorization();
        if (StringUtils.isEmpty(authorization)) {
            throw new ServiceException("authorization is empty");
        }
        TenantManageEntity tenant = tenantManageDao.getObjectById(UserUtil.getUser().getTenantId());
        if (tenant == null || StringUtils.isEmpty(tenant.getTenantCode())) {
            throw new ServiceException("tenant is empty");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorization);
        ResponseEntity<String> responseEntity = restTemplate.exchange(ucacConfig.getDmcUrl() + ucacAppUrl, HttpMethod.GET,
                new HttpEntity<String>(headers), String.class, tenant.getTenantCode());
        
        if (!responseEntity.getStatusCode().is2xxSuccessful() || StringUtils.isEmpty(responseEntity.getBody())) {
            throw new ServiceException("get app from ucac failed:" + responseEntity.getStatusCode());
        }
        List<UacApplicationVo> applications = JSONObject.parseArray(responseEntity.getBody(), UacApplicationVo.class);
        if (CollectionUtils.isEmpty(applications)) {
            return;
        }
        List<Application> applicationList = queryList(new HashMap());
        Map<String, Application> applicationMap = new HashMap<>();
        for (Application application : applicationList) {
            applicationMap.put(application.getApplicationCode(), application);
        }
        List<Application> insertApps = new ArrayList<>();
        List<Application> updateApps = new ArrayList<>();
        for (UacApplicationVo uacApplicationVo : applications) {
            if (applicationMap.containsKey(uacApplicationVo.getAppCode())) {
                Application application = applicationMap.get(uacApplicationVo.getAppCode());
                if (!StringUtils.equals(application.getApplicationName(), uacApplicationVo.getAppName())) {
                    application.setApplicationName(uacApplicationVo.getAppName());
                    application.setApplicationCode(uacApplicationVo.getAppCode());
                    updateApps.add(application);
                }
                continue;
            }
            Application application = new Application();
            application.setId(SnowflakeIdWorker.getID());
            application.setApplicationCode(uacApplicationVo.getAppCode());
            application.setApplicationName(uacApplicationVo.getAppName());
            application.setDescription("");
            application.setAccessKey(RandomUtil.generateSecureAccessKey(10));
            application.setAccessKeyStatus("1");
            insertApps.add(application);
        }
        if (!CollectionUtils.isEmpty(insertApps)) {
            applicationDao.saveApplicationBatch(insertApps);
        }
        if (!CollectionUtils.isEmpty(updateApps)) {
            applicationDao.updateApplicationBatch(updateApps);
        }
    }
    
    @Override
    public Application getObjectDataByMap(Map map) {
        Application result = null;
        try {
            result = applicationDao.getObjectDataByMap(map);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    public boolean checkAccessKey(Map map) {
        int i = applicationDao.checkAccessKey(map);
        return i > 0 ? true : false;
    }
    
    @Override
    public List<Application> queryListByIds(List<String> ids) {
        return applicationDao.queryListByIdList(ids);
    }
}
