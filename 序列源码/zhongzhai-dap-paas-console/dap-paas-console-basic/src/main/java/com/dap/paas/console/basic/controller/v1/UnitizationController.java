package com.dap.paas.console.basic.controller.v1;

import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.basic.dao.MachineDao;
import com.dap.paas.console.basic.dao.UnitizationDao;
import com.dap.paas.console.basic.entity.*;
import com.dap.paas.console.basic.service.MachineRoomService;
import com.dap.paas.console.basic.service.MachineService;
import com.dap.paas.console.basic.service.UnitizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单元化管理
 */

@Api(tags = "基础信息-单元化管理")
@RestController
@RequestMapping("/v1/basic/unit")
public class UnitizationController {
    private static final Logger log = LoggerFactory.getLogger(UnitizationController.class);

    @Autowired
    UnitizationService unitizationService;
    @Autowired
    MachineRoomService machineRoomService;
    @Autowired
    MachineService machineService;

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private UnitizationDao unitizationDao;

    /**
     *  查询所有的单元信息
     */
    @ApiOperation("查询所有的单元信息")
    @PostMapping("/queryPage")
    public Result queryPage(@RequestBody PageInDto<Unitization> param) {
        try {
            Page page = unitizationService.queryPage(param.getPageNo(), param.getPageSize(), param.getRequestObject());
            List<Unitization> unitizationList = page.getData();
            if (CollectionUtils.isEmpty(unitizationList)){
                return ResultUtils.success(ResultEnum.SUCCESS, page);
            }
            List<Unitization> result  = new ArrayList<>();
            for (Unitization unit : unitizationList){
               MachineRoom room = machineRoomService.getObjectById(unit.getMachineRoomId());
               unit.setMachineRoomName(room.getMachineRoomName());
               unit.setMachineRoomCode(room.getMachineRoomCode());

               Machine machine = new Machine();
               machine.setUnitId(unit.getId());
               List<Machine> machineList = machineService.queryList(machine);
               unit.setMachineList(machineList);
               result.add(unit);
            }
            page.setData(result);
            return ResultUtils.success(ResultEnum.SUCCESS, page);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }

    /**
     *  查询所有的单元信息
     */
    @ApiOperation("查询单元列表")
    @PostMapping("/queryList")
    public Result queryList(@RequestBody Unitization param) {
        try {
            List<Unitization> page = unitizationService.queryList(param);
            return ResultUtils.success(ResultEnum.SUCCESS, page);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }
    /**
     *  新增单元信息
     */
    @ApiOperation("新增单元信息")
    @PostMapping("/saveObject")
    public Result saveObject(@RequestBody Unitization unitization) {
        try {
            //对单元化编码进行唯一校验
            HashMap<String, Object> map = new HashMap<>();
            map.put("unitCode", unitization.getUnitCode());
            Unitization objectByMap = unitizationService.getObjectByMap(map);
            if (objectByMap != null) return ResultUtils.error(ResultEnum.CODE_EXIST);
            unitizationService.saveObject(unitization);
            return ResultUtils.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }

    /**
     *  编辑单元信息
     */
    @ApiOperation("编辑单元信息")
    @PostMapping("/updateObject")
    public Result updateObject(@RequestBody Unitization unitization) {
        try {
            Integer integer = unitizationService.updateObject(unitization);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }

    /**
     *  删除单元信息
     */
    @ApiOperation("删除单元信息")
    @DeleteMapping("/delete/{id}")
    public Result delUnitById(@PathVariable("id") String id) {
        Integer unitResult = null;
        unitizationService.delObjectById(id);
        try {
            unitResult = machineService.updateByUnitId(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if(unitResult>0){
            return ResultUtils.success(ResultEnum.SUCCESS);
        }
        return ResultUtils.success(ResultEnum.FAILED);
    }

    /**
     * 机器管理
     */
    @ApiOperation("机器管理")
    @PostMapping("/selectUnit")
    public Result selectUnit(@RequestBody Unitization unitization) {
        try {
            Map<String,List<Machine>> map = new HashMap();
            Map unitMap = new HashMap();
            String machineRoomId = unitization.getMachineRoomId();
            Machine machine  = new Machine();
            machine.setMachineRoomId(machineRoomId);
            String unitId = unitization.getId();
            unitMap.put("unitId",unitId);
            List<Machine> unitMachines = machineService.queryList(unitMap);
            List<Machine> mrMachines = machineDao.queryNotUse(machine);
            map.put("unitMachines",unitMachines);
            map.put("mrMachines",mrMachines);
            return ResultUtils.success(ResultEnum.SUCCESS,map);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }

    /**
     *  增删单元机器
     */
    @ApiOperation("增删单元机器")
    @PostMapping("/saveUnit")
    public Result saveUnit(@RequestBody SaveUnitMachine saveUnit) {
        Machine machine =  new Machine();
        if(null != saveUnit.getUnitId()){
            machine.setUnitId(saveUnit.getUnitId());
            try {
                machineService.updateByUnitId(saveUnit.getUnitId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(null != saveUnit.getIdList()){
            for (String id:saveUnit.getIdList()) {
                machine.setId(id);
                machineService.updateObject(machine);
            }
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     *  查询单元列表
     */
    @GetMapping("/queryUnitInfo/{machineRoomId}/{unitType}")
    public Result queryUnitInfo(@PathVariable("machineRoomId") String machineRoomId,@PathVariable("unitType") String unitType) {
        HashMap<String, String> map= new HashMap<>();
        map.put("machineRoomId",machineRoomId);
        map.put("unitType",unitType);
        try {
            return ResultUtils.success(ResultEnum.SUCCESS, unitizationService.queryList(map));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    /**
     *  查询单元化机器信息
     */
    @GetMapping("/queryMachine/{unitId}")
    public Result queryMachine(@PathVariable("unitId") String unitId){
        Machine machine = new Machine();
        machine.setUnitId(unitId);
        try {
            return ResultUtils.success(ResultEnum.SUCCESS, machineService.queryList(machine));
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
    @PostMapping("/check/uniqueness")
    @ApiOperation("唯一性检查")
    public Result checkUniqueness(@RequestBody Unitization unitization){
        try {
            int count = unitizationDao.selectCount(unitization);
            if(count>0){
                return ResultUtils.error(207,"当前单元编码与备份号已存在");
            }
            return ResultUtils.success();
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }
}