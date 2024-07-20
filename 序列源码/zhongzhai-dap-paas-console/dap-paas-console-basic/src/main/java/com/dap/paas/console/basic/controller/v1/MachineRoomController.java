package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.utils.UserUtil;
import com.dap.paas.console.basic.entity.City;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.service.MachineRoomService;
import com.dap.paas.console.basic.service.MachineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机房信息管理
 *
 */
@Api(tags = "基础信息-机房信息管理")
@RestController
@RequestMapping("/v1/basic/room")
public class MachineRoomController {
    private static final Logger log= LoggerFactory.getLogger(MachineRoomController.class);
    @Autowired
    private MachineRoomService machineRoomService;
    @Autowired
    private MachineService machineService;

    @PostMapping("/insert")
    public Result insert(@RequestBody MachineRoom machineRoom) {
        try {
            List<MachineRoom> machineRoomList = machineRoomService.queryList(new HashMap());
            for(MachineRoom mr : machineRoomList){
                if (mr.getMachineRoomCode().equals(machineRoom.getMachineRoomCode()) || mr.getMachineRoomName().equals(machineRoom.getMachineRoomName())) {
                    log.info("机房信息重复");
                    return ResultUtils.error(ResultEnum.FAILED.getCode(),"已存在");
                }
            }
            machineRoomService.saveObject(machineRoom);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("新增机房失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable("id") String id) {
        try {
            MachineRoom machineRoom = machineRoomService.getObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS, machineRoom);
        } catch (ServiceException e) {
            log.error("查询机房失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @ApiOperation("查询所有机房")
    @RequestMapping(value = "/queryList", method = {RequestMethod.GET, RequestMethod.POST})
    public Result queryList(@RequestParam(value = "cityId",required = false)String cityId) {
        Map param = new HashMap<>();
        param.put("cityId",cityId);
        try {
            List<MachineRoom> machineRooms = machineRoomService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, machineRooms);
        } catch (ServiceException e) {
            log.error("查询组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @ApiOperation("机房")
    @PostMapping("/query/list")
    public Result queryList(@RequestBody MachineRoom machineRoom) {
        try {
            List<MachineRoom> machineRooms = machineRoomService.queryList(machineRoom);
            return ResultUtils.success(ResultEnum.SUCCESS, machineRooms);
        } catch (ServiceException e) {
            log.error("查询组织失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<MachineRoom> param) {
        try {
            Page machineRooms = machineRoomService.queryPage(param.getPageNo(),param.getPageSize(), param.getRequestObject());
            return ResultUtils.success(ResultEnum.SUCCESS, machineRooms);
        } catch (ServiceException e) {
            log.error("查询分页机房失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody MachineRoom machineRoom) {
        try {
            List<MachineRoom> machineRoomList = machineRoomService.queryList(new HashMap());
            for (MachineRoom mr : machineRoomList) {
                if ((!mr.getId().equals(machineRoom.getId())) && mr.getMachineRoomCode().equals(machineRoom.getMachineRoomCode()) && mr.getMachineRoomName().equals(machineRoom.getMachineRoomName())) {
                    return ResultUtils.error(ResultEnum.FAILED.getCode(), "已存在");
                }
            }
            machineRoomService.updateObject(machineRoom);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("更新机房失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            Map map = new HashMap();
            map.put("machineRoomId",id);
            List<Machine> machines = machineService.queryList(map);
            if(!machines.isEmpty()){
                return ResultUtils.error(ResultEnum.FAILED.getCode(),"该机房有机器，不能删除");
            }
            machineRoomService.delObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除机房失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestParam("idList") List<String> idList) {
        try {
            machineRoomService.delObjectByIds(idList);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除机房失败",e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    @PostMapping("/syncMachineRoom")
    public Result syncMachineRoom() {
        try {

            machineRoomService.syncMachineRoom();
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (Exception e) {
            log.error("同步应用失败", e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
