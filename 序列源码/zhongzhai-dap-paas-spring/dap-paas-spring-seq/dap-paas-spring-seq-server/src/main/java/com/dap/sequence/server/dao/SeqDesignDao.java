package com.dap.sequence.server.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.server.entity.SeqDesignPo;

import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/1/19 13:59
 * @Descricption:
 */
public interface SeqDesignDao extends BaseDao<SeqDesignPo, String> {

    /**
     * 获取序列信息
     *
     * @param map map
     * @return SeqDesignPo
     */
    SeqDesignPo getObjectByCode(Map<String, String> map);

    /**
     * 检查接入key
     *
     * @param map map
     * @return int
     */
    int checkAccessKey(Map<String, String> map);
}
