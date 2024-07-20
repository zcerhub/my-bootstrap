package com.dap.paas.console.basic.dao;

import com.base.api.exception.DaoException;
import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.Machine;

import java.util.List;

public interface MachineDao extends BaseDao<Machine, String> {
    Integer updateByUnitId(String id) throws DaoException;

    List<Machine> queryNotUse(Machine machine) throws DaoException;

    List<Machine> findExist(List<Machine> cityList) ;

    Integer saveMachineBatch(List<Machine> cityList);
}
