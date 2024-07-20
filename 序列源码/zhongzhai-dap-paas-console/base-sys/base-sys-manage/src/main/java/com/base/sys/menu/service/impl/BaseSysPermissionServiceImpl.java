package com.base.sys.menu.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.*;
import com.base.sys.api.dto.*;
import com.base.sys.api.entity.*;
import com.base.sys.menu.dao.*;
import com.base.sys.menu.service.*;
import com.base.sys.org.service.BaseSysUserRoleService;
import com.base.sys.utils.UserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


@Lazy
@Service
public class BaseSysPermissionServiceImpl extends AbstractBaseServiceImpl<BaseSysPermission, String> implements BaseSysPermissionService {

    @Lazy
    @Resource
    private BaseSysMenuService baseSysMenuService;

    @Lazy
    @Resource
    private BaseSysOperateService baseSysOperateService;

    @Resource
    private BaseSysDataRuleService baseSysDataRuleService;

    @Resource
    private BaseSysPermissionDataService baseSysPermissionDataService;

    @Resource
    private BaseSysPermissionDao baseSysPermissionDao;

    @Resource
    private BaseSysMenuDao baseSysMenuDao;

    @Resource
    private BaseSysPermissionDataDao baseSysPermissionDataDao;

    @Resource
    private BaseSysAppDao baseSysAppDao;

    @Autowired
    private BaseSysUserRoleService baseSysUserRoleService;

    @Override
    public List<SysMenuDto> getSysMeunPermissionByRoleId(String roleId) throws DaoException {

        // 最后的结果
        List<SysMenuDto> menuList = new ArrayList<SysMenuDto>();

        List<SysMenuDto> sysMenuDtos = new ArrayList<>();

        //menu的set集合
        Set<String> leafMenuIdSet = new HashSet<>();

        //叶子节点的list集合
        List<SysMenuDto> leafMenuLsit = new ArrayList<>();

        //有操作点的权限集合
        List<BaseSysPermission> permissionHasOperateLsit = new ArrayList<>();


        Map map = new HashMap<>();
        map.put("roleId", roleId);
        List<BaseSysPermission> list = this.getBaseDao().queryList(map);

        if (CollectionUtils.isNotEmpty(list)) {
            for (BaseSysPermission baseSysPermission : list) {
                String meunId = baseSysPermission.getMenuId();
                // 相同menuId的，只查询一次,先保存到叶子节点的list集合中
                if (!leafMenuIdSet.contains(meunId)) {
                    leafMenuIdSet.add(meunId);
                    BaseSysMenu menu = baseSysMenuService.getObjectById(meunId);
                    if (null != menu) {
                        SysMenuDto sysMenuDto = new SysMenuDto();
                        BeanUtils.copyProperties(menu, sysMenuDto);
                        sysMenuDto.setMenuId(menu.getId());
                        sysMenuDto.setSysPermissionId(baseSysPermission.getId());
                        //这里sysMenuDtos只添加非叶子节点的菜单，叶子节点的先放leafMenuLsit中，在后面统一加到sysMenuDtos中
                        if (SysMenuEnum.LEAF.getCode().equals(sysMenuDto.getIsLeaf())) {
                            leafMenuLsit.add(sysMenuDto);
                        } else {
                            sysMenuDtos.add(sysMenuDto);
                        }
                    }
                }
                //查询授权对象下面是否有操作点
                if (!StringUtils.isEmpty(baseSysPermission.getOperateId())) {
                    permissionHasOperateLsit.add(baseSysPermission);
                }
            }
            //将permissionHasOperateLsit按照menuId 分组
            Map<String, List<BaseSysPermission>> collect = new HashMap<>();
            if (CollectionUtils.isNotEmpty(permissionHasOperateLsit)) {
                collect = permissionHasOperateLsit.stream()
                        .collect(Collectors.groupingBy(BaseSysPermission::getMenuId));
            }
            if (CollectionUtils.isNotEmpty(leafMenuLsit)) {
                for (SysMenuDto sysMenuDto : leafMenuLsit) {
                    if (!collect.isEmpty()) {
                        List<BaseSysPermission> permissions = collect.get(sysMenuDto.getMenuId());
                        if (CollectionUtils.isNotEmpty(permissions)) {
                            List<SysOperateDto> operateDtoList = new ArrayList<>();
                            for (BaseSysPermission baseSysPermission : permissions) {
                                //根据operateId 查询 操作点
                                BaseSysOperate baseSysOperate = baseSysOperateService.getObjectById(baseSysPermission.getOperateId());
                                if (null != baseSysOperate) {
                                    SysOperateDto sysOperateDto = new SysOperateDto();
                                    sysOperateDto.setSysPermissionId(baseSysPermission.getId());
                                    sysOperateDto.setOperateId(baseSysOperate.getId());
                                    BeanUtils.copyProperties(baseSysOperate, sysOperateDto);
                                    operateDtoList.add(sysOperateDto);
                                }
                            }
                            //给叶子节点菜单下添加操作点
                            sysMenuDto.setSysOperateDtoList(operateDtoList);
                        }
                    }
                    //添加叶子节点菜单
                    sysMenuDtos.add(sysMenuDto);
                }
            }


            // 整理对象sysMenuDto
            for (int i = 0; i < sysMenuDtos.size(); i++) {
                // 一级菜单没有parentId
                if (StringUtils.isEmpty(sysMenuDtos.get(i).getParentId())) {
                    menuList.add(sysMenuDtos.get(i));
                }
            }
            for (SysMenuDto menu : menuList) {
                menu.setChildren(getChild(menu.getMenuId(), sysMenuDtos));
            }
        }
        return menuList;
    }


    public List<SysMenuDto> getChild(String id, List<SysMenuDto> allMenu) {
        // 子菜单
        List<SysMenuDto> childList = new ArrayList<>();
        for (SysMenuDto menu : allMenu) {

            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!StringUtils.isEmpty(menu.getParentId())) {
                if (menu.getParentId().equals(id)) {
                    childList.add(menu);
                }
            }
        }

        // 把子菜单的子菜单再循环一遍
        for (SysMenuDto menu : childList) {
            if (!StringUtils.isEmpty(menu.getParentId())) {
                // 递归
                menu.setChildren(getChild(menu.getMenuId(), allMenu));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }


    @Override
    public List<BaseSysOperate> getSysOperatePermissionByRoleAndMenu(BaseSysPermission baseSysPermissionEntity) throws DaoException {
        Map map = new HashMap<>();
        map.put("roleId", baseSysPermissionEntity.getRoleId());
        map.put("menuId", baseSysPermissionEntity.getMenuId());
        List<BaseSysPermission> list = this.getBaseDao().queryList(map);
        List<BaseSysOperate> baseSysOperates = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (BaseSysPermission baseSysPermission : list) {
                String operateId = baseSysPermission.getOperateId();
                BaseSysOperate baseSysOperate = baseSysOperateService.getObjectById(operateId);
                baseSysOperates.add(baseSysOperate);
            }
        }
        return baseSysOperates;
    }


    @Override
    public List<SysMenuDto> showMenuAndDataPermission(String roleId) throws DaoException {

        // 最后的结果
        List<SysMenuDto> menuList = new ArrayList<SysMenuDto>();
         return menuList;
    }


    @Override
    public PermissionShowDto showOperatePermissions(String roleId,String appId) throws DaoException {

        PermissionShowDto showDto = new PermissionShowDto();
        List<PermissionTreeDto> permissionOperateTreeDtoList = new ArrayList<>();
        List<PermissionTreeDto> permissionOperateTreeDtos = new ArrayList<>();
        //有权限的
        List<PermissionTreeDto> hasPermissionOperateTreeDtos = new ArrayList<>();

        //所有操作点的集合
        List<BaseSysOperate> allBaseSysOperateList = new ArrayList<>();
        //有操作权限菜单id的集合
        List<String> permissionMenuIds = new ArrayList<>();

        // 将菜单权限id放在map中
        Map<String, String> permissionMenuIdMap = new HashMap<>();

        // 将操作点权限id放在map中
        Map<String, String> permissionOperateIdMap = new HashMap<>();


        List<String> permissionOperateIds = new ArrayList<>();

        Set<String> appSet = new HashSet<>();


        //查到该应用下所有的授权的菜单
        BaseSysMenu sysMenu = new BaseSysMenu();
        sysMenu.setAppId(appId);
        List<BaseSysMenu> baseSysMenus = baseSysMenuService.getAllBaseSysMenusByAppId(sysMenu);
        //将叶子节点菜单下的操作点查询出来
        if (CollectionUtils.isNotEmpty(baseSysMenus)) {
            for (BaseSysMenu baseSysMenu : baseSysMenus) {
                //如果是叶子节点，查询下面的操作点
                if (SysMenuEnum.LEAF.getCode().equals(baseSysMenu.getIsLeaf())) {
                    List<BaseSysOperate> baseSysOperates = baseSysOperateService.getOperateByMenuId(baseSysMenu.getId());
                    if (CollectionUtils.isNotEmpty(baseSysOperates)) {
                        allBaseSysOperateList.addAll(baseSysOperates);
                    }
                }
            }
        }



        for (BaseSysMenu menu : baseSysMenus) {
            PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
            permissionTreeDto.setId(menu.getId());
            permissionTreeDto.setType(PermissionTypeEnum.MENU_TYPE.getCode());
            //如果菜单的parent为空，用所在应用的id填充
            if(StringUtils.isEmpty(menu.getParentId())){
//                permissionTreeDto.setParentId(menu.getParentId());
//                if(!appSet.contains(menu.getAppId())){
//                    appSet.add(menu.getAppId());
//                    PermissionTreeDto appPermissionDto= new PermissionTreeDto();
//                    appPermissionDto.setId(menu.getAppId());
//                    appPermissionDto.setName("应用");
//                    appPermissionDto.setType(PermissionTypeEnum.APP_TYPE.getCode());
//                    permissionOperateTreeDtos.add(appPermissionDto);
//                }
            }else{
                permissionTreeDto.setParentId(menu.getParentId());
            }

            permissionTreeDto.setName(menu.getText());
            if (permissionMenuIds.contains(menu.getId())) {
                permissionTreeDto.setChecked(true);
                permissionTreeDto.setSysPermissionId(permissionMenuIdMap.get(menu.getId()));

                PermissionTreeDto hasPermission = new PermissionTreeDto();
                BeanUtils.copyProperties(permissionTreeDto, hasPermission);

                hasPermissionOperateTreeDtos.add(hasPermission);
            }
            permissionOperateTreeDtos.add(permissionTreeDto);
        }

        for (BaseSysOperate operate : allBaseSysOperateList) {
            PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
            permissionTreeDto.setId(operate.getId());
            permissionTreeDto.setType(PermissionTypeEnum.OPERATE_TYPE.getCode());
            //为了返回树结构，将menuId设为parentId
            permissionTreeDto.setParentId(operate.getMenuId());
            permissionTreeDto.setName(operate.getName());
            if (permissionOperateIds.contains(operate.getId())) {
                permissionTreeDto.setSysPermissionId(permissionOperateIdMap.get(operate.getId()));
                permissionTreeDto.setChecked(true);

                PermissionTreeDto hasPermission = new PermissionTreeDto();
                BeanUtils.copyProperties(permissionTreeDto, hasPermission);
                hasPermissionOperateTreeDtos.add(hasPermission);
            }
            permissionOperateTreeDtos.add(permissionTreeDto);
        }


        //整理 permissionOperateTreeDtos

        for (int i = 0; i < permissionOperateTreeDtos.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(permissionOperateTreeDtos.get(i).getParentId())) {
                permissionOperateTreeDtoList.add(permissionOperateTreeDtos.get(i));
            }
        }
        for (PermissionTreeDto menu : permissionOperateTreeDtoList) {
            menu.setChildren(getChildren(menu.getId(), permissionOperateTreeDtos));
        }
        showDto.setAllShow(permissionOperateTreeDtoList);
        return showDto;
    }


    /**
     * 整理 List<PermissionTreeDto> 数据
     *
     * @param id
     * @param permissionTreeDtos
     * @return
     */
    private List<PermissionTreeDto> getChildren(String id, List<PermissionTreeDto> permissionTreeDtos) {
        // 子菜单
        List<PermissionTreeDto> childList = new ArrayList<>();
        for (PermissionTreeDto permissionTreeDto : permissionTreeDtos) {

            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!StringUtils.isEmpty(permissionTreeDto.getParentId())) {
                if (permissionTreeDto.getParentId().equals(id)) {
                    childList.add(permissionTreeDto);
                }
            }
        }

        // 把子菜单的子菜单再循环一遍
        for (PermissionTreeDto permissionTreeDto : childList) {
            if (!StringUtils.isEmpty(permissionTreeDto.getParentId())) {
                // 递归
                permissionTreeDto.setChildren(getChildren(permissionTreeDto.getId(), permissionTreeDtos));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }


    @Override
    public List<BaseSysPermission> getPermissionByRoleId(String roleId) throws DaoException {
        Map map = new HashMap<>();
        map.put("roleId", roleId);
        List<BaseSysPermission> list = this.getBaseDao().queryList(map);
        return list;
    }


    @Override
    public List<String> showOperatePermission(String roleId) throws DaoException {
        //      查询授权表，将授权的菜单和操作点查出来
        Map map = new HashMap<>();
        map.put("roleId", roleId);
        List<String> arr = new ArrayList<>();
        Set<String> menuIdSet = new HashSet<>();
        Set<String> menuIds = new HashSet<>();

        List<BaseSysPermission> sysPermissions = this.getBaseDao().queryList(map);
        if (CollectionUtils.isNotEmpty(sysPermissions)) {
            for (BaseSysPermission baseSysPermission : sysPermissions) {
                String meunId = baseSysPermission.getMenuId();
                String operateId = baseSysPermission.getOperateId();
                if (!StringUtils.isEmpty(meunId)) {
                    menuIds.add(meunId);
                }
                if (!StringUtils.isEmpty(operateId)) {
                    menuIdSet.add(operateId);
                }
            }
            for(String menu:menuIds){
                map.put("parentId", menu);
                map.put("menuId", menu);
              Integer integer = baseSysMenuService.selectTotal(map);
              Integer integer1 = baseSysOperateService.selectTotal(map);
                if (integer == 0 && integer1 == 0) {
                    menuIdSet.add(menu);
                }
            }
            arr.clear();
            arr.addAll(menuIdSet);
        }
        return arr;
    }

    @Override
    public List<BaseSysPermission> selectByRoles(List<String> roleIds) throws DaoException {
        return baseSysPermissionDao.selectByRoles(roleIds);
    }

    @Override
    public Result getAppMenu(String roleId) throws DaoException{
        AppMenuTreeDto appMenuTreeDto = new AppMenuTreeDto();
        List<TreeNodeData> appList = baseSysAppDao.queryIdAndName(new HashMap());
        if (appList.isEmpty()){
            return ResultUtils.error(205,"应用列表为空");
        }
        CountDownLatch countDownLatch=new CountDownLatch(appList.size());
        List<MenuTreeDto> menuTreeDtoList = new ArrayList<>();
        AuthenticationUserDto userDto = UserUtil.getUser();
        for (TreeNodeData app:appList){
            Map<String,String> sqlMap = new HashMap<>();
            sqlMap.put("appId",app.getId());
            sqlMap.put("tenantId",userDto.getTenantId());
            new Thread( new Runnable(){
                @Override
                public void run() {
                  try{
                      MenuTreeDto menuTreeDto = new MenuTreeDto();
                      menuTreeDto.setMenuList(baseSysMenuDao.queryIdAndName(sqlMap));
                      menuTreeDto.setOperateList(baseSysOperateService.queryIdAndName(app.getId(), userDto.getTenantId()));
                      menuTreeDto.setDataList(baseSysDataRuleService.queryIdAndName(app.getId(), userDto.getTenantId()));
                      menuTreeDto.setCheckMenu(getSysMenuCheck(app.getId(),roleId,userDto.getTenantId()));
                      menuTreeDto.setCheckOprate(getSysOperateCheck(app.getId(),roleId,userDto.getTenantId()));
                      menuTreeDto.setCheckData(getSysDataCheck(app.getId(),roleId,userDto.getTenantId()));
                      menuTreeDto.setCheckDataMenu(getSysDataMenuCheck(app.getId(),roleId,userDto.getTenantId()));
                      menuTreeDto.setAppId(app.getId());
                      menuTreeDtoList.add(menuTreeDto);
                      countDownLatch.countDown();
                  }catch (Exception e){
                      e.printStackTrace();
                      countDownLatch.countDown();
                  }
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {

        }
        appMenuTreeDto.setAppList(appList);
        appMenuTreeDto.setMenuTreeList(menuTreeDtoList);

        return ResultUtils.success(appMenuTreeDto);
    }

    public   List<String> getSysMenuCheck(String appId,String roleId,String tenantId) throws DaoException{
        BaseSysPermission sysPermission = new BaseSysPermission();
        sysPermission.setAppId(appId);
        sysPermission.setRoleId(roleId);
        sysPermission.setTenantId(tenantId);

        return baseSysPermissionDao.queryCheckMenu(sysPermission);

    }


    public List<String> getSysOperateCheck(String appId,String roleId,String tenantId) throws DaoException{
        BaseSysPermission sysPermission = new BaseSysPermission();
        sysPermission.setAppId(appId);
        sysPermission.setRoleId(roleId);
        sysPermission.setTenantId(tenantId);
        return baseSysPermissionDao.queryCheckOperate(sysPermission);

    }

    public List<String> getSysDataCheck(String appId,String roleId,String tenantId) throws DaoException{
        BaseSysPermissionData baseSysPermissionData = new BaseSysPermissionData();
        baseSysPermissionData.setAppId(appId);
        baseSysPermissionData.setRoleId(roleId);
        baseSysPermissionData.setTenantId(tenantId);
        return baseSysPermissionDataDao.queryCheckData(baseSysPermissionData);

    }

    public List<String> getSysDataMenuCheck(String appId,String roleId,String tenantId) throws DaoException{
        BaseSysPermissionData baseSysPermissionData = new BaseSysPermissionData();
        baseSysPermissionData.setAppId(appId);
        baseSysPermissionData.setRoleId(roleId);
        baseSysPermissionData.setTenantId(tenantId);
        return baseSysPermissionDataDao.queryCheckDataMenu(baseSysPermissionData);

    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public  Result addPermission(AddCheckMenuDto addCheckMenuDto) throws DaoException{
        String roleId = addCheckMenuDto.getRoleId();

        if (roleId==null || "".equals(roleId)){
            return ResultUtils.error(205,"角色id不能为空");
        }
        try {
            if(addCheckMenuDto.getFuncSave()!=null  && addCheckMenuDto.getFuncSave().size()>0){
                for(TreeNodeData treeNodeData : addCheckMenuDto.getFuncSave()) {
                    BaseSysPermission baseSysPermission = new BaseSysPermission();
                    baseSysPermission.setRoleId(roleId);
                    baseSysPermission.setAppId(treeNodeData.getAppId());
                    if (treeNodeData.getFlag().equals("save")){
                        if (treeNodeData.getId().contains("m_")) {
                            baseSysPermission.setMenuId(treeNodeData.getId().split("_")[1]);
                            baseSysPermission.setId(SnowflakeIdWorker.getID());
                            baseSysPermission.setOperateId(null);
                            baseSysPermissionDao.saveObject(baseSysPermission);
                        } else {
                            baseSysPermission.setMenuId(treeNodeData.getMenuId().split("_")[1]);
                            baseSysPermission.setId(SnowflakeIdWorker.getID());
                            baseSysPermission.setOperateId(treeNodeData.getId().split("_")[1]);
                            baseSysPermissionDao.saveObject(baseSysPermission);
                        }
                    }else {

                        if (treeNodeData.getId().contains("m_")) {
                            baseSysPermission.setMenuId(treeNodeData.getId().split("_")[1]);
                            baseSysPermission.setId(SnowflakeIdWorker.getID());
                            baseSysPermission.setOperateId(null);
                            baseSysPermissionDao.delObjectByMenu(baseSysPermission);
                        } else {
                            baseSysPermission.setMenuId(treeNodeData.getMenuId().split("_")[1]);
                            baseSysPermission.setId(SnowflakeIdWorker.getID());
                            baseSysPermission.setOperateId(treeNodeData.getId().split("_")[1]);
                            baseSysPermissionDao.delObjectByOperate(baseSysPermission);
                        }

                    }

                }
            }

            if(addCheckMenuDto.getDataSave()!=null  && addCheckMenuDto.getDataSave().size()>0){
                for(TreeNodeData treeNodeData : addCheckMenuDto.getDataSave()) {
                    BaseSysPermissionData baseSysPermissionData = new BaseSysPermissionData();
                    baseSysPermissionData.setRoleId(roleId);
                    baseSysPermissionData.setAppId(treeNodeData.getAppId());
                    if (treeNodeData.getFlag().equals("save")){
                        if (treeNodeData.getId().contains("m_")) {
                            baseSysPermissionData.setMenuId(treeNodeData.getId().split("_")[1]);
                            baseSysPermissionData.setId(SnowflakeIdWorker.getID());
                            baseSysPermissionData.setDataRuleId(null);
                            baseSysPermissionDataService.saveObject(baseSysPermissionData);
                        } else {
                            baseSysPermissionData.setMenuId(treeNodeData.getMenuId().split("_")[1]);
                            baseSysPermissionData.setId(SnowflakeIdWorker.getID());
                            baseSysPermissionData.setDataRuleId(treeNodeData.getId().split("_")[1]);
                            baseSysPermissionDataService.saveObject(baseSysPermissionData);
                        }
                    }else {

                        if (treeNodeData.getId().contains("m_")) {
                            baseSysPermissionData.setMenuId(treeNodeData.getId().split("_")[1]);
                            baseSysPermissionData.setId(SnowflakeIdWorker.getID());
                            baseSysPermissionData.setDataRuleId(null);
                            baseSysPermissionDataDao.delObjectByMenu(baseSysPermissionData);
                        } else {
                            baseSysPermissionData.setMenuId(treeNodeData.getMenuId().split("_")[1]);
                            baseSysPermissionData.setId(SnowflakeIdWorker.getID());
                            baseSysPermissionData.setDataRuleId(treeNodeData.getId().split("_")[1]);
                            baseSysPermissionDataDao.delObjectByData(baseSysPermissionData);
                        }

                    }

                }
            }


            return ResultUtils.success(ResultEnum.SUCCESS);

        }catch (Exception e){
            e.printStackTrace();
            return ResultUtils.error(ResultEnum.FAILED);
        }

    }
    @Override
    public List<BaseSysPermission>  selectByUser(Map map) throws DaoException{
        List<BaseSysPermission> permissionList = new ArrayList<>();
        List<BaseSysUserRole> userRoleList = baseSysUserRoleService.queryList(map);
        if (userRoleList.isEmpty()){
            return null;
        }
        for (BaseSysUserRole sysUserRole:userRoleList){
            BaseSysPermission baseSysPermission = new BaseSysPermission();
            baseSysPermission.setRoleId(sysUserRole.getRoleId());
            List<BaseSysPermission> queryPermission = this.queryList(baseSysPermission);
            permissionList.addAll(queryPermission);
        }


        return  permissionList;
    }

}
