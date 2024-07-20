package com.base.sys.menu.service.impl;

import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysDataRule;
import com.base.sys.menu.dao.BaseSysDataRuleDao;
import com.base.sys.menu.dao.BaseSysMenuDao;
import com.base.sys.menu.service.BaseSysDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BaseSysDataRuleServiceImpl extends AbstractBaseServiceImpl<BaseSysDataRule, String> implements BaseSysDataRuleService {

    @Autowired
    private BaseSysMenuDao baseSysMenuDao;

    @Autowired
    private BaseSysDataRuleDao baseSysDataRuleDao;

    @Override
    public List<BaseSysDataRule> getSysDataListByMenuId(String menuId) throws ServiceException, DaoException {
        Map map = new HashMap();
        map.put("menuId",menuId);

        List<BaseSysDataRule> list = this.getBaseDao().queryList(map);
        return list;
    }
    @Override
    public List<TreeNodeData>   queryIdAndName(String appId, String tenantId){
        List<TreeNodeData> treeNodeDataList = new ArrayList<>();
        Map<String,String>  sqlMap = new HashMap<>();
        try {
            sqlMap.put("appId",appId);
            sqlMap.put("tenantId",tenantId);
            List<String> appMenuId = baseSysMenuDao.queryIdByApp(sqlMap);
            treeNodeDataList=queryMenuDataRule(appMenuId,tenantId,appId);
            return treeNodeDataList;
        }catch (Exception e){
            e.printStackTrace();;
            return treeNodeDataList;
        }
    }

    public List<TreeNodeData> queryMenuDataRule(List<String> menuIdList,String tenantId,String appId){
        List<TreeNodeData> dataRuleList = new ArrayList<>();
        try {
            for (String menuId: menuIdList){
                Map<String,String> sqlMap = new HashMap<>();
                sqlMap.put("menuId",menuId);
                sqlMap.put("tenantId",tenantId);
                List<TreeNodeData>  menuOperate=baseSysDataRuleDao.queryMenuDataRule(sqlMap);
                dataRuleList.addAll(menuOperate);
            }
            //添加appId和前缀，用于前端区分
            for (int i = 0;i<dataRuleList.size();i++){
                TreeNodeData treeNodeData =  dataRuleList.get(i);
                treeNodeData.setAppId(appId);
                treeNodeData.setId("d_"+ dataRuleList.get(i).getId());
                treeNodeData.setMenuId("m_"+dataRuleList.get(i).getMenuId());
                dataRuleList.set(i,treeNodeData);
            }

            return dataRuleList;
        }catch (Exception e){
            e.printStackTrace();
            return dataRuleList;
        }

    }
}
