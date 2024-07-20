package com.base.sys.async.service;

import com.base.api.exception.DaoException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * 异步线程的操作接口
 */
public interface AsyncCacheService {

    void asyncWriteCache();

    void asyncWriteUserCache() throws DaoException;

    void asyncWriteUserDataRule() throws DaoException, ParserConfigurationException;

    /**
     * 查询角色相关信息存入缓存
     *
     * @throws DaoException
     */
    void asyncWriteUserAppCache() throws DaoException;





}
