package com.base.sys.async.service.impl;

import com.base.api.exception.DaoException;
import com.base.sys.api.cache.DictMapCache;
import com.base.sys.api.cache.UserAppCache;
import com.base.sys.api.cache.UserInfoCache;
import com.base.sys.api.common.PermissionTypeEnum;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.common.SysMenuEnum;
import com.base.sys.api.dto.SysAppMenuDto;
import com.base.sys.api.dto.SysMenuDto;
import com.base.sys.api.entity.*;
import com.base.sys.async.service.AsyncCacheService;
import com.base.sys.dict.service.BaseSysDictionaryInfoService;
import com.base.sys.dict.service.BaseSysDictionaryService;
import com.base.sys.menu.dao.BaseSysAppDao;
import com.base.sys.menu.service.BaseSysAppService;
import com.base.sys.menu.service.BaseSysMenuService;
import com.base.sys.menu.service.BaseSysOperateService;
import com.base.sys.menu.service.BaseSysPermissionService;
import com.base.sys.org.service.BaseSysRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import java.util.*;
import java.util.stream.Collectors;


@Lazy
@Service
public class AsyncCacheServiceImpl implements AsyncCacheService {


    @Autowired
    private BaseSysDictionaryService baseSysDictionaryService;

    @Autowired
    private BaseSysDictionaryInfoService baseSysDictionaryInfoService;


    @Resource
    private BaseSysPermissionService baseSysPermissionService;

    @Autowired
    private BaseSysMenuService baseSysMenuService;

    @Autowired
    private BaseSysOperateService baseSysOperateService;


    @Autowired
    private BaseSysRoleService baseSysRoleService;

    @Autowired
    private BaseSysAppDao baseSysAppDaoImpl;

    @Autowired
    private BaseSysAppService baseSysAppService;




    @Async
    @Override
    public void asyncWriteCache() {

        // 查询字典表 查询路径映射的字典
        Map queryMap = new HashMap();

        queryMap.put("dicCode", "system_operate");

        List<BaseSysDictionary> baseSysDictionaryList = baseSysDictionaryService.queryList(queryMap);

        if (CollectionUtils.isNotEmpty(baseSysDictionaryList)) {
            BaseSysDictionary baseSysDictionary = baseSysDictionaryList.get(0);
            // 查询 dictInfo
            BaseSysDictionaryInfo baseSysDictionaryInfo = new BaseSysDictionaryInfo();
            baseSysDictionaryInfo.setDictId(baseSysDictionary.getId());
            List<BaseSysDictionaryInfo> baseSysDictionaryInfos = baseSysDictionaryInfoService.queryList(baseSysDictionaryInfo);
            Map<String, String> map = new HashMap();
            if (CollectionUtils.isNotEmpty(baseSysDictionaryInfos)) {
                for (BaseSysDictionaryInfo info : baseSysDictionaryInfos) {
                    map.put(info.getDicValue() + info.getDicName(), info.getDicDesc());
                }
            }
            DictMapCache.getCacheMap().put(SysConstant.ESB_PATH_MAPPING, map);
        }
    }


    @Async
    @Override
    public void asyncWriteUserCache() throws DaoException {





        // 查询所有角色
        Map map = new HashMap<>();
        List<BaseSysRole> baseSysUserRoles = baseSysRoleService.queryList(map);
        List<String> roleIds = new ArrayList<>();

        //角色下应用关联的菜单
        Map<String, Map<String, Object>> menuMap = new HashMap<>();
        //角色下应用关联的操作点
        Map<String, Map<String, Object>> operateMap = new HashMap<>();
        //角色下关联的应用
        Map<String, Object> roleAppMap = new HashMap<>();




        if (!CollectionUtils.isEmpty(baseSysUserRoles)) {
            roleIds = baseSysUserRoles.stream().map(BaseSysRole::getId).collect(Collectors.toList());
            List<BaseSysPermission> baseSysPermissions= baseSysPermissionService.selectByRoles(roleIds);
            if(CollectionUtils.isNotEmpty(baseSysPermissions)){
                //按照角色分组
                Map<String, List<BaseSysPermission>> groupByRoleMap = baseSysPermissions.stream().collect(Collectors.groupingBy(BaseSysPermission::getRoleId));

                if(null != groupByRoleMap){
                    Set<String> roleSet = groupByRoleMap.keySet();

                    for(String roleKey:roleSet){
                        //一个角色下的应用菜单授权
                        List<BaseSysPermission> roleBaseSysPermissions = groupByRoleMap.get(roleKey);
                        //将一个角色下的应用菜单授权按照应用进行分组
                        Map<String, List<BaseSysPermission>> groupByAppMap = roleBaseSysPermissions.stream().collect(Collectors.groupingBy(BaseSysPermission::getAppId));
                        if(groupByAppMap !=null){
                            //应用对应的菜单
                            Map<String, Object> appMenuMap = new HashMap<>();
                            Map<String, Object> appOPerateMap = new HashMap<>();
                            List<String> urls = new ArrayList<>();
                            Set<String> appSet = groupByAppMap.keySet();
                            for(String appKey:appSet){
                                List<BaseSysPermission> appBaseSysPermissions = groupByAppMap.get(appKey);
                                roleAppMeun( urls, appBaseSysPermissions, appKey, appMenuMap,appOPerateMap);
                            }
                            menuMap.put(roleKey,appMenuMap);
                            operateMap.put(roleKey,appOPerateMap);
                        }

                        //查询出一个角色下的应用
                        List<String> appIds=roleBaseSysPermissions.stream().map(BaseSysPermission::getAppId).collect(Collectors.toList());

                        List<BaseSysApp> baseSysApps=baseSysAppDaoImpl.selectByIds(appIds);

                        roleAppMap.put(roleKey,baseSysApps);
                    }

                    //存入全局的缓存中

                    UserInfoCache.getCacheMap().put(SysConstant.ROLE_APP, roleAppMap);

                    UserAppCache.getCacheMap().put(SysConstant.ROLE_APP_MENU,menuMap);

                    UserAppCache.getCacheMap().put(SysConstant.ROLE_APP_OPERATE,operateMap);

                }
            }

        }
    }

    private void roleMeun(List urls, Set<String> leafMenuIdSet, List<BaseSysPermission> baseSysPermissionList, List<SysMenuDto> sysMenuDtoList, List<BaseSysOperate> baseSysOperateList) {
        for (BaseSysPermission baseSysPermission : baseSysPermissionList) {
            //菜单id
            String meunId = baseSysPermission.getMenuId();
            //操作点id
            String operateId = baseSysPermission.getOperateId();
            //菜单的对象
            if (!leafMenuIdSet.contains(meunId)) {
                leafMenuIdSet.add(meunId);
                BaseSysMenu menu = baseSysMenuService.getObjectById(meunId);
                if (null != menu) {
                    SysMenuDto sysMenuDto = new SysMenuDto();
                    BeanUtils.copyProperties(menu, sysMenuDto);
                    sysMenuDto.setMenuId(meunId);
                    sysMenuDto.setIcon(menu.getIcon());
                    sysMenuDtoList.add(sysMenuDto);

                }
            }

            //操作点对象
            if (!StringUtils.isEmpty(operateId)) {
                BaseSysOperate baseSysOperate = baseSysOperateService.getObjectById(operateId);
                if (null != baseSysOperate) {
                    urls.add(baseSysOperate.getType() + baseSysOperate.getPath());
                    baseSysOperateList.add(baseSysOperate);
                }
            }
        }
    }








    private void menusAll(List<SysMenuDto> allSysMenuDtoList, List allUrls) throws DaoException {
        //所有的菜单
        List<BaseSysMenu> baseSysMenus = baseSysMenuService.getAllBaseSysMenus();
        if (CollectionUtils.isNotEmpty(baseSysMenus)) {
            for (BaseSysMenu baseSysMenu : baseSysMenus) {
                SysMenuDto sysMenuDto = new SysMenuDto();
                BeanUtils.copyProperties(baseSysMenu, sysMenuDto);
                sysMenuDto.setMenuId(baseSysMenu.getId());
                allSysMenuDtoList.add(sysMenuDto);
                //操作点
                if (SysMenuEnum.LEAF.getCode().equals(sysMenuDto.getIsLeaf())) {
                    //查询下面的操作点
                    List<BaseSysOperate> baseSysOperates = baseSysOperateService.getOperateByMenuId(sysMenuDto.getMenuId());
                    if (CollectionUtils.isNotEmpty(baseSysOperates)) {
                        for (BaseSysOperate baseSysOperate : baseSysOperates) {
                            //用请求方式加路径
                            allUrls.add(baseSysOperate.getType() + baseSysOperate.getPath());
                        }
                    }
                }
            }
        }
    }


    @Async
    @Override
    public void asyncWriteUserDataRule() throws DaoException, ParserConfigurationException {
        // 查询所有角色
//        Map map = new HashMap<>();
//        List<BaseSysRole> baseSysRoles = baseSysRoleService.queryList(map);
//        if (!CollectionUtils.isEmpty(baseSysRoles)) {
//            for (BaseSysRole baseSysRole : baseSysRoles) {
//                List<SysDataRuleDto> sysDataRuleDtoList = new ArrayList<>();
//                BaseSysPermissionData baseSysPermissionData = new BaseSysPermissionData();
//                baseSysPermissionData.setRoleId(baseSysRole.getId());
//                //查询数据权限表
//                List<SysDataRuleDto> sysDataRuleDtos = baseSysPermissionDataService.getPermissionDataByRole(baseSysPermissionData);
//                if (CollectionUtils.isNotEmpty(sysDataRuleDtos)) {
//                    sysDataRuleDtoList.addAll(sysDataRuleDtos);
//                }
//                map.put(baseSysRole.getId(), sysDataRuleDtoList);
//            }
//            DataAuthMapCache.getCacheMap().put(SysConstant.USER_INFO_OWN_DATA_RULE, map);
//        }


       List<SysAppMenuDto>  sysAppMenuDtos = new ArrayList<>();

       List<SysAppMenuDto> menuOperateDtos = new ArrayList<>();

        List<String> allUrls = new ArrayList<>();

       Map appMap = new HashMap();

       Map menuOpMap = new HashMap();

        //查询应用、菜单、操作点、数据规则 表
      List<BaseSysApp> baseSysApps =   baseSysAppService.queryList(new BaseSysApp());

      List<BaseSysMenu> baseSysMenus=baseSysMenuService.getAllBaseSysMenus();

      List<BaseSysOperate> baseSysOperates = baseSysOperateService.queryList(new BaseSysOperate());


      if(CollectionUtils.isNotEmpty(baseSysApps)){
          for (BaseSysApp baseSysApp:baseSysApps){
              SysAppMenuDto sysAppMenuDto = new SysAppMenuDto();
              sysAppMenuDto.setId(baseSysApp.getId());
              sysAppMenuDto.setName(baseSysApp.getName());
              sysAppMenuDto.setType(PermissionTypeEnum.APP_TYPE.getCode());
              sysAppMenuDtos.add(sysAppMenuDto);
          }
       }

        if(CollectionUtils.isNotEmpty(baseSysMenus)){
            for (BaseSysMenu baseSysMenu:baseSysMenus){
                SysAppMenuDto sysAppMenuDto = new SysAppMenuDto();
                sysAppMenuDto.setId(baseSysMenu.getId());
                sysAppMenuDto.setName(baseSysMenu.getText());
                sysAppMenuDto.setType(PermissionTypeEnum.MENU_TYPE.getCode());
                if(StringUtils.isEmpty(baseSysMenu.getParentId())){
                    sysAppMenuDto.setParentId(baseSysMenu.getAppId());
                }else{
                    sysAppMenuDto.setParentId(baseSysMenu.getParentId());
                }
                sysAppMenuDto.setRoutePath(baseSysMenu.getRoutePath());
                sysAppMenuDto.setIcon(baseSysMenu.getIcon());
                sysAppMenuDto.setSort(baseSysMenu.getSort());
                sysAppMenuDtos.add(sysAppMenuDto);
                menuOpMap.put(baseSysMenu.getId(),baseSysMenu.getRoutePath());
            }
        }



        if (CollectionUtils.isNotEmpty(baseSysOperates)) {
            for (BaseSysOperate baseSysOperate : baseSysOperates) {
                SysAppMenuDto sysAppMenuDto = new SysAppMenuDto();
                sysAppMenuDto.setId(baseSysOperate.getId());
                sysAppMenuDto.setCode(baseSysOperate.getCode());
                sysAppMenuDto.setMenuId(baseSysOperate.getMenuId());
                sysAppMenuDto.setRoutePath((String)menuOpMap.get(baseSysOperate.getMenuId()));
                menuOperateDtos.add(sysAppMenuDto);
                //用请求方式加路径
                allUrls.add(baseSysOperate.getType() + baseSysOperate.getPath());
            }
        }

        Map appMenuMap = new HashMap();
        appMenuMap.put(SysConstant.All_APP_MENU,sysAppMenuDtos);
        UserInfoCache.getCacheMap().put(SysConstant.All_APP_MENU, appMenuMap);


        Map allUrlsMap = new HashMap();
        allUrlsMap.put(SysConstant.All_APP_URLS,allUrls);
        UserInfoCache.getCacheMap().put(SysConstant.All_APP_URLS, allUrlsMap);


        Map allOperateMap = new HashMap();
        allOperateMap.put(SysConstant.All_APP_OPERATE,baseSysOperates);
        UserInfoCache.getCacheMap().put(SysConstant.All_APP_OPERATE, allOperateMap);


        Map operateSignMap = new HashMap();
        operateSignMap.put(SysConstant.All_APP_OPERATE_SIGN,menuOperateDtos);
        UserInfoCache.getCacheMap().put(SysConstant.All_APP_OPERATE_SIGN, operateSignMap);




    }



    @Async
    @Override
    public void asyncWriteUserAppCache() throws DaoException {
        // 查询所有角色
        Map map = new HashMap<>();
        List<BaseSysRole> baseSysUserRoles = baseSysRoleService.queryList(map);
        List<String> roleIds = new ArrayList<>();

        //角色下应用关联的菜单
        Map<String, Map<String, Object>> menuMap = new HashMap<>();
        //角色下应用关联的操作点
        Map<String, Map<String, Object>> operateMap = new HashMap<>();
        //角色下关联的应用
        Map<String, Object> roleAppMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(baseSysUserRoles)) {
            roleIds = baseSysUserRoles.stream().map(BaseSysRole::getId).collect(Collectors.toList());
            List<BaseSysPermission> baseSysPermissions= baseSysPermissionService.selectByRoles(roleIds);
            if(CollectionUtils.isNotEmpty(baseSysPermissions)){
                //按照角色分组
                Map<String, List<BaseSysPermission>> groupByRoleMap = baseSysPermissions.stream().collect(Collectors.groupingBy(BaseSysPermission::getRoleId));

                if(null != groupByRoleMap){
                    Set<String> roleSet = groupByRoleMap.keySet();

                    for(String roleKey:roleSet){
                        //一个角色下的应用菜单授权
                        List<BaseSysPermission> roleBaseSysPermissions = groupByRoleMap.get(roleKey);
                        //将一个角色下的应用菜单授权按照应用进行分组
                        Map<String, List<BaseSysPermission>> groupByAppMap = roleBaseSysPermissions.stream().collect(Collectors.groupingBy(BaseSysPermission::getAppId));
                        if(groupByAppMap !=null){
                            //应用对应的菜单
                            Map<String, Object> appMenuMap = new HashMap<>();
                            Map<String, Object> appOPerateMap = new HashMap<>();
                            List<String> urls = new ArrayList<>();
                            Set<String> appSet = groupByAppMap.keySet();
                            for(String appKey:appSet){
                                List<BaseSysPermission> appBaseSysPermissions = groupByAppMap.get(roleKey);
                                roleAppMeun( urls, appBaseSysPermissions, appKey, appMenuMap,appOPerateMap);
                            }
                            menuMap.put(roleKey,appMenuMap);
                            operateMap.put(roleKey,appOPerateMap);
                        }

                        //查询出一个角色下的应用
                        List<String> appIds=roleBaseSysPermissions.stream().map(BaseSysPermission::getAppId).collect(Collectors.toList());

                        List<BaseSysApp> baseSysApps=baseSysAppDaoImpl.selectByIds(appIds);

                        roleAppMap.put(roleKey,baseSysApps);
                    }

                    //存入全局的缓存中

                    UserInfoCache.getCacheMap().put(SysConstant.ROLE_APP, roleAppMap);

                    UserAppCache.getCacheMap().put(SysConstant.ROLE_APP_MENU,menuMap);

                    UserAppCache.getCacheMap().put(SysConstant.ROLE_APP_OPERATE,operateMap);
                }

            }
        }
    }





    private void roleAppMeun(List urls, List<BaseSysPermission> baseSysPermissionList,String appId, Map<String, Object> appMenuMap,Map<String, Object> appOPerateMap) {

        List<SysMenuDto> sysMenuDtoList = new ArrayList<>();

        List<BaseSysOperate> baseSysOperateList = new ArrayList<>();

        Set<String> leafMenuIdSet = new HashSet<>();

        for (BaseSysPermission baseSysPermission : baseSysPermissionList) {
            //菜单id
            String meunId = baseSysPermission.getMenuId();
            //操作点id
            String operateId = baseSysPermission.getOperateId();

            //菜单的对象
            if (!leafMenuIdSet.contains(meunId)) {
                leafMenuIdSet.add(meunId);
                BaseSysMenu menu = baseSysMenuService.getObjectById(meunId);
                if (null != menu) {
                    SysMenuDto sysMenuDto = new SysMenuDto();
                    BeanUtils.copyProperties(menu, sysMenuDto);
                    sysMenuDto.setMenuId(meunId);
                    sysMenuDto.setIcon(menu.getIcon());
                    sysMenuDtoList.add(sysMenuDto);
                }
            }
            //操作点对象
            if (!StringUtils.isEmpty(operateId)) {
                BaseSysOperate baseSysOperate = baseSysOperateService.getObjectById(operateId);
                if (null != baseSysOperate) {
                    urls.add(baseSysOperate.getType() + baseSysOperate.getPath());
                    baseSysOperateList.add(baseSysOperate);
                }
            }
        }
        appMenuMap.put(appId,sysMenuDtoList);
        appOPerateMap.put(appId,baseSysOperateList);
    }









}
