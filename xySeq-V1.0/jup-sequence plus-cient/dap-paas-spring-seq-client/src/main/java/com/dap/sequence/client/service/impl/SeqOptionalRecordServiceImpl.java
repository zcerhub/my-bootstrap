package com.dap.sequence.client.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.dap.sequence.api.dto.*;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.dao.SeqOptionalRecordDao;
import com.dap.sequence.client.entity.SeqCurNum;
import com.dap.sequence.client.entity.SeqOptionalRecord;
import com.dap.sequence.client.entity.SeqRecycleInfo;
import com.dap.sequence.client.factory.AbstractSeqFlowEngine;
import com.dap.sequence.client.holder.SeqHolder;
import com.dap.sequence.client.service.SeqCurNumService;
import com.dap.sequence.client.service.SeqOptionalRecordService;
import com.dap.sequence.client.service.SeqRecycleInfoService;
import com.dap.sequence.client.utils.NetUtils;
import com.dap.sequence.client.utils.SequenceUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Autowired
    private SeqRecycleInfoService seqRecycleInfoService;

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
        //验证是否传递序列号
        Optional.ofNullable(seqParamsDto.getSerialNumber()).filter(StringUtils::isNotBlank).orElseThrow(() ->
                new SequenceException(ExceptionEnum.GET_SEQ_OPTIONAL_SERIAL_NUM_ERROR));

        // 查询并锁定自增记录
        SeqObj seqObj = getSeqObj(seqParamsDto);
        Rule optionalRule = getOptionRule(seqObj.getId());
        SeqCurNum seqCurNum = getAndSaveIfNoExist(optionalRule.getRuleId(), seqParamsDto);
        seqCurNum = seqCurNumService.selectForUpdateById(seqCurNum.getId());
        long curNum = Long.parseLong(seqCurNum.getCurVal());
        //根据序列号截取自选optionValue, 序列号格式：字符串前缀+自选数字+校验  或者 字符串前缀+自选数字
        seqParamsDto.setOptionalValue(getOptionRuleBySeqNum(seqParamsDto.getSerialNumber(), seqObj.getId()));
        long optionalValue = Long.parseLong(seqParamsDto.getOptionalValue());
        LOG.warn("序号【{}】当前自增值：{}，自选值：{}", seqParamsDto.getSeqCode(), curNum, optionalValue);
        // 自选值 <= 自增记录 不满足使用条件
        if (optionalValue <= curNum) {
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_SELECTED);
        }
        // 占用序号
        SeqOptionalRecord optionalRecord = buildOptionalSeq(seqParamsDto, seqCurNum);
        Integer result = seqOptionalRecordDao.saveOptional(optionalRecord);
        if (result < 1) {
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_SELECTED);
        }
    }

    /**
     * seqNum格式：字符串前缀+自选数字+校验  或者 字符串前缀+自选数字
     *
     * @param seqNum
     * @param seqDesignId
     * @return
     */
    private static String getOptionRuleBySeqNum(String seqNum, String seqDesignId) {
        List<Rule> rules = SeqHolder.getRulesMap().get(seqDesignId);
        NumberRuleInfo numberRuleInfo = null;
        for (Rule rule : rules) {
            String ruleType = rule.getRuleType();
            //字符串前缀+自选数字+校验  或者 字符串前缀+自选数字
            switch (ruleType) {
                case STRING_RULE:
                    StringRuleInfo stringRuleInfo = (StringRuleInfo) rule.getRuleData();
                    seqNum = seqNum.substring(stringRuleInfo.getInitData().length());
                    break;
                case NUMBER_RULE:
                    numberRuleInfo = (NumberRuleInfo) rule.getRuleData();
                    break;
                case CHECK_RULE:
                    CheckRuleInfo checkRuleInfo = (CheckRuleInfo) rule.getRuleData();
                    seqNum = seqNum.substring(0, seqNum.length() - checkRuleInfo.getCheckLength());
                    break;
            }
        }
        //未找到自选规则 或者 长度与定义不相等
        if(numberRuleInfo == null || !numberRuleInfo.getOptional() || numberRuleInfo.getNumberFormat().length() !=  seqNum.length()){
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_VAL_NOT_MATCH_ERROR);
        }
        return seqNum;
    }

    @Override
    public void cancelOptionalRecord(SeqParamsDto seqParamsDto) {
        SeqOptionalRecord optionalRecord = getUpdateRecord(seqParamsDto);
        optionalRecord = seqOptionalRecordDao.selectOneRecord(optionalRecord);
        if (optionalRecord == null) {
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SELECTED_NOT_FOUND);
        }
        SeqObj seqObj = getSeqObj(seqParamsDto);
        Rule optionalRule = getOptionRule(seqObj.getId());
        SeqCurNum seqCurNum = getAndSaveIfNoExist(optionalRule.getRuleId(), seqParamsDto);
        seqCurNum = seqCurNumService.getObjectById(seqCurNum.getId());
        long curNum = Long.parseLong(seqCurNum.getCurVal());
        long optionalValue = optionalRecord.getOptionalValue();
        // 自选值 <= 自增记录 进入回收队列
        if (optionalValue <= curNum) {
            SeqRecycleInfo seqRecycleInfo = new SeqRecycleInfo();
            seqRecycleInfo.setTenantId(seqParamsDto.getTenantId());
            seqRecycleInfo.setRqDay(seqParamsDto.getDay());
            seqRecycleInfo.setSeqDesignId(seqObj.getId());
            seqRecycleInfo.setRecycleCode(seqParamsDto.getSerialNumber());
            seqRecycleInfo.setCreateDate(new Date());
            seqRecycleInfo.setId(SequenceUtil.getUUID32());
            seqRecycleInfo.setIp(NetUtils.ip());
            seqRecycleInfo.setSeqCode(seqObj.getSequenceCode());
            List<SeqRecycleInfo> seqRecycleInfos = new ArrayList<>();
            seqRecycleInfos.add(seqRecycleInfo);
            seqRecycleInfoService.insertBatch(seqRecycleInfos);
        }
        seqOptionalRecordDao.delObjectById(optionalRecord.getId());
    }

    private SeqObj getSeqObj(SeqParamsDto seqParamsDto) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        return seqFlowEngine.getSeqObj();
    }

    private Rule getOptionRule(String id) {
        List<Rule> rules = SeqHolder.getRulesMap().get(id);
        for (Rule rule : rules) {
            String ruleType = rule.getRuleType();
            if (ruleType.equals(NUMBER_RULE)) {
                NumberRuleInfo numberRuleInfo = (NumberRuleInfo) rule.getRuleData();
                if (numberRuleInfo.getOptional()) {
                    return rule;
                }
            }
        }
        throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SERIAL_EMPTY);
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
        //optionalRecord.setSeqValue(seqParamsDto.getSeqVal());
        optionalRecord.setSerialNumber(seqParamsDto.getSerialNumber());
        return optionalRecord;
    }

    public SeqOptionalRecord buildOptionalSeq(SeqParamsDto seqParamsDto, SeqCurNum seqCurNum) {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord();
        optionalRecord.setOptionalValue(Long.parseLong(seqParamsDto.getOptionalValue()));
        optionalRecord.setSerialNumber(seqParamsDto.getSerialNumber());
        optionalRecord.setSeqValue(seqParamsDto.getSeqVal());
        optionalRecord.setCreateDate(new Date());
        optionalRecord.setOptionalStatus(OPTIONAL_STATUS_USE);
        optionalRecord.setCurNumId(seqCurNum.getId());
        optionalRecord.setSeqCode(seqParamsDto.getSeqCode());
        optionalRecord.setSeqLock(0);
        optionalRecord.setId(SequenceUtil.getUUID32());
        return optionalRecord;
    }

    private SeqCurNum getAndSaveIfNoExist(String ruleId, SeqParamsDto seqParamsDto) {
        Map<String, String> mapCur = new HashMap<>();
        mapCur.put("seqInstanceRuleId", ruleId);
        mapCur.put("inDay", seqParamsDto.getDay());
        SeqCurNum seqCurNum = seqCurNumService.getObjectByCode(mapCur);
        if (Objects.isNull(seqCurNum)) {
            seqCurNum = new SeqCurNum();
            seqCurNum.setId(SequenceUtil.getUUID32());
            seqCurNum.setInDay(seqParamsDto.getDay());
            seqCurNum.setSeqInstanceRuleId(ruleId);
            seqCurNum.setTenantId(seqParamsDto.getTenantId());
            seqCurNum.setSeqLock(0);
            seqCurNum.setCurVal("0");
            Integer result = seqCurNumService.saveObject(seqCurNum);
            if (result == null || result == 0) {
                seqCurNum = seqCurNumService.getObjectByCode(mapCur);
            }
        }
        return seqCurNum;
    }
}
