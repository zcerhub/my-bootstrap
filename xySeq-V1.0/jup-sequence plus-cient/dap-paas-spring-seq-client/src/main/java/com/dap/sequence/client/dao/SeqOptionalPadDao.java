package com.dap.sequence.client.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.client.entity.SeqOptionalPad;

/**
 * @className SeqOptionalPadDao
 * @description TODO
 * @author renle
 * @date 2023/12/21 14:56:35 
 * @version: V23.06
 */
public interface SeqOptionalPadDao extends BaseDao<SeqOptionalPad, String> {

    /**
     * 获取当前补位值
     *
     * @param seqOptionalPad seqOptionalPad
     * @return SeqOptionalPad
     */
    SeqOptionalPad getOptionalPad(SeqOptionalPad seqOptionalPad);

    /**
     * 保存当前补位值
     *
     * @param seqOptionalPad seqOptionalPad
     * @return Integer
     */
    Integer saveOptionalPad(SeqOptionalPad seqOptionalPad);

    /**
     * 更新补位值
     *
     * @param seqOptionalPad seqOptionalPad
     */
    void updateOptionalPad(SeqOptionalPad seqOptionalPad);

    /**
     * 查询自选列并锁定
     *
     * @param id id
     * @return SeqOptionalPad
     */
    SeqOptionalPad selectForUpdatePadById(String id);
}
