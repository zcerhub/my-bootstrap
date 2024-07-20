package com.dap.paas.console.basic.dao.impl;

import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.dap.paas.console.basic.dao.MachineRoomDao;
import com.dap.paas.console.basic.entity.MachineRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MachineRoomDaoImpl extends AbstractBaseDaoImpl<MachineRoom,String> implements MachineRoomDao {
    @Override
    public List<MachineRoom> findExist(List<MachineRoom> machineRoomList) {
        List<MachineRoom> result = null;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace().concat(".findExist"), machineRoomList);
        }catch (Exception e){
        }
        return result;
    }

    @Override
    public Integer saveMachineRoomBatch(List<MachineRoom> machineRoomList) {
        Integer result = 0;
        try {
            result = this.getSqlSession().insert(getSqlNamespace() + ".saveMachineRoomBatch", machineRoomList);
        } catch (Exception e) {
        }
        return result;
    }
}
