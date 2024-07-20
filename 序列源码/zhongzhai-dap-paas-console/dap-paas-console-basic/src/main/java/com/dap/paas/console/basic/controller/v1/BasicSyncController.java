package com.dap.paas.console.basic.controller.v1;


import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.basic.entity.*;
import com.dap.paas.console.basic.service.*;
import com.dap.paas.console.common.util.PinyinUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 基础信息同步接口
 */
@Api(tags = "基础信息-数据中心相关信息同步接口")
@RestController
@RequestMapping("/v1/basic/sync")
public class BasicSyncController {

    @Autowired
    private BasicSyncService basicSyncService;


    @ApiOperation("同步数据中心数据.必填id,machineRoomCode数据中心编码,machineRoomName数据中心名称,cityId数据中心地址,tenantId租户ID")
    @PostMapping("/datacenter")
    public Result syncDatacenter(@RequestBody List<MachineRoom> machineRoomList) {
        for (MachineRoom room : machineRoomList) {
            City city = new City();
            city.setCityName(room.getCityId());
            city.setCityCode(PinyinUtil.getPinyinInitials(room.getCityId()));
            city.setTenantId(room.getTenantId());
            Result result = basicSyncService.syncCity(city);
            if(result.getCode().equals(ResultEnum.FAILED.getCode())) {
                return result;
            }
            room.setCityId(city.getId());
        }
        return basicSyncService.syncDatacenter(machineRoomList);
    }

    @ApiOperation("升级数据中心数据.")
    @PutMapping("/datacenter")
    public Result updateDatacenter(@RequestBody MachineRoom room) {
        City city = new City();
        city.setCityName(room.getCityId());
        city.setCityCode(PinyinUtil.getPinyinInitials(room.getCityId()));
        city.setTenantId(room.getTenantId());
        MachineRoom machineRoom = basicSyncService.getDatacenter(room.getId());
        if (machineRoom != null) {
            city.setId(machineRoom.getCityId());
            Result result = basicSyncService.updateCity(city);
            if(result.getCode().equals(ResultEnum.FAILED.getCode())) {
                return result;
            }
        }
        return basicSyncService.updateDatacenter(room);
    }

    @ApiOperation("删除数据中心数据.")
    @DeleteMapping("/datacenter")
    public Result deleteDatacenter(String id) {
        return basicSyncService.deleteDatacenter(id);
    }

    @ApiOperation("同步机器数据.必填id,hostIp-IP地址,hostPort-SSH端口号,hostSshAccount系统用户名,hostSshPassword系统密码,machineRoomId所属数据中心,tenantId租户ID")
    @PostMapping("/machine")
    public Result syncMachine(@RequestBody List<Machine> machineList) {
            return basicSyncService.syncMachine(machineList);
    }

    @ApiOperation("升级机器数据.")
    @PutMapping("/machine")
    public Result updateMachine(@RequestBody Machine machine) {
        return basicSyncService.updateMachine(machine);
    }

    @ApiOperation("删除机器数据.")
    @DeleteMapping("/machine")
    public Result deleteMachine(String id) {
        return basicSyncService.deleteMachine(id);
    }

    @ApiOperation("同步单元数据.必填id,unitCode单元编号,unitType-1-逻辑单元2-全局单元3-全局单元只读副本,bakNo备份编号,machineRoomId所属数据中心,tenantId租户ID")
    @PostMapping("/unitization")
    public Result syncUnitization(@RequestBody List<Unitization> unitizationList) {
            return basicSyncService.syncUnitization(unitizationList);
    }

    @ApiOperation("升级单元数据.")
    @PutMapping("/unitization")
    public Result updateUnitization(@RequestBody Unitization unitization) {
        return basicSyncService.updateUnitization(unitization);
    }

    @ApiOperation("删除单元数据.")
    @DeleteMapping("/unitization")
    public Result deleteUnitization(String id) {
        return basicSyncService.deleteUnitization(id);
    }

}
