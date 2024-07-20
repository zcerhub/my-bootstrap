package com.base.sys.menu.controller;


import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PermissionShowDto;
import com.base.sys.api.dto.PermissionTreeDto;
import com.base.sys.api.dto.SysDataRuleDto;
import com.base.sys.api.entity.BaseSysPermissionData;
import com.base.sys.menu.service.BaseSysPermissionDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QuJing
 * @date 2021/1/14
 */
@RestController
@RequestMapping("/baseSysPermissionDataEntity")
public class BaseSysPermissionDataController {

    private static final Logger log = LoggerFactory.getLogger(
            BaseSysPermissionDataController.class);

    @Resource
    private BaseSysPermissionDataService baseSysPermissionDataService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne/{id}")
    public Result selectOne(@PathVariable("id")String id) {
        BaseSysPermissionData baseSysPermissionDataEntity = null;
        try {
            baseSysPermissionDataEntity = baseSysPermissionDataService.getObjectById(id);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, baseSysPermissionDataEntity);
    }

    /**
     * 新增数据
     *
     * @param baseSysPermissionDataEntity 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysPermissionData baseSysPermissionDataEntity) {
        try {
            baseSysPermissionDataService.saveObject(baseSysPermissionDataEntity);
        } catch (ServiceException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 修改数据
     *
     * @param baseSysPermissionDataEntity 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysPermissionData baseSysPermissionDataEntity) {
        try {
            baseSysPermissionDataService.updateObject(baseSysPermissionDataEntity);
        } catch (ServiceException e) {
            log.error("修改失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 删除数据
     *
     * @param id 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable("id")String id) {
        try {
            baseSysPermissionDataService.delObjectById(id);
        } catch (ServiceException e) {
            log.error("删除失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }



    /**
     *    根据角色查询授权的数据
     * @param baseSysPermissionData
     * @return
     */
    @GetMapping("/selectPermissionDataByRole")
    public Result selectPermissionDataByRole(BaseSysPermissionData baseSysPermissionData) {
        List<SysDataRuleDto> dataList = new ArrayList<>();
        try {
            dataList = baseSysPermissionDataService.getPermissionDataByRole(baseSysPermissionData);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询数据库失败", e);
            return ResultUtils.error(ResultEnum.FAILED);

        }
        return ResultUtils.success(ResultEnum.SUCCESS, dataList);
    }


    /**
     * 返回树 菜单和数据规则
     *
     * @param baseSysPermissionData
     * @return
     */
    @GetMapping("/showDataRulePermissions")
    public Result showDataRulePermissions(BaseSysPermissionData baseSysPermissionData) {
        String roleId="";
        PermissionShowDto showDto = new PermissionShowDto();
        try {
            if(baseSysPermissionData != null){
                roleId = baseSysPermissionData.getRoleId();
            }

            showDto = baseSysPermissionDataService.showDataRulePermissions(roleId);
        } catch (ServiceException | DaoException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, showDto);
    }




    @GetMapping("/showDataPermission")
    public Result showDataPermission(BaseSysPermissionData baseSysPermissionData) {
        String roleId="";
        List<PermissionTreeDto> permissionTreeDtos = new ArrayList<>();
        try {
            if(baseSysPermissionData != null){
                roleId = baseSysPermissionData.getRoleId();
            }
            permissionTreeDtos = baseSysPermissionDataService.showDataPermission(roleId);
        } catch (ServiceException | DaoException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, permissionTreeDtos);
    }





}

