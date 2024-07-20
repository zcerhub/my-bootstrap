package com.dap.sequence.client.dao;

import com.base.core.dao.BaseDao;
import com.dap.sequence.client.entity.SeqDesignPo;

import java.util.List;
import java.util.Map;

/**
 * @className SeqDesignDao
 * @description 序列设计Dao
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqDesignDao extends BaseDao<SeqDesignPo, String> {

    /**
     * 获取应用总数
     *
     * @return int
     */
    int queryApplicationTotal();

    /**
     * 通过序列code获取序列信息
     *
     * @param map map
     * @return SeqDesignPo
     */
    SeqDesignPo getObjectByCode(Map map);

    /**
     * 通过序列生产方式获取序列信息
     *
     * @param map map
     * @return SeqDesignPo
     */
    SeqDesignPo getObjectByid(String id);


    /**
     * 通过id更新状态
     *
     * @param paramMap paramMap
     * @return Integer
     */
    Integer updateStatusByIds(Map<String, Object> paramMap);

    /**
     * 批量更新
     *
     * @param seqDesignList seqDesignList
     * @return Integer
     */
    Integer updateObjectBatch(List<SeqDesignPo> seqDesignList);


    List<SeqDesignPo> selectCode();

    SeqDesignPo getLocalObjectByCode(String code);

}
