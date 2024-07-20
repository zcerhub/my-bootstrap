package com.base.core.service;


import com.base.api.dto.Page;
import com.base.api.entity.Base;
import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseService<T extends Base, ID extends Serializable> {

    /**
     * 查询数据集合
     *
     * @param map
     * @return
     */
    List<T> queryList(Map map) throws ServiceException;


    List<String> getIds(Map map) throws ServiceException;

    /**
     * 通过Id查询
     *
     * @param po T
     * @return List
     */
    List<T> queryList(T po) throws ServiceException;

    /**
     * 查询分页数据数据
     *
     * @param map
     * @return
     */
    Page<T> queryPage(int pageNo, int pageSize, Map map) throws ServiceException;

    /**
     * 查询分页数据数据
     *
     * @param pageNo   当前页
     * @param pageSize 页数量
     * @param po       po
     * @return
     */
    Page<T> queryPage(int pageNo, int pageSize, T po) throws ServiceException;

    /**
     * 通过Map单个数据对象
     *
     * @param map
     * @return
     */
    T getObjectByMap(Map map) throws ServiceException;

    /**
     * 通过Id查询
     *
     * @param id
     * @return
     */
    T getObjectById(ID id) throws ServiceException;

    /**
     * 根据Ids批量删除
     *
     * @param ids
     * @return
     */
    Integer delObjectByIds(List<ID> ids) throws ServiceException;

    /**
     * 根据Id删除
     *
     * @param id
     * @return
     */
    Integer delObjectById(ID id) throws ServiceException;

    /**
     * 保存数据
     *
     * @param obj
     * @return
     */
    Integer saveObject(T obj) throws ServiceException;

    /**
     * 更新数据
     *
     * @param obj
     * @return
     */
    Integer updateObject(T obj) throws ServiceException;

    /**
     * 更新状态
     *
     * @param obj
     * @return
     */
    Integer updateStatus(T obj) throws ServiceException;


    /**
     * 根据条件查询 表中是否包含数据
     *
     * @param map map中包含三个参数
     *            key ：table           value ：表明
     *            key ：attributeName   value ：字段名称
     *            key ：attributeValue  value ：字段属性值
     * @return
     * @throws ServiceException 使用方法： 需要在对应的xml文件中添加如下代码
     *                          <p>
     *                          <p>
     *                          <!-- 根据实体名称和字段名称和字段值获取唯一记录 -->
     *                          <select id="selectTotal"  resultType="java.lang.Integer" statementType="STATEMENT">
     *                          select count(1) FROM ${table}  where ${attributeName} = '${attributeValue}'
     *                          </select>
     */
    Integer selectTotal(Map<String, Object> map) throws ServiceException;


    /**
     * 根据条件删除数据
     *
     * @param map map中包含三个参数
     *            key ：table           value ：表明
     *            key ：attributeName   value ：字段名称
     *            key ：attributeValue  value ：字段属性值
     * @return
     * @throws ServiceException 使用方法： 需要在对应的xml文件中添加如下代码
     *                          <p>
     *                          <p>
     *                          <!-- 根据表明和属性删除数据 -->
     *                          <select id="deleteAttributeData"  resultType="java.lang.Integer" statementType="STATEMENT">
     *                          delete FROM ${table}  where ${attributeName} = '${attributeValue}'
     *                          </select>
     */
    Integer deleteAttributeData(Map<String, String> map) throws ServiceException;

    /**
     * 查询数据集合
     *
     * @param map
     * @return
     */
    List<T> queryListByMap(Map map) throws ServiceException;

    /**
     * 根据id批量查询
     *
     * @param ids id
     * @return 根据id批量查询
     * @throws DaoException 异常
     */
    List<T> queryListByIds(Set<ID> ids) throws DaoException;
}
