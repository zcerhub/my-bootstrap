package com.base.core.service.impl;


import com.base.api.dto.Page;
import com.base.api.entity.Base;
import com.base.api.exception.ServiceException;
import com.base.core.dao.BaseDao;
import com.base.core.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class BaseServiceImpl<T extends Base, ID extends Serializable> implements BaseService<T, ID> {
    @Autowired(required = false)
    private BaseDao<T, ID> baseDao;

    @Override
    public List<T> queryList(Map map) throws ServiceException {
        List<T> result = null;
        try {
            result = baseDao.queryList(map);
        } catch (Exception e) {
            log.error("queryList方法调用异常",e);
            throw new ServiceException("queryList方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<T> queryList(T po) throws ServiceException {
        List<T> result;
        try {
            result = baseDao.queryList(po);
        } catch (Exception e) {
            log.error("queryList方法调用异常",e);
            throw new ServiceException("queryList方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public Page queryPage(int pageNo, int pageSize, Map map) throws ServiceException {
        Page result = null;
        try {
            result = baseDao.queryPage(pageNo, pageSize, map);
        } catch (Exception e) {
            log.error("queryPage方法调用异常",e);
            throw new ServiceException("queryPage方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public Page queryPage(int pageNo, int pageSize, T po) throws ServiceException {
        Page result = null;
        try {
            result = baseDao.queryPage(pageNo, pageSize, po);
        } catch (Exception e) {
            log.error("queryPage方法调用异常",e);
            throw new ServiceException("queryPage方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public T getObjectByMap(Map map) throws ServiceException {
        T result = null;
        try {
            result = (T) baseDao.getObjectByMap(map);
        } catch (Exception e) {
            log.error("getObjectByMap方法调用异常",e);
            throw new ServiceException("getObjectByMap方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public Integer delObjectByIds(List<ID> ids) throws ServiceException {
        Integer result;
        if (ids != null && !"".equals(ids)) {
            try {
                result = baseDao.delObjectByIds(ids);
            } catch (Exception e) {
                log.error("getObjectById方法调用异常",e);
                throw new ServiceException("getObjectById方法调用异常" + e.getMessage());
            }
        } else {
            throw new ServiceException("getObjectById方法调用参数:id为空");
        }
        return result;
    }

    @Override
    public T getObjectById(ID id) throws ServiceException {
        T result = null;
        if (id != null && !"".equals(id)) {
            try {
                result = (T) baseDao.getObjectById(id);
            } catch (Exception e) {
                log.error("getObjectById方法调用异常",e);
                throw new ServiceException("getObjectById方法调用异常" + e.getMessage());
            }
        } else {
            throw new ServiceException("getObjectById方法调用参数:id为空");
        }
        return result;
    }

    @Override
    public Integer saveObject(T obj) throws ServiceException {
        Integer result = 0;
        if (obj != null) {
            try {
                result = getBaseDao().saveObject(obj);
            } catch (Exception e) {
                log.error("saveObject方法调用异常",e);
                throw new ServiceException("saveObject方法调用异常" + e.getMessage());
            }
        } else {
            throw new ServiceException("saveObject方法调用参数:obj为空");
        }
        return result;
    }

    @Override
    public Integer updateObject(T obj) throws ServiceException {
        Integer result = 0;
        if (obj != null) {
            try {
                result = getBaseDao().updateObject(obj);
            } catch (Exception e) {
                log.error("updateObject方法调用异常",e);
                throw new ServiceException("updateObject方法调用异常" + e.getMessage());
            }
        } else {
            throw new ServiceException("updateObject方法调用参数:obj为空");
        }
        return result;
    }

    @Override
    public Integer delObjectById(ID id) throws ServiceException {
        Integer result = 0;
        if (id != null && !"".equals(id)) {
            try {
                result = baseDao.delObjectById(id);
            } catch (Exception e) {
                log.error("delObjectById方法调用异常",e);
                throw new ServiceException("delObjectById方法调用异常" + e.getMessage());
            }
        } else {
            throw new ServiceException("delObjectById方法调用参数:id为空");
        }
        return result;
    }

    @Override
    public Integer selectTotal(Map<String, Object> map) throws ServiceException {
        Integer result = 0;
        try {
            result = baseDao.selectTotal(map);
        } catch (Exception e) {
            log.error("selectTotal方法调用异常",e);
            throw new ServiceException("selectTotal方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public Integer deleteAttributeData(Map<String, String> map) throws ServiceException {
        Integer result = 0;
        try {
            result = baseDao.deleteAttributeData(map);
        } catch (Exception e) {
            log.error("selectTotal方法调用异常",e);
            throw new ServiceException("selectTotal方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<String> getIds(Map map) throws ServiceException {
        List<String> list = null;
        try {
            list = getBaseDao().getIds(map);
        } catch (Exception e) {
            log.error("getIds方法调用异常",e);
            throw new ServiceException("getIds方法调用异常" + e.getMessage());
        }
        return list;
    }

    public BaseDao<T, ID> getBaseDao() {
        return baseDao;
    }

    @Override
    public List<T> queryListByMap(Map map) throws ServiceException {
        List<T> result = null;
        try {
            result = baseDao.queryListByMap(map);
        } catch (Exception e) {
            log.error("queryList方法调用异常",e);
            throw new ServiceException("queryList方法调用异常" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<T> queryListByIds(Set<ID> ids) throws ServiceException {
        try {
            return baseDao.queryListByIds(ids);
        } catch (Exception e) {
            log.error("queryListByIds方法调用异常",e);
            throw new ServiceException("queryListByIds方法调用异常" + e.getMessage());
        }
    }

}
