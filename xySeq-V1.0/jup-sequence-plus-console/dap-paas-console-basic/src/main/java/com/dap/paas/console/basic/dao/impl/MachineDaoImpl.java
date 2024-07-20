package com.dap.paas.console.basic.dao.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.MachineDao;
import com.dap.paas.console.basic.entity.Machine;

@Slf4j
@Repository
public class MachineDaoImpl extends AbstractBaseDaoImpl<Machine,String> implements MachineDao {
    @Override
    public Integer updateByUnitId(String id) throws DaoException {
        Integer result=0;
        try {
            result= this.getSqlSession().update(getSqlNamespace()+"."+ "updateByUnitId", id);
        }catch (Exception e){
            String msg= SqlSessionMapperLabel.UpdateObject.getName()+":"+"updateByUnitId"+e.getMessage();
            log.error(msg,e);
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
            log.error(msg,e);

            throw new DaoException(msg);
        }
        return result;
    }
}
