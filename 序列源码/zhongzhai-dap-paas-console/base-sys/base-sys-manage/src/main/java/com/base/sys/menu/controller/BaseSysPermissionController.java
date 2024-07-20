package com.base.sys.menu.controller;


import com.alibaba.fastjson.JSONObject;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.cache.UserInfoCache;
import com.base.sys.api.common.*;
import com.base.sys.api.dto.*;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.api.entity.BaseSysPermission;
import com.base.sys.api.entity.BaseSysPermissionData;
import com.base.sys.async.service.AsyncCacheService;
import com.base.sys.menu.dao.BaseSysAppDao;
import com.base.sys.menu.dao.BaseSysOperateDao;
import com.base.sys.menu.dao.BaseSysPermissionDao;
import com.base.sys.menu.service.BaseSysMenuService;
import com.base.sys.menu.service.BaseSysOperateService;
import com.base.sys.menu.service.BaseSysPermissionDataService;
import com.base.sys.menu.service.BaseSysPermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * @author QuJing
 * @date 2021/1/14
 */
@RestController
@RequestMapping("/baseSysPermissionEntity")
public class BaseSysPermissionController {

    private static final Logger log = LoggerFactory.getLogger(
            BaseSysPermissionController.class);

    @Resource
    private BaseSysPermissionService baseSysPermissionService;

    @Resource
    private BaseSysMenuService baseSysMenuService;

    @Resource
    private BaseSysPermissionDataService baseSysPermissionDataService;

    @Resource
    private BaseSysOperateService baseSysOperateService;

    @Resource
    private BaseSysPermissionDao baseSysPermissionDao;

    @Lazy
    @Autowired
    private AsyncCacheService   cacheService;


    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne/{id}")
    public Result selectOne(@PathVariable("id") String id) {
        BaseSysPermission baseSysPermissionEntity = null;
        try {
            baseSysPermissionEntity = baseSysPermissionService.getObjectById(id);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, baseSysPermissionEntity);
    }


    /**
     * 新增数据
     *
     * @param baseSysPermissionEntity 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Result insert(@RequestBody BaseSysPermission baseSysPermissionEntity) {
        try {
            baseSysPermissionService.saveObject(baseSysPermissionEntity);
        } catch (ServiceException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }

    /**
     * 修改数据
     *
     * @param baseSysPermissionEntity 实体对象
     * @return 修改结果
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseSysPermission baseSysPermissionEntity) {
        try {
            baseSysPermissionService.updateObject(baseSysPermissionEntity);
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
    public Result delete(@PathVariable("id") String id) {
        try {
            baseSysPermissionService.delObjectById(id);
        } catch (ServiceException e) {
            log.error("删除失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }


    /**
     * 根据角色查询菜单（树状）
     *
     * @param roleId
     * @return
     */
    @GetMapping("/selectMenuByRoleId/{roleId}")
    public Result selectMenuByRoleId(String roleId) {
        List<SysMenuDto> list = null;
        try {
            list = baseSysPermissionService.getSysMeunPermissionByRoleId(roleId);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询数据库失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }


    @GetMapping("/selectMenuByRoleAndMenu")
    public Result selectMenuByRoleAndMenu(BaseSysPermission baseSysPermission) {
        List<BaseSysOperate> list = null;
        try {
            list = baseSysPermissionService.getSysOperatePermissionByRoleAndMenu(baseSysPermission);
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
     * 授权（包括菜单操作点、数据授权）
     *
     * @param permissionToRoleDto
     * @return
     */

    @PostMapping("/addPermissionToRole")
    public Result addPermissionToRole(@RequestBody MenuDataRuleOperatePermission permissionToRoleDto) {
        try {
            //
        } catch (ServiceException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS);
    }


    /**
     * 授权展示，包含菜单授权、操作点授权、数据授权
     *
     * @param roleId
     * @return
     */
    @GetMapping("/showPermission/{roleId}")
    public Result showPermission(@PathVariable("roleId") String roleId) {
        MenuDataRuleOperatePermission menuDataRuleOperatePermission = new MenuDataRuleOperatePermission();

        try {
            //根据角色查询菜单、操作点、授权数据
            baseSysPermissionService.showMenuAndDataPermission(roleId);

            //查询所有的菜单、操作点、授权数据
            List<SysMenuDto> allMenuDtoList = baseSysMenuService.getAllMenuDataRuleOperate();
            menuDataRuleOperatePermission.setAllBaseSysDataList(allMenuDtoList);
            menuDataRuleOperatePermission.setAllSysMenuDtoList(allMenuDtoList);
        } catch (ServiceException | DaoException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, menuDataRuleOperatePermission);
    }


    /**
     * 返回树 菜单和操作点
     *
     * @param baseSysPermission
     * @return
     */
    @GetMapping("/showOperatePermissions")
    public Result showOperatePermissions(BaseSysPermission baseSysPermission) {
        String roleId = "";
        String appId ="";
        PermissionShowDto showDto = new PermissionShowDto();
        try {
            if (null != baseSysPermission) {
                roleId = baseSysPermission.getRoleId();
                appId=baseSysPermission.getAppId();
            }
            showDto = baseSysPermissionService.showOperatePermissions(roleId,appId);
        } catch (ServiceException | DaoException e) {
            log.error("新增失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, showDto);
    }


    @PostMapping("/updateOperateAndDataRule")
    public Result updateOperateAndDataRule(@RequestBody PermissionShowDto permissionShowDto) {
        List<PermissionTreeDto> operatePermissionTreeDtos = new ArrayList<>();
        List<PermissionTreeDto> dataRulePermissionTreeDtos = new ArrayList<>();
        String roleId = "";
        String appId ="";
        if (null != permissionShowDto) {
            operatePermissionTreeDtos = permissionShowDto.getOperateList();
            dataRulePermissionTreeDtos = permissionShowDto.getDataRuleList();
            roleId = permissionShowDto.getRoleId();
            appId=permissionShowDto.getAppId();
        }

        boolean operateFlag = operatePermission(operatePermissionTreeDtos, roleId,appId);

        boolean dataRuleFlag = dataRulePermission(dataRulePermissionTreeDtos, roleId);

        if (operateFlag && dataRuleFlag) {
            return ResultUtils.success(ResultEnum.SUCCESS);
        } else {

            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    /**
     * 修改数据授权
     *
     * @param dataRulePermissionTreeDtos
     * @param roleId
     * @return
     * @throws DaoException
     */
    private boolean dataRulePermission(List<PermissionTreeDto> dataRulePermissionTreeDtos, String roleId) {
        boolean flag = true;
        //需要新增的数据权限 id集合
        List<String> addDataRuleList = new ArrayList<>();
        //保留的权限id
        List<String> savedPermissionIds = new ArrayList<>();

        List<String> deletePermissionIds = new ArrayList<>();


        try {
            if (CollectionUtils.isNotEmpty(dataRulePermissionTreeDtos)) {
                for (PermissionTreeDto permissionTreeDto : dataRulePermissionTreeDtos) {
                    // 权限id为空的 说明需要新授权的
                    if (StringUtils.isEmpty(permissionTreeDto.getDataPermissionId())) {
                        if (PermissionTypeEnum.DATA_RULE_TYPE.getCode().equals(permissionTreeDto.getType())) {
                            addDataRuleList.add(permissionTreeDto.getId());
                        }
                    } else {
                        savedPermissionIds.add(permissionTreeDto.getId());
                    }
                }
            }

            //查询旧的数据权限
            BaseSysPermissionData baseSysPermissionData = new BaseSysPermissionData();
            baseSysPermissionData.setRoleId(roleId);
            List<SysDataRuleDto> oldDataRuleDtos = baseSysPermissionDataService.getPermissionDataByRole(baseSysPermissionData);

            //旧的所有权限id 的集合
            List<String> oldPermissionIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(oldDataRuleDtos)) {
                oldPermissionIds = oldDataRuleDtos.stream().map(SysDataRuleDto::getPermissionDataId).collect(toList());
            }

            //需要删除的  求差集(库中已经存在的和现存的做差集)
            deletePermissionIds = oldPermissionIds.stream().filter(item -> !savedPermissionIds.contains(item)).collect(toList());

            //删除数据权限
            if (CollectionUtils.isNotEmpty(deletePermissionIds)) {
                if (deletePermissionIds.size() != baseSysPermissionDataService.delObjectByIds(deletePermissionIds)) {
                    flag = false;
                }
            }
            //新增数据规则权限
            if (CollectionUtils.isNotEmpty(addDataRuleList)) {
                for (String dataRuleId : addDataRuleList) {
                    BaseSysPermissionData sysPermissionData = new BaseSysPermissionData();
                    sysPermissionData.setDataRuleId(dataRuleId);
                    sysPermissionData.setRoleId(roleId);
                    baseSysPermissionDataService.saveObject(sysPermissionData);
                }
            }
        } catch (ServiceException | DaoException e) {
            log.error("操作失败", e);
            flag = false;
        }
        return flag;
    }


    /**
     * 菜单和操作点授权
     *
     * @param operatePermissionTreeDtos
     * @param roleId
     * @return
     */
    private boolean operatePermission(List<PermissionTreeDto> operatePermissionTreeDtos, String roleId,String appId) {
        //需要新增的菜单权限 id集合
        List<String> addMenuIdList = new ArrayList<>();
        //需要新增的操作点权限
        List<String> addOperateIdList = new ArrayList<>();
        //保留的权限id
        List<String> savedPermissionIds = new ArrayList<>();

        List<String> deletePermissionIds = new ArrayList<>();

        List<SysMenuDto> sysMenuDtoList = new ArrayList<>();

        Map<String, String> dataMap = new HashMap<>();

        boolean flag = true;
        try {
            if (CollectionUtils.isNotEmpty(operatePermissionTreeDtos)) {
                for (PermissionTreeDto permissionTreeDto : operatePermissionTreeDtos) {
                    // 权限id为空的 说明需要新授权的
                    if (StringUtils.isEmpty(permissionTreeDto.getSysPermissionId())) {
                        if (PermissionTypeEnum.MENU_TYPE.getCode().equals(permissionTreeDto.getType())) {
                            addMenuIdList.add(permissionTreeDto.getId());
                        } else if (PermissionTypeEnum.OPERATE_TYPE.getCode().equals(permissionTreeDto.getType())) {
                            addOperateIdList.add(permissionTreeDto.getId());
                            //这里的parentId 实则是操作点对应的菜单id(menuId)
                            dataMap.put(permissionTreeDto.getId(), permissionTreeDto.getParentId());
                        }
                    } else {
                        savedPermissionIds.add(permissionTreeDto.getId());
                    }
                }
            }

            // 查询旧的授权
            List<BaseSysPermission> permissions = baseSysPermissionService.getPermissionByRoleId(roleId);
            //旧的所有权限id 的集合
            List<String> oldPermissionIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(permissions)) {
                oldPermissionIds = permissions.stream().map(BaseSysPermission::getId).collect(toList());
            }
            //需要删除的  求差集(库中已经存在的和现存的做差集)
            deletePermissionIds = oldPermissionIds.stream().filter(item -> !savedPermissionIds.contains(item)).collect(toList());
            //删除权限
            if (CollectionUtils.isNotEmpty(deletePermissionIds)) {
                if (deletePermissionIds.size() != baseSysPermissionService.delObjectByIds(deletePermissionIds)) {
                    flag = false;
                }
            }
            //新增菜单权限
            if (CollectionUtils.isNotEmpty(addMenuIdList)) {
                for (String menuId : addMenuIdList) {
                    BaseSysPermission baseSysPermission = new BaseSysPermission();
                    baseSysPermission.setMenuId(menuId);
                    baseSysPermission.setRoleId(roleId);
                    baseSysPermission.setAppId(appId);
                    baseSysPermissionService.saveObject(baseSysPermission);
                }
                //刷缓存
                //角色拥有的菜单
//                Map<String, Object> menuMap = UserInfoCache.getCacheMap().get(SysConstant.USER_INFO_OWN_MENUS);
                Map map = new HashMap();
                map.put("roleId", roleId);
                List<BaseSysMenu> baseSysMenus = baseSysMenuService.queryListByRole(map);
                if (CollectionUtils.isNotEmpty(baseSysMenus)) {
                    for (BaseSysMenu baseSysMenu : baseSysMenus) {
                        SysMenuDto sysMenuDto = new SysMenuDto();
                        BeanUtils.copyProperties(baseSysMenu, sysMenuDto);
                        sysMenuDto.setMenuId(baseSysMenu.getId());
                        sysMenuDtoList.add(sysMenuDto);
                    }
                }

//                menuMap.put(roleId, sysMenuDtoList);
//                UserInfoCache.getCacheMap().put(SysConstant.USER_INFO_OWN_MENUS, menuMap);
            }

            //新增操作点权限
            if (CollectionUtils.isNotEmpty(addOperateIdList)) {
                for (String operateId : addOperateIdList) {
                    BaseSysPermission baseSysPermission = new BaseSysPermission();
                    baseSysPermission.setMenuId(dataMap.get(operateId));
                    baseSysPermission.setOperateId(operateId);
                    baseSysPermission.setRoleId(roleId);
                    baseSysPermissionService.saveObject(baseSysPermission);
                }


                //刷缓存
                cacheService.asyncWriteUserAppCache();


                //角色拥有的url
                Map<String, Object> urlsMap = UserInfoCache.getCacheMap().get(SysConstant.USER_INFO_ROLE_OWN_URL);
                //角色拥有的操作点对象
                Map<String, Object> operateMap = UserInfoCache.getCacheMap().get(SysConstant.USER_INFO_OWN_OPERATES);
                Map map = new HashMap();
                map.put("roleId", roleId);
                List<BaseSysOperate> operateList = baseSysOperateService.queryListByRole(map);
                List<String> urlList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(operateList)) {
                    for (BaseSysOperate baseSysOperate : operateList) {
                        urlList.add(baseSysOperate.getType() + baseSysOperate.getPath());
                    }
                }
                urlsMap.put(roleId, urlList);
                operateMap.put(roleId, operateList);

                UserInfoCache.getCacheMap().put(SysConstant.USER_INFO_ROLE_OWN_URL, urlsMap);
                UserInfoCache.getCacheMap().put(SysConstant.USER_INFO_OWN_OPERATES, operateMap);

            }
        } catch (ServiceException | DaoException e) {
            log.error("操作失败", e);
            flag = false;
        }
        return flag;
    }


    @GetMapping("/showOperatePermission")
    public Result showOperatePermission(BaseSysPermission baseSysPermission) {

        String roleId = "";
        List<String> arr = new ArrayList<>();
        try {
            if (null != baseSysPermission) {
                roleId = baseSysPermission.getRoleId();
            }
            if (!StringUtils.isEmpty(roleId)) {
                arr = baseSysPermissionService.showOperatePermission(roleId);
            }
        } catch (DaoException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, arr);
    }



    @PostMapping("/selectByRoleIds")
    public Result selectByRoleIds(@RequestBody JSONObject json) {
        List<String> roleIds = (List<String>) json.get("roleIds");
        List<BaseSysPermission> list = null;
        try {
            list = baseSysPermissionDao.selectByRoles(roleIds);
        } catch (ServiceException e) {
            log.error("查询失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        } catch (DaoException e) {
            log.error("查询数据库失败", e);
            return ResultUtils.error(ResultEnum.FAILED);
        }
        return ResultUtils.success(ResultEnum.SUCCESS, list);
    }

    @GetMapping("/get/app/menu")
    public Result getAppTree(String roleId) throws  DaoException{
        return baseSysPermissionService.getAppMenu(roleId);
    }

    @PostMapping("/add/app/menu/permission")
    public Result addPermission(@RequestBody  AddCheckMenuDto addCheckMenuDto) throws DaoException{
        return baseSysPermissionService.addPermission(addCheckMenuDto);
    }

}

