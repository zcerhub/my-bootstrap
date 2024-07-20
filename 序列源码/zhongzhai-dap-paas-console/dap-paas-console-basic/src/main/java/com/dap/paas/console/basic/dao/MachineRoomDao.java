package com.dap.paas.console.basic.dao;

import com.base.core.dao.BaseDao;
import com.dap.paas.console.basic.entity.MachineRoom;

import java.util.List;

public interface MachineRoomDao extends BaseDao<MachineRoom, String> {

    List<MachineRoom> findExist(List<MachineRoom> cityList) ;

    Integer saveMachineRoomBatch(List<MachineRoom> cityList);
}
