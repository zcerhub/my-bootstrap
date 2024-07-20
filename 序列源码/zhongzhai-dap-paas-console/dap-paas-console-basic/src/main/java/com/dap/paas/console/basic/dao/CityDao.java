package com.dap.paas.console.basic.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.City;

import java.util.List;

public interface CityDao extends BaseDao<City, String> {

    List<City> findExist(List<City> cityList) ;

    Integer saveCityBatch(List<City> cityList);

}
