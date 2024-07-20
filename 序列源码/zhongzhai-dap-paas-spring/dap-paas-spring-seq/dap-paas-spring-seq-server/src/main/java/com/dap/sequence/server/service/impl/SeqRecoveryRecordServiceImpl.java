package com.dap.sequence.server.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dao.SeqRecoveryRecordDao;
import com.dap.sequence.server.entity.SeqRecoveryRecord;
import com.dap.sequence.server.service.SeqRecoveryRecordService;
import com.dap.sequence.server.utils.SequenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @className SeqRecoveryRecordServiceImpl
 * @description 回收序列实现
 * @author renle
 * @date 2024/02/01 11:03:31 
 * @version: V23.06
 */
@Service
public class SeqRecoveryRecordServiceImpl extends AbstractBaseServiceImpl<SeqRecoveryRecord, String> implements SeqRecoveryRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(SeqRecoveryRecordServiceImpl.class);

    @Autowired
    private SeqRecoveryRecordDao seqRecoveryRecordDao;

    @Transactional(rollbackFor = Exception.class, timeout = 3)
    @Override
    public List<SeqRecoveryRecord> selectRecoveryForUpdate(SeqParamsDto seqParamsDto, SeqObj seqObj) {
        SeqRecoveryRecord seqRecoveryRecord = new SeqRecoveryRecord();
        seqRecoveryRecord.setSeqCode(seqObj.getSequenceCode());
        seqRecoveryRecord.setRecoveryStatus(SequenceConstant.RECOVERY_STATUS_UN_USE);
        seqRecoveryRecord.setLimit(seqParamsDto.getCreateNumber());
        List<SeqRecoveryRecord> seqRecoveryRecords = seqRecoveryRecordDao.selectRecoveryForUpdate(seqRecoveryRecord);
        seqRecoveryRecords.stream().peek(record -> record.setRecoveryStatus(SequenceConstant.RECOVERY_STATUS_USE)).forEach(this::updateRecoveryStatus);
        return seqRecoveryRecords;
    }

    @Override
    public int saveRecoveryRecord(SeqParamsDto seqParamsDto, SeqObj seqObj, List<Object> seqList) {
        List<SeqRecoveryRecord> seqRecoveryRecords = seqList.stream().map(seq -> {
            SeqRecoveryRecord seqRecoveryRecord = new SeqRecoveryRecord();
            seqRecoveryRecord.setId(SequenceUtil.getUUID32());
            seqRecoveryRecord.setSeqCode(seqObj.getSequenceCode());
            seqRecoveryRecord.setRecoveryStatus(SequenceConstant.RECOVERY_STATUS_USE);
            seqRecoveryRecord.setSerialNumber(seq.toString());
            seqRecoveryRecord.setCreateDate(new Date());
            seqRecoveryRecord.setTenantId(seqObj.getTenantId());
            return seqRecoveryRecord;
        }).collect(Collectors.toList());
        return seqRecoveryRecordDao.saveBatchRecovery(seqRecoveryRecords);
    }

    @Override
    public void updateRecoveryStatus(SeqRecoveryRecord seqRecoveryRecord) {
        seqRecoveryRecord.setUpdateDate(new Date());
        seqRecoveryRecordDao.updateRecoveryStatus(seqRecoveryRecord);
    }

    @Override
    public void updateRecovery(SeqParamsDto seqParamsDto) {
        SeqRecoveryRecord seqRecoveryRecord = new SeqRecoveryRecord();
        seqRecoveryRecord.setSeqCode(seqParamsDto.getSeqCode());
        seqRecoveryRecord.setSerialNumber(seqParamsDto.getSerialNumber());
        List<SeqRecoveryRecord> seqRecoveryRecords = seqRecoveryRecordDao.queryRecoveryRecords(seqRecoveryRecord);
        if (seqRecoveryRecords == null || seqRecoveryRecords.isEmpty()) {
            LOG.error("回收序列【{}】所回收的序号【{}】无法找到", seqParamsDto.getSeqCode(), seqParamsDto.getSerialNumber());
            throw new SequenceException(ExceptionEnum.SEQ_RECOVERY_SERIAL_NOT_FOUND);
        }
        seqRecoveryRecords.stream().peek(record -> record.setRecoveryStatus(SequenceConstant.RECOVERY_STATUS_UN_USE)).forEach(this::updateRecoveryStatus);
    }
}
