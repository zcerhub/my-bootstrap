package com.base.sys.org.service.impl;

import com.base.api.exception.DaoException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.cache.DataAuthMapCache;
import com.base.sys.api.cache.UserInfoCache;
import com.base.sys.api.common.SysConstant;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.api.dto.SysAppMenuDto;
import com.base.sys.api.dto.SysDataRuleDto;
import com.base.sys.api.dto.SysOperateSign;
import com.base.sys.api.entity.*;
import com.base.sys.menu.dao.BaseSysPermissionDao;
import com.base.sys.menu.service.BaseSysPermissionDataService;
import com.base.sys.menu.service.BaseSysPermissionService;
import com.base.sys.org.service.AuthenticationUserService;
import com.base.sys.org.service.BaseSysUserRoleService;
import com.base.sys.org.service.BaseSysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
class AuthenticationUserServiceImpl extends AbstractBaseServiceImpl<BaseSysUser, String> implements AuthenticationUserService {

    private static final Logger log = LoggerFactory.getLogger(
            AuthenticationUserServiceImpl.class);
    @Lazy
    @Autowired
    private BaseSysUserService baseSysUserService;
    @Lazy
    @Autowired
    private BaseSysUserRoleService baseSysUserRoleService;

    @Lazy
    @Resource
    private BaseSysPermissionService baseSysPermissionService;

    @Autowired
    private BaseSysPermissionDataService baseSysPermissionDataService;

    @Override
    public AuthenticationUserDto getUserByLoginName(String loginName,String tenantId) {

        AuthenticationUserDto authenticationUserDTo = new AuthenticationUserDto();
        try {
            //得到用户名和密码
            BaseSysUser baseSysUser = baseSysUserService.getByAccount(loginName);
            if (null == baseSysUser) {
                return authenticationUserDTo;
            }
            String userId = baseSysUser.getId();
            //用户账号
            authenticationUserDTo.setAccount(baseSysUser.getAccount());
            //密码
            authenticationUserDTo.setPassword(baseSysUser.getPassword());
            //用户id
            authenticationUserDTo.setUserId(userId);
            //用户名
            authenticationUserDTo.setUserName(baseSysUser.getName());

            List<BaseSysOperate>  baseSysOperates=new ArrayList<>();

            List<SysAppMenuDto> operateSigns = new ArrayList<>();

            List<String> allUrls = new ArrayList<>();


            //页面下的操作点
            List<SysOperateSign> sysOperateSignList = new ArrayList<>();


            // 根据用户id得到 角色
            Map map = new HashMap<>();
            map.put("userId", userId);
            List<BaseSysUserRole> baseSysUserRoles = baseSysUserRoleService.queryList(map);
            if (CollectionUtils.isEmpty(baseSysUserRoles)) {
                return authenticationUserDTo;
            }


            Map<String,Object> appMenuMap = (Map<String,Object>)UserInfoCache.get(SysConstant.All_APP_MENU);
            if(null == appMenuMap){
                return authenticationUserDTo;
            }

            Map<String,Object> operateMap = (Map<String,Object>)UserInfoCache.get(SysConstant.All_APP_OPERATE);

            Map<String,Object> allUrlsMap = (Map<String,Object>)UserInfoCache.get(SysConstant.All_APP_URLS);

            Map<String,Object>  operateSignMap=(Map<String,Object>)UserInfoCache.get(SysConstant.All_APP_OPERATE_SIGN);

            if(null != allUrlsMap){
                allUrls= (List<String>)allUrlsMap.get(SysConstant.All_APP_URLS);
                authenticationUserDTo.setAllUrls(allUrls);
            }

           //从缓存中获取所有的菜单
            List<SysAppMenuDto>  sysAppMenuDtos= (List<SysAppMenuDto>)appMenuMap.get(SysConstant.All_APP_MENU);
            if (CollectionUtils.isEmpty(sysAppMenuDtos)) {
                return authenticationUserDTo;
            }

            if(null != operateMap){
                baseSysOperates= (List<BaseSysOperate>)operateMap.get(SysConstant.All_APP_OPERATE);
            }

            if(null != operateSignMap){
                operateSigns= (List<SysAppMenuDto>)operateSignMap.get(SysConstant.All_APP_OPERATE_SIGN);
            }

            // 根据用户查询权限表
            Map userMap = new HashMap();
            userMap.put("userId",baseSysUser.getId());
            List<BaseSysPermission> baseSysPermissions=  baseSysPermissionService.selectByUser(userMap);
            if (CollectionUtils.isEmpty(baseSysPermissions)) {
                return authenticationUserDTo;
            }
            Set<String>  authOpreateSet = new HashSet<>();
            for (BaseSysPermission baseSysPermission : baseSysPermissions){
                if (baseSysPermission.getOperateId()!=null){
                    authOpreateSet.add(baseSysPermission.getOperateId());
                }
            }
            List<SysAppMenuDto> ownSysAppMenuDtos = new ArrayList<>();

            Set appSet = new HashSet();

            Set menuSet = new HashSet();

            Set operateSet = new HashSet();

            Set<String> set = new HashSet<>();

            for(BaseSysPermission permission:baseSysPermissions){

                if(!appSet.contains(permission.getAppId())){
                    appSet.add(permission.getAppId());
                    SysAppMenuDto sysAppMenuDto = new SysAppMenuDto();
                    sysAppMenuDto.setId(permission.getAppId());
                    ownSysAppMenuDtos.add(sysAppMenuDto);

                    set.add(permission.getAppId());
                }
                if(!menuSet.contains(permission.getMenuId())){
                    appSet.add(permission.getMenuId());
                    SysAppMenuDto sysAppMenuDto = new SysAppMenuDto();
                    sysAppMenuDto.setId(permission.getMenuId());
                    ownSysAppMenuDtos.add(sysAppMenuDto);
                    set.add(permission.getMenuId());
                }

                if(!StringUtils.isEmpty(permission.getOperateId())){
                    operateSet.add(permission.getOperateId());
                }

            }


            List<String> operateStrs = new ArrayList(operateSet);

            //自己的url

            List<String> urls = new ArrayList<>();

            Map<String,Object> userAppMenuMap = new HashMap<>();
            List<String> strs = new ArrayList(set);

            List<BaseSysOperate> ownBaseSysOperateList = baseSysOperates.stream().filter(baseSysOperate -> operateStrs.contains(baseSysOperate.getId())).collect(Collectors.toList());

            for (BaseSysOperate baseSysOperate : ownBaseSysOperateList) {
                //用请求方式加路径
                urls.add(baseSysOperate.getType() + baseSysOperate.getPath());
            }
            authenticationUserDTo.setUrls(urls);


            List<SysAppMenuDto> sysAppMenuDtoList = sysAppMenuDtos.stream().filter(sysAppMenuDto -> strs.contains(sysAppMenuDto.getId())).collect(Collectors.toList());

            List<SysAppMenuDto> sysAppMenuDtoTreeList = new ArrayList<>();



            // 整理对象,生成树
            if (CollectionUtils.isNotEmpty(sysAppMenuDtoList)) {
                for (int i = 0; i < sysAppMenuDtoList.size(); i++) {
                    // 一级菜单没有parentId
                    if (StringUtils.isEmpty(sysAppMenuDtoList.get(i).getParentId())) {
                        sysAppMenuDtoTreeList.add(sysAppMenuDtoList.get(i));
                    }
                }
                for (SysAppMenuDto sysAppMenuDto : sysAppMenuDtoTreeList) {
                    sysAppMenuDto.setChildren(getAppMenuChild(sysAppMenuDto.getId(), sysAppMenuDtoList));
                }
            }


            for(SysAppMenuDto sysAppMenuDto:sysAppMenuDtoTreeList){
                userAppMenuMap.put(sysAppMenuDto.getId(),sysAppMenuDto);
            }
              //该用户下所有 所有应用及关联的 菜单
            authenticationUserDTo.setUserAppMenuMap(userAppMenuMap);

            if(CollectionUtils.isNotEmpty(operateSigns)){
                Map<String, List<SysAppMenuDto>> mapBymenuId = operateSigns.stream().collect(Collectors.groupingBy(SysAppMenuDto::getMenuId));
               if(null !=mapBymenuId ){
                   for(String menuId :mapBymenuId.keySet()){
                       List<SysAppMenuDto> dtos = mapBymenuId.get(menuId);
                       if(CollectionUtils.isNotEmpty(dtos)){
                           List<String> codeList= new ArrayList<>();
                           SysOperateSign sysOperateSign = new SysOperateSign();
                           for (SysAppMenuDto dto:dtos){
                               if (authOpreateSet.contains(dto.getId())) {
                                   codeList.add(dto.getCode());
                               }
                           }
                           sysOperateSign.setCodeList(codeList);
                           sysOperateSign.setRoutePath(dtos.get(0).getRoutePath());
                           sysOperateSignList.add(sysOperateSign);
                       }
                   }
               }
            }
            setDataPermission(baseSysUserRoles,  authenticationUserDTo);
            authenticationUserDTo.setSysOperateSignList(sysOperateSignList);

        } catch (Exception e) {
            log.error("查询失败", e);
        }

        return authenticationUserDTo;
    }

    /**
     * 查询角色下面的数据权限
     *
     * @param baseSysUserRoles
     */
    private void setDataPermission(List<BaseSysUserRole> baseSysUserRoles, AuthenticationUserDto authenticationUserDTo) throws DaoException {

        Map<String,Object>  dataRuleMap= DataAuthMapCache.get(SysConstant.USER_INFO_OWN_DATA_RULE);

        List<SysDataRuleDto> baseSysDataRules = new ArrayList<>();
        Map<String,String> dataPermessionMap = new HashMap<>();

        if(dataRuleMap != null){
            for (BaseSysUserRole baseSysUserRole : baseSysUserRoles) {
                List list = (List<SysDataRuleDto>) dataRuleMap.get(baseSysUserRole.getRoleId());
                if (CollectionUtils.isNotEmpty(list)) {
                    baseSysDataRules.addAll(list);
                }
            }

        }else{
            //该用户下所有的数据授权信息
            BaseSysPermissionData baseSysPermissionData = new BaseSysPermissionData();
            for (BaseSysUserRole baseSysUserRole : baseSysUserRoles) {
                baseSysPermissionData.setRoleId(baseSysUserRole.getRoleId());
                //查询数据权限表
                List<SysDataRuleDto> sysDataRuleDtos = baseSysPermissionDataService.getPermissionDataByRole(baseSysPermissionData);
                if (CollectionUtils.isNotEmpty(sysDataRuleDtos)) {
                    baseSysDataRules.addAll(sysDataRuleDtos);
                }
            }
        }

        //去掉重复后的数据规则数据
        List<SysDataRuleDto>   sysDataRuleReDtos = baseSysDataRules.stream().filter(distinctByKey(SysDataRuleDto::getId)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(sysDataRuleReDtos)){
            for(SysDataRuleDto dataRule : sysDataRuleReDtos){
                dataPermessionMap.put(dataRule.getPath(),dataRule.getRule());
            }
        }

        authenticationUserDTo.setDataPermessionList(dataPermessionMap);
    }

    public List<SysAppMenuDto> getAppMenuChild(String id, List<SysAppMenuDto> allSysAppMenuDto) {
        // 子菜单
        List<SysAppMenuDto> childList = new ArrayList<>();
        for (SysAppMenuDto sysAppMenuDto : allSysAppMenuDto) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (!StringUtils.isEmpty(sysAppMenuDto.getParentId())) {
                if (sysAppMenuDto.getParentId().equals(id)) {
                    childList.add(sysAppMenuDto);
                }
            }
        }

        // 把子菜单的子菜单再循环一遍
        for (SysAppMenuDto sysAppMenuDto : childList) {
            if (!StringUtils.isEmpty(sysAppMenuDto.getParentId())) {
                // 递归
                sysAppMenuDto.setChildren(getAppMenuChild(sysAppMenuDto.getId(), allSysAppMenuDto));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }









    /***
     * 根据对象的某个属性去重
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;

    }


    /**
     * 去重
     * @param list
     * @return
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<String>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    }
