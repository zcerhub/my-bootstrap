package com.base.sys.menu.service.impl;

import com.base.api.exception.DaoException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.api.entity.BaseSysOperate;
import com.base.sys.api.entity.BaseSysPermission;
import com.base.sys.menu.dao.BaseSysMenuDao;
import com.base.sys.menu.dao.BaseSysOperateDao;
import com.base.sys.menu.service.BaseSysOperateService;
import com.base.sys.menu.service.BaseSysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Lazy
@Service
public class BaseSysOperateServiceImpl extends AbstractBaseServiceImpl<BaseSysOperate, String> implements BaseSysOperateService {


    @Resource
    private BaseSysOperateDao baseSysOperateDaoImpl;
    @Resource
    private BaseSysPermissionService baseSysPermissionService;

    @Autowired
    private BaseSysMenuDao baseSysMenuDao;

    @Override
    public List<BaseSysOperate> getOperateByMenuId(String menuId) throws DaoException {
        List<BaseSysOperate>  list ;
        Map  map =new HashMap();
        map.put("menuId",menuId);
        list =  this.getBaseDao().queryList(map);
        return list;
    }

    @Override
    public List<BaseSysOperate> queryListByRole(Map map) throws DaoException {
        List<BaseSysOperate> result = new ArrayList<>();
        List<BaseSysPermission> permissionList = baseSysPermissionService.queryList(map);
        if (permissionList == null){
            return null;
        }
        for (BaseSysPermission sysPermission: permissionList){
            result.add(this.getObjectById(sysPermission.getOperateId()));
        }
        return result;
    }
    @Override
    public List<TreeNodeData>  queryIdAndName(String appId, String tenantId){
        List<TreeNodeData> treeNodeDataList = new ArrayList<>();
        Map<String,String>  sqlMap = new HashMap<>();
        try {
            sqlMap.put("appId",appId);
            sqlMap.put("tenantId",tenantId);
            List<String> appMenuId = baseSysMenuDao.queryIdByApp(sqlMap);
            treeNodeDataList = queryMenuOperate(appMenuId,tenantId,appId);
           return treeNodeDataList;
        }catch (Exception e){
            e.printStackTrace();;
            return treeNodeDataList;
        }

    }

    public List<TreeNodeData> queryMenuOperate(List<String> menuIdList,String tenantId,String appId){
        List<TreeNodeData> operateList = new ArrayList<>();
        try {
            for (String menuId: menuIdList){
                Map<String,String> sqlMap = new HashMap<>();
                sqlMap.put("menuId",menuId);
                sqlMap.put("tenantId",tenantId);
                List<TreeNodeData>  menuOperate=baseSysOperateDaoImpl.queryMenuOperate(sqlMap);
                operateList.addAll(menuOperate);
            }
        //添加appId和前缀，用于前端区分
        for (int i = 0;i<operateList.size();i++){
            TreeNodeData treeNodeData =  operateList.get(i);
            treeNodeData.setAppId(appId);
            treeNodeData.setId("o_"+ operateList.get(i).getId());
            treeNodeData.setMenuId("m_"+operateList.get(i).getMenuId());
            operateList.set(i,treeNodeData);
        }

        return operateList;
        }catch (Exception e){
            e.printStackTrace();
            return operateList;
        }

    }

}
