package com.dap.paas.console.basic.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.MachineDao;
import com.dap.paas.console.basic.entity.Machine;

@Repository
public class MachineDaoImpl extends AbstractBaseDaoImpl<Machine,String> implements MachineDao {
    @Override
    public Integer updateByUnitId(String id) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().update(getSqlNamespace()+"."+ "updateByUnitId", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.UpdateObject.getName()+":"+"updateByUnitId"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }


    @Override
    public List<Machine> queryNotUse(Machine machine) throws DaoException {
        List<Machine> result=null;
        try {
            result= this.getSqlSession().selectList(getSqlNamespace()+"."+ "queryNotUse",machine);
        }catch (Exception e){
            String msg= "queryNotUse:"+e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public List<Machine> findExist(List<Machine> machineList) {
        List<Machine> result = null;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace().concat(".findExist"), machineList);
        }catch (Exception e){
        }
        return result;
    }

    @Override
    public Integer saveMachineBatch(List<Machine> machineList) {
        Integer result = 0;
        try {
            result = this.getSqlSession().insert(getSqlNamespace() + ".saveMachineBatch", machineList);
        } catch (Exception e) {
        }
        return result;
    }
}
