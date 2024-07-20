package com.dap.sequence.server.service;

import com.base.core.service.BaseService;
import com.dap.sequence.server.entity.SeqOptionalPad;

/**
 * @className SeqOptionalPadService
 * @description 自选
 * @author renle
 * @date 2023/12/21 14:40:43 
 * @version: V23.06
 */
public interface SeqOptionalPadService extends BaseService<SeqOptionalPad, String> {

    /**
     * 获取当前补位值
     *
     * @param seqCode seqCode
     * @param seqVal seqVal
     * @return SeqOptionalPad
     */
    SeqOptionalPad getOptionalPad(String seqCode, String seqVal);

    /**
     * 更新补位值
     *
     * @param seqOptionalPad seqOptionalPad
     */
    void updateOptionalPad(SeqOptionalPad seqOptionalPad);

    /**
     * 锁定自选功能
     *
     * @param id id
     * @return SeqOptionalPad
     */
    SeqOptionalPad selectForUpdatePadById(String id);
}
