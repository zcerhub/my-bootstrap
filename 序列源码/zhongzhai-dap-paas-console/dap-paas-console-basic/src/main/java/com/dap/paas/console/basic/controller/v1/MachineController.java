package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.dao.MachineDao;
import com.dap.paas.console.basic.entity.Machine;
import com.dap.paas.console.basic.entity.MachineRoom;
import com.dap.paas.console.basic.entity.Unitization;
import com.dap.paas.console.basic.service.MachineRoomService;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.basic.service.UnitizationService;
import com.dap.paas.console.basic.vo.MachineVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机器信息管理
 */
@Api(tags = "基础信息-机器信息管理")
@RestController
@RequestMapping("/v1/basic/machine")
public class MachineController {
    private static final Logger log = LoggerFactory.getLogger(MachineController.class);
    @Autowired
    private MachineService machineService;
    @Autowired
    private MachineRoomService machineRoomService;
    @Autowired
    private UnitizationService unitizationService;

    @Autowired
    private MachineDao machineDao;
    @PostMapping("/insert")
    public Result insert(@RequestBody Machine machine) {
        try {
             checkPo(machine);
            if (StringUtils.isEmpty(machine.getOsVersion()) || StringUtils.isEmpty(machine.getCoreArch())) {
                MachineVo machineVo = machineService.getMachineInfo(machine);
                machine.setOsVersion(machineVo.getOsVersion());
                machine.setCoreArch(machineVo.getCoreArch());
            }
            Map param = new HashMap();
            param.put("hostIp", machine.getHostIp());
            List<Machine> machineList = machineService.queryList(param);
            for(Machine m:machineList){
                if(machine.getHostSshAccount().equals(m.getHostSshAccount())){
                    log.error("用户名重复");
                    return ResultUtils.error(ResultEnum.FAILED.getCode(), "已存在");
                }
            }
            machineService.saveObject(machine);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("新增机器失败", e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED.getCode(), e.getMessage());
        }
    }
    @ApiOperation("查询未被单元化使用的机器")
    @GetMapping("/queryNotUse")
    public Result queryNotUse(@RequestBody Machine machine) {
        try {
            List<Machine> machineList = machineDao.queryNotUse(machine);
            return ResultUtils.success(ResultEnum.SUCCESS, machineList);
        } catch (DaoException e) {
            log.error("查询机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @GetMapping("/query/{id}")
    public Result query(@PathVariable("id") String id) {
        try {
            Machine machine = machineService.getObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS, machine);
        } catch (ServiceException e) {
            log.error("查询机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    @ApiOperation("通过ids查询所有的机器")
    @PostMapping("/queryByIds")
    public Result queryByIds(@RequestBody PageInDto<Machine> param) {
        try {
            Machine machine1  = param.getRequestObject();
            List<MachineVo> machineList = new ArrayList<>();
            if(!machine1.getId().isEmpty()){
                String[] str =machine1.getId().split(";");
                for (String id : str) {
                    Machine machine = machineService.getObjectById(id);
                    MachineVo machineVo = machineService.getMachineInfo(machine);
                    machineList.add(machineVo);
                }
            }
            return ResultUtils.success(ResultEnum.SUCCESS, machineList);
        } catch (ServiceException e) {
            log.error("查询机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @ApiOperation("机器信息查询")
    @GetMapping("/queryDetail/{id}")
    public Result queryDetail(@PathVariable("id") String id) {
        try {
            Machine machine = machineService.getObjectById(id);
            MachineVo machineVo = machineService.getMachineInfo(machine);
            return ResultUtils.success(ResultEnum.SUCCESS, machineVo);
        } catch (ServiceException e) {
            log.error("查询机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @ApiOperation("根据机房id查询机器列表")
    @GetMapping("/queryListByRoomId/{machineRoomId}")
    public Result queryListByRoomId(@PathVariable("machineRoomId") String machineRoomId) {
        Map param = new HashMap<>();
        try {
            param.put("machineRoomId",machineRoomId);
            List<Machine> machineList = machineService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, machineList);
        } catch (ServiceException e) {
            log.error("查询的机器列表失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    @ApiOperation("查询所有的机器")
    @PostMapping("/queryList")
    public Result queryList() {
        Map param = new HashMap<>();
        try {
            List<Machine> machineList = machineService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, machineList);
        } catch (ServiceException e) {
            log.error("查询所有的机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    @PostMapping("/queryDetailPage")
    public Result queryDetailPage(@RequestBody PageInDto<Machine> param) {
        try {
            Page machines = machineService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
            List<Machine> machineList = machines.getData();
            for(Machine machine:machineList){
                MachineRoom machineRoom=machineRoomService.getObjectById(machine.getMachineRoomId());
               if( machineRoom != null && !"".equals(machineRoom) ){
                   machine.setComputerRoom(StringUtils.isEmpty(machineRoom.getMachineRoomName())?"":machineRoom.getMachineRoomName());
               }else{
                   machine.setComputerRoom("");
               }

            }
            machines.setData(machineList);
            return ResultUtils.success(ResultEnum.SUCCESS, machines);
        } catch (ServiceException e) {
            log.error("查询机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody Machine machine) {
        try {
            machineService.updateObject(machine);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("更新机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") String id) {
        try {
            Machine machine = machineService.getObjectById(id);
            if (machine.getUnitId()!=null){
                Unitization unitization = unitizationService.getObjectById(machine.getUnitId());
                return ResultUtils.error("这台机器正在被单元 "+unitization.getUnitName()+" 使用");
            }
            machineService.delObjectById(id);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (Exception e) {
            log.error("删除机器失败", e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @DeleteMapping("/deleteList")
    public Result deleteList(@RequestParam("idList") List<String> idList) {
        try {
            machineService.delObjectByIds(idList);
            return ResultUtils.success(ResultEnum.SUCCESS);
        } catch (ServiceException e) {
            log.error("删除机器失败", e.getMsg());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    /**
     * 检查连接
     */
    @ApiOperation("ssh 检查连接")
    @PostMapping("/checkSsh")
    public Result checkSsh(@RequestBody Machine machine) {
        try {
            if (ObjectUtils.isEmpty(machine)) {
                return ResultUtils.error(ResultEnum.FAILED);
            }
            if (StringUtils.isEmpty(machine.getHostIp()) || StringUtils.isEmpty(machine.getHostSshAccount())) {
                return ResultUtils.error(ResultEnum.FAILED);
            }
            Boolean b = machineService.checkSsh(machine);
            if (b) {
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
        } catch (Exception e) {
            log.error("连接失败", e.getMessage());
            return ResultUtils.error(500, e.getMessage());
        }
        return ResultUtils.error(500, "连接失败");

    }

    /**
     * 检查参数是否正确
     */
    private void checkPo(Machine machine) {
        if (ObjectUtils.isEmpty(machine)) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "null param");
        }

        if (StringUtils.isEmpty(machine.getMachineRoomId())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "机房为空");
        }

        if (StringUtils.isEmpty(machine.getHostIp())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "主机IP为空");
        }

        if (StringUtils.isEmpty(machine.getHostPort())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "主机端口为空");
        }

        if (StringUtils.isEmpty(machine.getHostSshAccount())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "账号为空");
        }

        if (StringUtils.isEmpty(machine.getHostSshPassword())) {
            throw new ServiceException(ResultEnum.FAILED.getMsg(), "密码为空");
        }
    }
}
