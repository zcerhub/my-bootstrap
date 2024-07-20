package com.dap.paas.console.seq.service;

import com.base.api.dto.Page;
import com.base.core.service.BaseService;
import com.dap.paas.console.seq.entity.SeqRecycleInfo;

/**
 * @className SeqRecycleInfoService
 * @description 序列回收接口
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
public interface SeqRecycleInfoService{

    /**
     * 分页查询
     *
     * @param pageNo
     * @param pageSize
     * @param requestObject
     * @return
     */
    Page queryPage(int pageNo, int pageSize, SeqRecycleInfo requestObject);
}
