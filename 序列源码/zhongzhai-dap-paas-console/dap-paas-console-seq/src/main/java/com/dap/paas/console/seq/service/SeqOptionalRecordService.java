package com.dap.paas.console.seq.service;


import com.base.core.service.BaseService;
import com.base.sys.api.common.Result;
import com.dap.paas.console.seq.entity.SeqOptionalRecord;

/**
 * @className SeqOptionalRecordService
 * @description 序列自选记录接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqOptionalRecordService extends BaseService<SeqOptionalRecord, String> {

    /**
     * 自选序列记录保存
     *
     * @param node node
     * @return Result
     */
    Result insert(SeqOptionalRecord node);

}
