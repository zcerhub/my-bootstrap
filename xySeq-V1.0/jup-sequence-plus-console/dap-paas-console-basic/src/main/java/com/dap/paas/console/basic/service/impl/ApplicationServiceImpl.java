package com.dap.paas.console.basic.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.basic.dao.ApplicationDao;
import com.dap.paas.console.basic.dao.beijing.AppAppInfoDao;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.beijing.AppAppInfo;
import com.dap.paas.console.basic.service.ApplicationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApplicationServiceImpl extends AbstractBaseServiceImpl<Application,String> implements ApplicationService {
   @Autowired
   private ApplicationDao applicationDao;
   @Autowired
   private AppAppInfoDao appInfoDao;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer delObjectById(String s)  {
        Integer result=0;
        try {
            applicationDao.delObjectById(s);
            result = appInfoDao.delObjectById(s);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer saveObject(Application obj)  {
        Integer result=0;
        try {
            obj.setId(SnowflakeIdWorker.getID());
            AppAppInfo appInfo= new AppAppInfo();
            BeanUtils.copyProperties(obj,appInfo);
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            appInfo.setAppType("1");
            appInfo.setAppLanguage("java");
            appInfo.setAppFrame("springboot");
            appInfo.setUsable("1");
            appInfo.setShowStoreFlag("1");
            appInfo.setUpdDate(format);
            appInfo.setCreDate(format);
            appInfoDao.saveObject(appInfo);
            result = applicationDao.saveObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Integer updateObject(Application obj)  {
        Integer result=0;
        try{

            AppAppInfo appInfo= new AppAppInfo();
            BeanUtils.copyProperties(obj,appInfo);
            appInfo.setUpdDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            appInfoDao.updateObject(appInfo);
            result = applicationDao.updateObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
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
        return i>0?true:false;
    }


}
