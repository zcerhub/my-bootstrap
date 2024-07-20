package com.base.core.dao;


import com.base.api.dto.Page;
import com.base.api.exception.DaoException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseDao<T, Id extends Serializable> {

    /**
     * 查询数据集合
     *
     * @param map
     * @return
     */
    List<T> queryList(Map map) throws DaoException;

    /**
     * 查询分页数据数据
     *
     * @param map
     * @return
     */
    Page queryPage(int pageNo, int pageSize, Map map) throws DaoException;

    /**
     * 分页查询所有匹配
     *
     * @param pageNo   当前页
     * @param pageSize 页数量
     * @param po       po
     * @return PageResult
     */
    Page queryPage(int pageNo, int pageSize, T po) throws DaoException;

    /**
     * 通过Map单个数据对象
     *
     * @param map
     * @return
     */
    T getObjectByMap(Map map) throws DaoException;

    /**
     * 通过Id查询
     *
     * @param id
     * @return
     */
    T getObjectById(Id id) throws DaoException;

    /**
     * 根据Id删除
     *
     * @param id
     * @return
     */
    Integer delObjectById(Id id) throws DaoException;

    /**
     * 批量删除
     *
     * @param ids
     * @return
     * @throws DaoException
     */
    Integer delObjectByIds(List<Id> ids) throws DaoException;

    /**
     * 保存数据
     *
     * @param obj
     * @return
     */
    Integer saveObject(T obj) throws DaoException;

    /**
     * 保存数据
     *
     * @param map
     * @return
     */
    Integer InsertBatch(Map map) throws DaoException;

    /**
     * 更新数据
     *
     * @param obj
     * @return
     */
    Integer updateObject(T obj) throws DaoException;


    /**
     * 更新状态
     *
     * @param obj
     * @return
     */
    Integer updateStatus(T obj) throws DaoException;

    /**
     * 根据map查询数据条数
     *
     * @param map
     * @return
     * @throws DaoException
     */
    Integer selectTotal(Map map) throws DaoException;


    /**
     * 根据map删除数据
     *
     * @param map
     * @return
     * @throws DaoException
     */
    Integer deleteAttributeData(Map map) throws DaoException;

    List<T> queryList(T po) throws DaoException;

    List<T> queryListByPo(T po) throws DaoException;

    List<String> getIds(Map map) throws DaoException;

    /**
     * 查询加锁
     *
     * @param po
     * @return 单个对象
     */
    T selectForUpdate(T po) throws DaoException;

    /**
     * 查询数据集合
     *
     * @param map
     * @return
     */
    List<T> queryListByMap(Map map) throws DaoException;

    List<T> queryListByIds(Set<Id> ids) throws DaoException;

	List<T> queryList(String statement, T po, int offset, int limit) throws DaoException;

}
