package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.CityDao;
import com.dap.paas.console.basic.entity.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityDaoImpl extends AbstractBaseDaoImpl<City,String> implements CityDao {


    @Override
    public List<City> findExist(List<City> cityList)  {
        List<City> result = null;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace().concat(".findExist"), cityList);
        }catch (Exception e){
        }
        return result;
    }

    @Override
    public Integer saveCityBatch(List<City> cityList) {
        Integer result = 0;
        try {
            result = this.getSqlSession().insert(getSqlNamespace() + ".saveCityBatch", cityList);
        } catch (Exception e) {
        }
        return result;
    }


}
