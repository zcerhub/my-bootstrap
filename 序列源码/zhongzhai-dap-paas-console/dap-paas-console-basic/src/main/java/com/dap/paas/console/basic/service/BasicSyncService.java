package com.dap.paas.console.basic.service;

import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.dap.paas.console.basic.entity.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface BasicSyncService extends BaseService<BasicSync, String> {
    /**
     * 同步城市数据
     * @param city
     * @return
     */
    public Result syncCity(@RequestBody City city);

    Result updateCity(City city);

    /**
     * 同步组织数据
     * @param organizationList
     * @return
     */
    public Result syncOrganization(@RequestBody List<Organization> organizationList);

    /**
     * 同步数据中心数据
     * @param machineRoomList
     * @return
     */
    MachineRoom getDatacenter(String id);
    public Result syncDatacenter(@RequestBody List<MachineRoom> machineRoomList);
    Result updateDatacenter(MachineRoom machineRoom);
    Result deleteDatacenter(String id);

    /**
     * 同步机器数据
     * @param machineList
     * @return
     */
    public Result syncMachine(@RequestBody List<Machine> machineList);
    Result updateMachine(Machine machine);
    Result deleteMachine(String id);

    /**
     * 同步单元数据
     * @param unitizationList
     * @return
     */
    public Result syncUnitization(@RequestBody List<Unitization> unitizationList);
    Result updateUnitization(Unitization unitization);
    Result deleteUnitization(String id);

}
