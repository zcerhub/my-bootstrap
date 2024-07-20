package com.dap.paas.console.seq.service.impl;

import com.base.api.dto.Page;
import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.dto.PageInDto;
import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.entity.SeqCurNum;
import com.dap.paas.console.seq.entity.SeqOptionalRecord;
import com.dap.paas.console.seq.feign.SeqOptionalRecordFeignApi;
import com.dap.paas.console.seq.service.SeqOptionalRecordService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @className SeqOptionalRecordServiceImpl
 * @description 序列自选实现
 * @date 2023/12/07 10:14:29
 * @version: V23.06
 */
@Service("seqOptionalRecordService")
public class SeqOptionalRecordServiceImpl implements SeqOptionalRecordService {

    @Autowired
    private SeqOptionalRecordFeignApi seqOptionalRecordFeignApi;

    @Override
    public ResultResponse insert(SeqOptionalRecord seqOptionalRecord) {
        if (Objects.isNull(seqOptionalRecord.getId())) {
            seqOptionalRecord.setId(SnowflakeIdWorker.getID());
        }
        ResultResponse<Integer> resultResponse = seqOptionalRecordFeignApi.saveObject(seqOptionalRecord);

        if (resultResponse.getData() > 0) {
            return ResultResponse.success();
        } else {
            return ResultResponse.error(ExceptionEnum.SAVE_FAIL);
        }
    }

    @Override
    public Page queryPage(int pageNo, int pageSize, SeqOptionalRecord requestObject) {
        PageInDto<SeqOptionalRecord> param = new PageInDto<>();
        param.setPageNo(pageNo);
        param.setPageSize(pageSize);
        param.setRequestObject(requestObject);
        ResultResponse<Page<SeqOptionalRecord>> pageResultResponse = seqOptionalRecordFeignApi.queryPage(param);

        return pageResultResponse.getData();
    }

    @Override
    public Integer updateObject(SeqOptionalRecord seqOptionalRecord) {
        ResultResponse<Integer> integerResultResponse = seqOptionalRecordFeignApi.updateObject(seqOptionalRecord);
        return integerResultResponse.getData();
    }

    @Override
    public Integer delObjectById(String id) {
        ResultResponse<Integer> resultResponse = seqOptionalRecordFeignApi.delObjectById(id);
        return resultResponse.getData();
    }
}
