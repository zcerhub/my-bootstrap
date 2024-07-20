package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.CityDao;
import com.dap.paas.console.basic.entity.City;
import org.springframework.stereotype.Repository;

@Repository
public class CityDaoImpl extends AbstractBaseDaoImpl<City,String> implements CityDao {
}
