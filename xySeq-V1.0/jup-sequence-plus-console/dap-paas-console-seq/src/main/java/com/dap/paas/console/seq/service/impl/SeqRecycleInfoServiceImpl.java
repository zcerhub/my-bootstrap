package com.dap.paas.console.seq.service.impl;

import com.base.api.dto.Page;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqRecycleInfo;
import com.dap.paas.console.seq.feign.SeqRecycleInfoFeignApi;
import com.dap.paas.console.seq.service.SeqRecycleInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @className SeqRecycleInfoServiceImpl
 * @description 序列回收信息实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service
public class SeqRecycleInfoServiceImpl  implements SeqRecycleInfoService {

    @Autowired
    private SeqRecycleInfoFeignApi seqRecycleInfoFeignApi;

    @Override
    public Page queryPage(int pageNo, int pageSize, SeqRecycleInfo requestObject) {
        PageInDto<SeqRecycleInfo> param = new PageInDto<>();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        param.setRequestObject(requestObject);
        ResultResponse<Page<SeqRecycleInfo>> pageResultResponse = seqRecycleInfoFeignApi.queryPage(param);
        return pageResultResponse.getData();
    }
}
