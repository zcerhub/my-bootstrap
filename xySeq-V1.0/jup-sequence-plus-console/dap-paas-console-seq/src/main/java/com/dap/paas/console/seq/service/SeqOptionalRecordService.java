package com.dap.paas.console.seq.service;


import com.base.api.dto.Page;
import com.base.core.service.BaseService;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqOptionalRecord;


/**
 * @className SeqOptionalRecordService
 * @description 序列自选记录接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqOptionalRecordService {

    /**
     * 自选序列记录保存
     *
     * @param node node
     * @return Result
     */
    ResultResponse insert(SeqOptionalRecord node);

    /**
     * 分页查询
     *
     * @param pageNo
     * @param pageSize
     * @param requestObject
     * @return
     */
    Page queryPage(int pageNo, int pageSize, SeqOptionalRecord requestObject);

    /**
     * 更新
     *
     * @param seqOptionalRecord
     */
    Integer updateObject(SeqOptionalRecord seqOptionalRecord);


    Integer delObjectById(String id);
}
