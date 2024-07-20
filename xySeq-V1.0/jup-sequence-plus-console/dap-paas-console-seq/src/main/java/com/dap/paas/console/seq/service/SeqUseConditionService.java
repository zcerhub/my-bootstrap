package com.dap.paas.console.seq.service;

import com.base.api.dto.Page;
import com.base.core.service.BaseService;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqUseCondition;


/**
 * @className SeqUseConditionService
 * @description 序列使用情况接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqUseConditionService {

    /**
     * 分页查询
     *
     * @param param param
     * @return Result
     */
    ResultResponse<Page<SeqUseCondition>> queryPage(PageInDto<SeqUseCondition> param);

    /**
     * 保存
     *
     * @param seqUseCondition
     * @return
     */
    Integer saveObject(SeqUseCondition seqUseCondition);
}
