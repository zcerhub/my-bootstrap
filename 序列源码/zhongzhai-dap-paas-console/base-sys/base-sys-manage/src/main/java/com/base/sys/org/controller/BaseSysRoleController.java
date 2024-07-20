package com.base.sys.org.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.dto.PageInDto;
import com.base.sys.api.dto.SysRoleDto;
import com.base.sys.api.entity.BaseSysRole;
import com.base.sys.org.service.BaseSysRoleService;
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
@RequestMapping("/baseSysRoleEntity")
public class BaseSysRoleController {

    private static final Logger log = LoggerFactory.getLogger(
            BaseSysRoleController.class);

    @Resource
    private BaseSysRoleService baseSysRoleService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne/{id}")
    public Result selectOne(@PathVariable("id") String id) {
        BaseSysRole baseSysRoleEntity = null;
        try {
            baseSysRoleEntity = baseSysRoleService.getObjectById(id);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, baseSysRoleEntity);
    }

    /**
     * 新增数据
     *
     * @param baseSysRoleEntity 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysRole baseSysRoleEntity) {
        try {
                Integer count = baseSysRoleService.getRoleCountByParentId(baseSysRoleEntity.getParentId());
                baseSysRoleEntity.setRoleSort(count + 1);
            if (1 == baseSysRoleService.saveObject(baseSysRoleEntity)) {
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
        } catch (ServiceException | DaoException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }

    /**
     * 修改数据
     *
     * @param baseSysRoleEntity 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysRole baseSysRoleEntity) {
        try {
            if (1 == baseSysRoleService.updateObject(baseSysRoleEntity)) {
                return ResultUtils.success(ResultEnum.SUCCESS);
            }
        } catch (ServiceException e) {
            log.error("修改失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.error(ResultEnum.FAILED);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping("/deleteAll")
    public Result delete(@RequestBody JSONObject idList) {
        List<String> ids = (List<String>) idList.get("idList");
        try {
            if (ids.size() == baseSysRoleService.delObjectByIds(ids)) {
                return ResultUtils.success(ResultEnum.SUCCESS);
            } else {
                return ResultUtils.error(ResultEnum.FAILED);
            }

        } catch (ServiceException e) {
            log.error("删除失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }


    /**
     * 根据名称或者code查询角色
     *
     * @param baseSysRole
     * @return
     */
    @GetMapping("/selectRoleByNameOrCode")
    public Result selectRoleByNameOrCode(BaseSysRole baseSysRole) {
        List<BaseSysRole> list = new ArrayList<>();
        try {
            list = baseSysRoleService.getSysRoleByNameOrCode(baseSysRole);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询数据库失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }


    @GetMapping("queryPage")
    public Result queryPage(PageInDto<BaseSysRole> pages) {

        Page page = null;
        try {
            page = baseSysRoleService.queryPage(pages.getPageNo(), pages.getPageSize(), pages.getRequestObject());
        } catch (ServiceException e) {
            log.error("查询失败：", e.getStackTrace());
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
        log.info(page.toString());
        return ResultUtils.success(ResultEnum.SUCCESS, page);
    }


    /**
     * 查询所有角色、返回树形结构
     *
     * @return
     */
    @GetMapping("/queryAllRole")
    public Result queryAllRole() {
        List<SysRoleDto> list = new ArrayList<>();
        try {
            list = baseSysRoleService.getAllRole();
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询数据库失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }


    /**
     * 修改角色的排序
     *
     * @param input
     * @return
     */
    @PutMapping("updateRoleSort")
    public Result updateRoleSort(@RequestBody JSONObject input) {
        try {

            baseSysRoleService.updateRoleSort(input);
        } catch (Exception e) {
            log.error("修改失败", e.getStackTrace());
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);


    }


}

