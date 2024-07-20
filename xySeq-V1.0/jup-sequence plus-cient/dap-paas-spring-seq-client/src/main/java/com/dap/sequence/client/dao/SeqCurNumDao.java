package com.dap.sequence.client.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.client.entity.SeqCurNum;

import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/2/7 13:50
 * @Descricption:
 */
public interface SeqCurNumDao extends BaseDao<SeqCurNum, String> {

    /**
     * 通过code获取计数
     *
     * @param map map
     * @return SeqCurNum
     */
    SeqCurNum getObjectByCode(Map<String, String> map);

    /**
     * 通过id更新计数
     *
     * @param id id
     * @return SeqCurNum
     */
    SeqCurNum selectForUpdateById(String id);

    /**
     * 查询并加锁
     *
     * @param query query
     * @return SeqCurNum
     */
    SeqCurNum selectForUpdateBySeq(SeqCurNum query);
}
