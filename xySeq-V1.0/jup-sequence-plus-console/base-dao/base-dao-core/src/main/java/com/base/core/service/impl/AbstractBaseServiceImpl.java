package com.base.core.service.impl;


import com.base.api.entity.BaseEntity;
import com.base.api.exception.ServiceException;
import com.base.api.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

@Slf4j
public abstract class AbstractBaseServiceImpl<T extends BaseEntity, ID extends Serializable> extends BaseServiceImpl<T, ID> {

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
                log.error("queryListByIds方法调用异常",e);
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
                log.error("updateObject方法调用异常",e);

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
                log.error("updateStatus方法调用异常",e);

                throw new ServiceException("updateStatus方法调用异常"+e.getMessage());
            }
        }else{
            throw new ServiceException("updateStatus方法调用参数:obj为空");
        }
        return result;
    }


}
