package com.base.core.service.impl;


import com.base.api.entity.BaseEntity;
import com.base.api.exception.ServiceException;
import com.base.api.util.SnowflakeIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

public abstract class AbstractBaseServiceImpl<T extends BaseEntity, ID extends Serializable> extends BaseServiceImpl<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseServiceImpl.class);

    @Override
    public Integer saveObject(T  obj) throws ServiceException {
        Integer result=0;
        if(obj!=null) {
            try {
                if (obj.getId()==null || "".equals(obj.getId())) {
                    obj.setId(SnowflakeIdWorker.getID());
                }
                if (obj.getCreateDate()==null || "".equals(obj.getCreateDate())) {
                    obj.setCreateDate(new Date());
                }
                if (obj.getUpdateDate()==null || "".equals(obj.getUpdateDate())) {
                    obj.setUpdateDate(new Date());
                }
                result = getBaseDao().saveObject(obj);
            } catch (Exception e) {
                LOGGER.error("error", e);
                throw new ServiceException("saveObject方法调用异常"+e.getMessage());
            }
        }else{
            throw new ServiceException("saveObject方法调用参数:obj为空");
        }
        return result;
    }

    @Override
    public Integer updateObject(T  obj) throws ServiceException {
        Integer result=0;
        if(obj!=null) {
            try {
                obj.setUpdateDate(new Date());
                result = getBaseDao().updateObject(obj);
            } catch (Exception e) {
                LOGGER.error("error", e);
                throw new ServiceException("updateObject方法调用异常"+e.getMessage());
            }
        }else{
            throw new ServiceException("updateObject方法调用参数:obj为空");
        }
        return result;
    }

    @Override
    public Integer updateStatus(T  obj) throws ServiceException {
        Integer result=0;
        if(obj!=null) {
            try {
                result = getBaseDao().updateStatus(obj);
            } catch (Exception e) {
                LOGGER.error("error", e);
                throw new ServiceException("updateStatus方法调用异常"+e.getMessage());
            }
        }else{
            throw new ServiceException("updateStatus方法调用参数:obj为空");
        }
        return result;
    }


}
