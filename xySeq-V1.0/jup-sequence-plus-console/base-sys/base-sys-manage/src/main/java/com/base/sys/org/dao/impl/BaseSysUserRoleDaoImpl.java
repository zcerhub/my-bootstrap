package com.base.sys.org.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseSysUserRole;
import com.base.sys.org.dao.BaseSysUserRoleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: scalor
 * @Date: 2021/1/15:17:52
 * @Descricption:
 */
@Slf4j
@Repository
public class BaseSysUserRoleDaoImpl extends AbstractBaseDaoImpl<BaseSysUserRole,String> implements BaseSysUserRoleDao {
    @Override
    public List<String> getRoleIdByUser(String userId) throws DaoException {
        List<String> esList=null;
        try {
            esList=this.getSqlSession().selectList(getSqlNamespace().concat(".getRoleIdByUser"),userId);
        }catch (Exception e){
            log.error("delObjectByMenu执行异常:",e);
            throw new DaoException("delObjectByMenu执行异常",e.getMessage());
        }
        return esList;
    }
}
