package com.dap.sequence.server.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dao.SeqOptionalRecordDao;
import com.dap.sequence.server.entity.SeqCurNum;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.service.SeqCurNumService;
import com.dap.sequence.server.service.SeqOptionalRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dap.sequence.api.constant.SequenceConstant.*;

/**
 * (SeqOptionalRecord)表服务实现类
 */
@Service
public class SeqOptionalRecordServiceImpl extends AbstractBaseServiceImpl<SeqOptionalRecord, String> implements SeqOptionalRecordService {

    private static final Logger LOG = LoggerFactory.getLogger(SeqOptionalRecordServiceImpl.class);

    @Autowired
    private SeqOptionalRecordDao seqOptionalRecordDao;


    @Autowired
    private SeqCurNumService seqCurNumService;

    @Override
    public LinkedList<Object> queryRecoveryOptional(SeqParamsDto seqParamsDto, SeqObj seqObj) {
        String seqCode = seqParamsDto.getSeqCode();
        String seqValue = seqParamsDto.getSeqVal();
        // 请求参数有配置请求数量 优先使用请求数量 没有使用序列默认请求数量
        Integer requestNumber = Optional.ofNullable(seqParamsDto.getRequestNumber()).filter(num -> num != 0).orElse(seqObj.getRequestNumber());
        SeqOptionalRecord po = new SeqOptionalRecord();
        po.setSeqCode(seqCode);
        po.setSeqValue(seqValue);
        // 获取可以回收的序列
        List<SeqOptionalRecord> seqOptionalRecords = seqOptionalRecordDao.getRecoveryOptional(po, requestNumber);
        LinkedList<Object> seqList = seqOptionalRecords.stream().map(SeqOptionalRecord::getSerialNumber).collect(Collectors.toCollection(LinkedList::new));
        // 本次请求还需要生产的自选序号数量
        int needCreateNum = requestNumber - seqList.size();
        seqParamsDto.setCreateNumber(needCreateNum);
        return seqList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 3)
    public void selectedOptionalRecord(SeqParamsDto seqParamsDto) {
        try {
            // 锁定对应序列
            SeqOptionalRecord optionalRecord = getUpdateRecord(seqParamsDto);
            optionalRecord = seqOptionalRecordDao.selectForUpdate(optionalRecord);
            String optionalStatus = Optional.ofNullable(optionalRecord)
                    .map(SeqOptionalRecord::getOptionalStatus)
                    .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SELECTED_NOT_FOUND));
            if (optionalStatus.equals(OPTIONAL_STATUS_USE)) {
                throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_SELECTED);
            }
            // 查询自增记录
            SeqCurNum seqCurNum = seqCurNumService.getObjectById(optionalRecord.getCurNumId());
            long curNum = Long.parseLong(seqCurNum.getCurVal());
            long optionalValue = optionalRecord.getOptionalValue();
            LOG.warn("序号【{}】当前自增值：{}，自选值：{}", seqParamsDto.getSeqCode(), curNum, optionalValue);
            String filterStatus = optionalRecord.getFilterStatus();
            if ((filterStatus == null || filterStatus.equals(OPTIONAL_STATUS_UN_FILTER)) && curNum >= optionalValue) {
                throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_SELECTED);
            }
            // 占用序号
            optionalRecord.setUpdateDate(new Date());
            optionalRecord.setOptionalStatus(OPTIONAL_STATUS_USE);
            seqOptionalRecordDao.updateRecordStatus(optionalRecord);
        } catch (Exception e) {
            LOG.error("自选序列选中失败.msg = {}", e.getMessage(), e);
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_SELECTED);
        }
    }

    @Override
    public void cancelOptionalRecord(SeqParamsDto seqParamsDto) {
        try {
            SeqOptionalRecord optionalRecord = getUpdateRecord(seqParamsDto);
            optionalRecord.setUpdateDate(new Date());
            optionalRecord.setOptionalStatus(OPTIONAL_STATUS_UN_USE);
            seqOptionalRecordDao.updateRecordStatus(optionalRecord);
        } catch (Exception e) {
            LOG.error("自选序列取消失败.msg = {}", e.getMessage(), e);
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_CANCEL);
        }
    }

    @Override
    public List<SeqOptionalRecord> getRecordByValue(String seqCode, long start, long end) {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord();
        optionalRecord.setSeqCode(seqCode);
        optionalRecord.setStart(start);
        optionalRecord.setEnd(end);
        return seqOptionalRecordDao.getRecordByValue(optionalRecord);
    }

    @Override
    public void updateFilterStatus(List<SeqOptionalRecord> seqOptionalRecords) {
        seqOptionalRecordDao.updateFilterStatus(seqOptionalRecords);
    }

    private SeqOptionalRecord getUpdateRecord(SeqParamsDto seqParamsDto) {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord();
        optionalRecord.setSeqCode(seqParamsDto.getSeqCode());
        optionalRecord.setSeqValue(seqParamsDto.getSeqVal());
        optionalRecord.setSerialNumber(seqParamsDto.getSerialNumber());
        return optionalRecord;
    }
}
