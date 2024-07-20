package com.dap.paas.console.basic.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.Unitization;

public interface UnitizationDao extends BaseDao<Unitization, String> {
    public int selectCount(Unitization unitization) throws DaoException;
}
