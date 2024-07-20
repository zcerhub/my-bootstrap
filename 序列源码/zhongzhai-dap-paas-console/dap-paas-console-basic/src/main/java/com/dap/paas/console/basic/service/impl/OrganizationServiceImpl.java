package com.dap.paas.console.basic.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.paas.console.basic.dao.OrganizationDao;
import com.dap.paas.console.basic.dao.beijing.AppSysInfoDao;
import com.dap.paas.console.basic.entity.Organization;
import com.dap.paas.console.basic.entity.beijing.AppSysInfo;
import com.dap.paas.console.basic.service.OrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OrganizationServiceImpl extends AbstractBaseServiceImpl<Organization,String> implements OrganizationService {
   @Autowired
   private OrganizationDao organizationDao;
   @Autowired
   private AppSysInfoDao appSysInfoDao;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Integer delObjectById(String s)  {
        Integer result=0;
        try{
            result = organizationDao.delObjectById(s);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Integer saveObject(Organization obj)  {
        Integer result=0;
        try{
            obj.setId(SnowflakeIdWorker.getID());
            result = organizationDao.saveObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public Integer updateObject(Organization obj)  {
        Integer result=0;
        try{
            result = organizationDao.updateObject(obj);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return result;

    }
}
