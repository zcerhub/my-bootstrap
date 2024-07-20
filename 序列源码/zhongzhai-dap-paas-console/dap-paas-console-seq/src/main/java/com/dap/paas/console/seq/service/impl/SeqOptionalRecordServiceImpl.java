package com.dap.paas.console.seq.service.impl;

import com.base.api.util.SnowflakeIdWorker;
import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.seq.dao.SeqOptionalRecordDao;
import com.dap.paas.console.seq.entity.SeqOptionalRecord;
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
public class SeqOptionalRecordServiceImpl extends AbstractBaseServiceImpl<SeqOptionalRecord, String> implements SeqOptionalRecordService {

    @Autowired
    private SeqOptionalRecordDao seqOptionalRecordDao;

    @Override
    public Result insert(SeqOptionalRecord seqOptionalRecord) {
        if (Objects.isNull(seqOptionalRecord.getId())) {
            seqOptionalRecord.setId(SnowflakeIdWorker.getID());
        }
        Integer integer = seqOptionalRecordDao.saveObject(seqOptionalRecord);
        if (integer > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
