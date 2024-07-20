package com.base.sys.tenant.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.base.sys.api.entity.TenantManageEntity;


public interface TenantManageDao extends BaseDao<TenantManageEntity,String> {
    /**
     * 根据Code查询ID(仅在登录时使用，如需要相同功能清重写方法)
     * @param code
     * @return
     * @throws DaoException
     */

    String getIdByCodeOnLogin(String code) throws DaoException;
}
