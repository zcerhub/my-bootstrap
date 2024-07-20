package com.base.core.dao.impl;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import com.base.api.dto.Page;
import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.BaseDao;
import com.base.core.utils.GenericsUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author keriezhang
 * @date 2021/04/25
 */
public abstract class AbstractBaseDaoImpl<T, ID extends Serializable> extends SqlSessionDaoSupport implements BaseDao<T, ID> {

    private String schema;

    public String getSchema() {
        return schema;
    }

    @Autowired(required = false)
    @Qualifier("sqlSessionFactory")
    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (sqlSessionFactory.getConfiguration().getVariables().get("schema") != null) {
            this.schema = (String) sqlSessionFactory.getConfiguration().getVariables().get("schema");
        }
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    protected String getSqlNamespace() {
        Class<T> clazz = (Class) GenericsUtils.getSuperClassGenricType(this.getClass());
        String nameSpaceNew = clazz.getName();
        return nameSpaceNew;
    }

    @Override
    public List queryList(Map map) throws DaoException {
        List result;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.QueryList.getCode(), map);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.QueryList.getName() + ":" + SqlSessionMapperLabel.QueryList.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Page queryPage(int pageNo, int pageSize, Map map) throws DaoException {
        Page result = new Page();
        try {
            PageHelper.startPage(pageNo, pageSize);
            List list = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.QueryPage.getCode(), map);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(list);
            result.setPageNo(pageInfo.getPageNum());
            result.setPageSize(pageInfo.getPageSize());
            result.setTotalCount((int) pageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.QueryPage.getName() + ":" + SqlSessionMapperLabel.QueryPage.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Page queryPage(int pageNo, int pageSize, T po) throws DaoException {
        Page result = new Page();
        try {
            PageHelper.startPage(pageNo, pageSize);
            List list = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.QueryPage.getCode(), po);
            PageInfo pageInfo = new PageInfo(list);
            result.setData(list);
            result.setPageNo(pageInfo.getPageNum());
            result.setPageSize(pageInfo.getPageSize());
            result.setTotalCount((int) pageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.QueryPage.getName() + ":" + SqlSessionMapperLabel.QueryPage.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public T getObjectByMap(Map map) throws DaoException {
        T result = null;
        try {
            List list = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.GetObjectByMap.getCode(), map);
            if (!list.isEmpty()) {
                result = (T) list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.GetObjectByMap.getName() + ":" + SqlSessionMapperLabel.GetObjectByMap.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public T getObjectById(ID id) throws DaoException {
        T result = null;
        try {
            result = this.getSqlSession().selectOne(getSqlNamespace() + "." + SqlSessionMapperLabel.GetObjectById.getCode(), id);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.GetObjectById.getName() + ":" + SqlSessionMapperLabel.GetObjectById.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer delObjectById(ID id) throws DaoException {
        Integer result = 0;
        try {
            result = this.getSqlSession().delete(getSqlNamespace() + "." + SqlSessionMapperLabel.DelObjectById.getCode(), id);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.DelObjectById.getName() + ":" + SqlSessionMapperLabel.DelObjectById.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer saveObject(T obj) throws DaoException {
        Integer result = 0;
        try {
            result = this.getSqlSession().insert(getSqlNamespace() + "." + SqlSessionMapperLabel.SaveObject.getCode(), obj);
        } catch (Exception e) {
            logger.error(e);
            String msg = SqlSessionMapperLabel.SaveObject.getName() + ":" + SqlSessionMapperLabel.SaveObject.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer updateObject(T obj) throws DaoException {
        Integer result = 0;
        try {
            result = this.getSqlSession().update(getSqlNamespace() + "." + SqlSessionMapperLabel.UpdateObject.getCode(), obj);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.UpdateObject.getName() + ":" + SqlSessionMapperLabel.UpdateObject.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer updateStatus(T obj) throws DaoException {
        Integer result = 0;
        try {
            result = this.getSqlSession().update(getSqlNamespace() + "." + SqlSessionMapperLabel.UpdateStatus.getCode(), obj);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.UpdateStatus.getName() + ":" + SqlSessionMapperLabel.UpdateStatus.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer delObjectByIds(List<ID> ids) throws DaoException {
        Integer result = 0;
        try {
            result = this.getSqlSession().update(getSqlNamespace() + "." + SqlSessionMapperLabel.DelObjectByIds.getCode(), ids);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.DelObjectByIds.getName() + ":" + SqlSessionMapperLabel.DelObjectByIds.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer selectTotal(Map map) throws DaoException {
        Integer result = 0;
        try {
            List<Integer> objects = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.SelectTotal.getCode(), map);
            if (objects != null) {
                result = objects.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.SelectTotal.getName() + ":" + SqlSessionMapperLabel.SelectTotal.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public Integer deleteAttributeData(Map map) throws DaoException {
        Integer result = 0;
        try {
            result = this.getSqlSession().delete(getSqlNamespace() + "." + SqlSessionMapperLabel.DeleteAttributeData.getCode(), map);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.DeleteAttributeData.getName() + ":" + SqlSessionMapperLabel.DeleteAttributeData.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public List<T> queryList(T po) throws DaoException {
        List<T> result;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.QueryList.getCode(), po);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.QueryList.getName() + ":" + SqlSessionMapperLabel.QueryList.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }
    
    @Override
    public List<T> queryList(String statement , T po , int offset, int limit) throws DaoException {
        List<T> result;
        try {
        	RowBounds rowBounds = new RowBounds(offset, limit);
            result = this.getSqlSession().selectList(getSqlNamespace() + "." + statement, po , rowBounds);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.QueryList.getName() + ":" + SqlSessionMapperLabel.QueryList.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public List<T> queryListByPo(T po) throws DaoException {
        List<T> result;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.QueryListByPo.getCode(), po);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.QueryListByPo.getName() + ":" + SqlSessionMapperLabel.QueryListByPo.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }


    @Override
    public List<String> getIds(Map map) throws DaoException {
        List<String> list = null;
        try {
            list = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.GetIds.getCode(), map);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.GetIds.getName() + ":" + SqlSessionMapperLabel.GetIds.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return list;
    }

    /**
     * 添加批量新增
     *
     * @param map
     * @return
     * @throws DaoException
     */
    @Override
    public Integer InsertBatch(Map map) throws DaoException {
        Integer result = 0;
        try {
            result = this.getSqlSession().insert(getSqlNamespace() + "." + SqlSessionMapperLabel.InsertBatch.getCode(), map);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.InsertBatch.getName() + ":" + SqlSessionMapperLabel.InsertBatch.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public T selectForUpdate(T po) throws DaoException {
        T result = null;
        try {
            result = this.getSqlSession().selectOne(getSqlNamespace().concat(".selectForUpdate"), po);
        } catch (Exception e) {
            throw new DaoException("selectForUpdate" + e.getMessage());
        }
        return result;
    }

    @Override
    public List queryListByMap(Map map) throws DaoException {
        List result;
        try {
            result = this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.QueryListByMap.getCode(), map);
        } catch (Exception e) {
            String msg = SqlSessionMapperLabel.QueryListByMap.getName() + ":" + SqlSessionMapperLabel.QueryListByMap.getCode() + e.getMessage();
            throw new DaoException(msg);
        }
        return result;
    }

    @Override
    public List<T> queryListByIds(Set<ID> ids) throws DaoException {
        if (CollectionUtils.isEmpty(ids)) {
            throw new DaoException("queryListByIds ids can not be empty!");
        }
        try {
            return this.getSqlSession().selectList(getSqlNamespace() + "." + SqlSessionMapperLabel.QueryListByIds.getCode(), ids);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = SqlSessionMapperLabel.QueryListByIds.getName() + ":" + SqlSessionMapperLabel.QueryListByIds.getCode() + Arrays.toString(e.getStackTrace());
            throw new DaoException(msg);
        }
    }

}
