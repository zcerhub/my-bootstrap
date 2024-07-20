package com.base.sys.menu.dao.impl;

import com.base.api.exception.DaoException;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.dto.TreeNodeData;
import com.base.sys.api.entity.BaseSysMenu;
import com.base.sys.menu.dao.BaseSysMenuDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author QuJing
 * @date 2021/1/15 17:46
 */
@Repository
public class BaseSysMenuDaoImpl  extends AbstractBaseDaoImpl<BaseSysMenu,String> implements BaseSysMenuDao {


    @Override
    public List<BaseSysMenu> queryListByAttribute(BaseSysMenu po) throws DaoException {
        List<BaseSysMenu> result;
        try {
            result= getSqlSession().selectList(getSqlNamespace().concat(".queryListByAttribute"), po);
        }catch (Exception e){
            throw new DaoException("queryListByAttribute执行异常," + ResultEnum.FAILED.getMsg());
        }
        return result;
    }






    /**
     * 权限新增进行唯一性验证
     *
     * @param map 菜单对象
     * @return 返回当前新增菜单的个数
     * @throws DaoException
     */
    @Override
    public Integer selectMenuList(Map map) throws DaoException {
        Integer result;
        try {
            result =this.getSqlSession().selectOne(getSqlNamespace().concat(".selectMenuList"),map);
        }catch (Exception e){
            throw new DaoException("selectMenuList执行异常"+ResultEnum.FAILED.getMsg());
        }
        return result;
    }

    @Override
    public  List<TreeNodeData> queryIdAndName(Map<String,String> map) throws DaoException{
        List<TreeNodeData> result = new ArrayList<>();
        try {
            List<TreeNodeData> sqlResult= getSqlSession().selectList(getSqlNamespace().concat(".queryIdAndName"), map);
            for (TreeNodeData nodeData : sqlResult){
                String id = "m_" + nodeData.getId();
                String parentId = "m_" + nodeData.getParentId();
                nodeData.setId(id);
                nodeData.setParentId(parentId);
                result.add(nodeData);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new DaoException("queryIdAndName执行异常," + ResultEnum.FAILED.getMsg());
        }
        return result;
    }

    @Override
    public List<String> queryIdByApp(Map<String,String> map) throws DaoException{
        List<String> result;
        try {
            result= getSqlSession().selectList(getSqlNamespace().concat(".queryIdByApp"), map);
        }catch (Exception e){
            throw new DaoException("queryIdByApp执行异常," + ResultEnum.FAILED.getMsg());
        }
        return result;
    }
    @Override
    public List<BaseSysMenu> queryListByIds(List<String> ids) throws DaoException{
        List<BaseSysMenu> result;
        Map<String,Object> sqlMap = new HashMap<>();
        sqlMap.put("ids",ids);
        try {
            result= getSqlSession().selectList(getSqlNamespace().concat(".queryListByIds"), sqlMap);
        }catch (Exception e){
            throw new DaoException("queryListByIds执行异常," + ResultEnum.FAILED.getMsg());
        }
        return result;
    }

}
