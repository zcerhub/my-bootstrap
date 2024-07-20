package com.dap.paas.console.basic.service;

import com.base.api.exception.DaoException;
import com.dap.paas.console.basic.entity.Application;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.MachineRoom;

import java.util.List;

/**
 * 基础数据同步服务接口
 *
 * @author makejava
 * @since 2023-05-18 11:04:08
 */
public interface BasicInfoSyncService  {

    void syncMachineRoom(List<MachineRoom> list);

    void syncMachine(List<Machine> list);

    void syncCity(List<City> list) ;

    void syncApplication(List<Application> list);

}
