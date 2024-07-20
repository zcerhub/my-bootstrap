package com.dap.sequence.server.service;

import com.base.core.service.BaseService;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.server.entity.SeqCurNum;

import java.util.Map;

/**
 * @Author: cao
 * Data: 2022/2/7 14:05
 * @Descricption:
 */
public interface SeqCurNumService extends BaseService<SeqCurNum, String> {

    /**
     * 获取计数
     *
     * @param map map
     * @return SeqCurNum
     */
    SeqCurNum getObjectByCode(Map<String, String> map);

    /**
     * 获取计数
     *
     * @param id id
     * @return SeqCurNum
     */
    SeqCurNum selectForUpdateById(String id);

    /**
     * 查询并锁定
     *
     * @param seqParamsDto seqParamsDto
     * @param rule rule
     * @return SeqCurNum
     */
    SeqCurNum selectForUpdateBySeq(SeqParamsDto seqParamsDto, Rule rule);
}
