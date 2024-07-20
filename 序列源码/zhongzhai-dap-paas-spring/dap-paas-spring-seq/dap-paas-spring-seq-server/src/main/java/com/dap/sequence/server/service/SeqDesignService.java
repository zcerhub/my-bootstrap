package com.dap.sequence.server.service;

import com.base.core.service.BaseService;
import com.dap.sequence.server.entity.SeqDesignPo;

import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/1/19 14:01
 * @Descricption:
 */
public interface SeqDesignService extends BaseService<SeqDesignPo, String> {

    /**
     * 获取序列
     *
     * @param map map
     * @return SeqDesignPo
     */
    SeqDesignPo getObjectByCode(Map<String, String> map);

    /**
     * 接入key检查
     *
     * @param map map
     * @return boolean
     */
    boolean checkAccessKey(Map<String, String> map);
}
