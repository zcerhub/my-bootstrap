package com.dap.paas.console.basic.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.City;

import java.util.Map;

public interface ApplicationDao extends BaseDao<Application, String> {
    Application getObjectDataByMap(Map map) throws DaoException;

    int checkAccessKey(Map map) ;
}
