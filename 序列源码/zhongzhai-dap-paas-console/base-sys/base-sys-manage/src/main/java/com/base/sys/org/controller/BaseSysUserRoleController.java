package com.base.sys.org.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.dto.Page;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.api.entity.BaseSysUserRole;
import com.base.sys.org.service.BaseSysUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/14
 */
@Controller
@RequestMapping("baseSysUserRole")
public class BaseSysUserRoleController {
    private static final Logger log = LoggerFactory.getLogger(
            BaseSysUserRoleController.class);

    @Resource
    private BaseSysUserRoleService baseSysUserRoleService;


    /**
     *  根据roleId 查询所有的关联用户数据，再根据关联的用户，id分页查询所有的用户信息
     * @return 单条数据
     */

    @PostMapping("/queryPage")
    public @ResponseBody Result queryRoleUserPage(@RequestBody JSONObject json) {

        try {
            String roleId = (String)json.get("id");
            String name = (String)json.get("name");
            if(StringUtils.isEmpty(roleId)){
                return ResultUtils.error(211,"roleId 不能为空");
            }

            Page page = baseSysUserRoleService.queryUserByRole(roleId,name);
            return ResultUtils.success(ResultEnum.SUCCESS, page);
        } catch (Exception e) {
            log.error("查询失败", e);
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }
    /**
     * 新增数据
     *
     * @return 新增结果
     * @paramBaseSysUserRole 实体对象
     */
    @PostMapping("insert")
    public @ResponseBody Result insert(@RequestBody JSONObject json) {
        try {
            String roleId = (String) json.get("roleId");
            String createUserId = (String) json.get("createUserId");
            List<String> userIdList = (List<String>) json.get("userIdList");
            for (String userId : userIdList
            ) {
                BaseSysUserRole userRole = new BaseSysUserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRole.setCreateOrgId(createUserId);
                baseSysUserRoleService.saveObject(userRole);
            }
        } catch (ServiceException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 修改数据
     *
     * @return 修改结果
     * @paramBaseSysUserRole 实体对象
     */
    @PutMapping("update")
    public Result update(BaseSysUserRole baseSysUserRole) {
        try {
            baseSysUserRoleService.updateObject(baseSysUserRole);
        } catch (ServiceException e) {
            log.error("修改失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 删除数据
     * @return 删除结果
     */
    @DeleteMapping("deleteAll")
    public @ResponseBody  Result delete(@RequestBody JSONObject json) {
        try {
            List<String> idList = (List) json.get("ids");
            baseSysUserRoleService.delObjectByIds(idList);
        } catch (ServiceException e) {
            log.error("删除失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

}

