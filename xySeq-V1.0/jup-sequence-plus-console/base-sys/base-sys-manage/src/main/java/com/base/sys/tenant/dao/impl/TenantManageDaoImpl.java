package com.base.sys.tenant.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.TenantManageEntity;
import com.base.sys.tenant.dao.TenantManageDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TenantManageDaoImpl extends AbstractBaseDaoImpl<TenantManageEntity,String> implements TenantManageDao {
    @Override
    public String getIdByCodeOnLogin(String code) throws DaoException {
        String result;
        try {
            result = this.getSqlSession().selectOne(getSqlNamespace().concat(".getIdByCodeOnLogin"), code);
        }catch (Exception e){
            log.error("selectByIds执行异常",e);
            throw new DaoException("selectByIds执行异常"+e.getMessage());
        }
        return result;
    }
}
