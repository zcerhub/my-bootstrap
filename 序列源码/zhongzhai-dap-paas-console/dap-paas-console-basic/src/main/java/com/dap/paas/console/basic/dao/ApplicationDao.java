package com.dap.paas.console.basic.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.Application;

import java.util.List;
import java.util.Map;

public interface ApplicationDao extends BaseDao<Application, String> {
    Application getObjectDataByMap(Map map) throws DaoException;

    int checkAccessKey(Map map) ;

    List<Application> findExist(List<Application> cityList) ;

    Integer saveApplicationBatch(List<Application> appList);

    List<Application> queryListByIdList(List<String> ids) ;
    
    Integer updateApplicationBatch(List<Application> appList);
}
