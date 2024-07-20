package com.dap.paas.console.seq.service;

import com.base.api.dto.Page;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqCurNum;

/**
 * @className SeqCurNumService
 * @description 序列计数接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqCurNumService {

    /**
     * 序列使用情况列表
     *
     * @param param param
     * @return Result
     */
    ResultResponse<Page<SeqCurNum>> queryPage(PageInDto<SeqCurNum> param);

    /**
     * 序列使用情况编辑
     *
     * @param seqUseCondition seqUseCondition
     * @return Result
     */
    ResultResponse<Integer> update(SeqCurNum seqUseCondition);
}
