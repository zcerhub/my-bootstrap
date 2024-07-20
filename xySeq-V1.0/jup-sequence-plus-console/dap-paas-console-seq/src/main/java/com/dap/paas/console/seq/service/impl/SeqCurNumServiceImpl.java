package com.dap.paas.console.seq.service.impl;

import com.base.api.dto.Page;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqCurNum;
import com.dap.paas.console.seq.feign.SeqCurNumFeignApi;
import com.dap.paas.console.seq.service.SeqCurNumService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className SeqCurNumServiceImpl
 * @description 序列计数实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
@Slf4j
public class SeqCurNumServiceImpl  implements SeqCurNumService {

    @Autowired
    private SeqCurNumFeignApi seqCurNumFeignApi;

    /**
     * 序列使用情况列表
     *
     * @param param
     * @return
     */
    @Override
    public ResultResponse<Page<SeqCurNum>> queryPage(PageInDto<SeqCurNum> param) {
        return seqCurNumFeignApi.queryPage(param);
    }

    /**
     * 序列使用情况编辑
     *
     * @param seqUseCondition seqUseCondition
     * @return Result
     */
    @Override
    public ResultResponse<Integer> update(SeqCurNum seqUseCondition) {
        return seqCurNumFeignApi.update(seqUseCondition);
    }
}
