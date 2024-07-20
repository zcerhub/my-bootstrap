package com.base.sys.org.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseSysRole;
import com.base.sys.org.dao.BaseSysRoleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/15 17:59
 */
@Slf4j
@Repository
public class BaseSysRoleDaoImpl extends AbstractBaseDaoImpl<BaseSysRole,String> implements BaseSysRoleDao {


    @Override
    public List<BaseSysRole> queryListByAttribute(BaseSysRole po) throws DaoException {
        List<BaseSysRole> result;
        try {
            result= getSqlSession().selectList(getSqlNamespace().concat(".queryListByAttribute"), po);
        }catch (Exception e){
            log.error("queryListByAttribute执行异常",e);
            throw new DaoException("queryListByAttribute执行异常," + e.getMessage());
        }
        return result;
    }
}
