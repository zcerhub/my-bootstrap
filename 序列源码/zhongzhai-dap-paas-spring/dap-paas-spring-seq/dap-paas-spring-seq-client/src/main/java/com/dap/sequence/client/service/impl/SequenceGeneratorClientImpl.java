package com.dap.sequence.client.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.util.RandomUtil;
import com.dap.sequence.client.balancer.RestTemplateBalancer;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.service.SequenceGeneratorClient;
import com.dap.sequence.client.snow.SnowflakeGenerator;
import com.dap.sequence.client.utils.CheckRuleUtil;
import com.dap.sequence.client.utils.DatePlaceholderUtil;
import com.dap.sequence.client.utils.PlaceholderRuleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @className SequenceGeneratorClientImpl
 * @description 序列接入类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Service
public class SequenceGeneratorClientImpl implements SequenceGeneratorClient {

    @Autowired
    RestTemplateBalancer restTemplateBalancer;

    @Autowired
    private SeqConsumer seqConsumer;

    @Autowired
    private SequenceQueueFactory sequenceQueueFactory;

    @Override
    public String getStringSequence(String seqCode, String... params) {
        return getVariableSequence(seqCode, null, params);
    }

    @Override
    public String getStringSequence(String seqCode, Date date, String... params) {
        return getVariableSequence(seqCode, null, params);
    }

    @Override
    public String getStringSequenceWithDatePlaceholder(String seqCode,Date date, String... params) {
        return getVariableSequenceWithDatePlaceholder(seqCode, date,null, params);
    }

    @Override
    public String getVariableSequence(String seqCode, String sign, String... params) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setDay(sign);
        setParams(seqParamsDto, params);
        Object seq = sequenceQueueFactory.getSequenceBase(seqParamsDto, new Date());
        String sequence = String.valueOf(seq);
        return PlaceholderRuleUtil.placeholderHandle(sequence, params);
    }

    @Override
    public String getVariableSequence(String seqCode,Date date, String sign, String... params) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setDay(sign);
        setParams(seqParamsDto, params);
//        如果date不为空表明开启日期占位符替换功能
        if (ObjectUtil.isNotNull(date)) {
            seqParamsDto.setEnableDatePlaceholder(true);
        }

//        如果用户传入日期，则使用用户传入的日期，否则使用系统时间
        Date seqDate=date;
        if (ObjectUtil.isNull(seqDate)) {
            seqDate = new Date();
        }
        Object seq = sequenceQueueFactory.getSequenceBase(seqParamsDto,seqDate);
        String sequence = String.valueOf(seq);
        String seqValue = PlaceholderRuleUtil.placeholderHandle(sequence, params);
        if (seqParamsDto.isEnableDatePlaceholder()) {
            seqValue = DatePlaceholderUtil.datePlaceholderHandle(seqValue,date);
        }
        return seqValue;
    }

    @Override
    public String getIncreaseSequence(String seqCode, String... params) {
        return getIncreaseSequenceBySign(seqCode, null, params);
    }

    @Override
    public String getIncreaseSequenceBySign(String seqCode, String sign, String... params) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setDay(sign);
        setParams(seqParamsDto, params);
        Object seq = sequenceQueueFactory.getIncreaseSequence(seqParamsDto, new Date());
        String sequence = String.valueOf(seq);
        return PlaceholderRuleUtil.placeholderHandle(sequence, params);
    }

    @Override
    public String getRecoverySequence(String seqCode) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setDay(SequenceConstant.DAY_BASE);
        Object seq = sequenceQueueFactory.getRecoverySequence(seqParamsDto, new Date());
        return String.valueOf(seq);
    }

    @Override
    public String getRecoverySequenceWithDatePlaceholder(String seqCode,Date date) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setDay(SequenceConstant.DAY_BASE);
//        设置开启日期替换符
        seqParamsDto.setEnableDatePlaceholder(true);
        Object seq = sequenceQueueFactory.getRecoverySequence(seqParamsDto, new Date());
        String seqValue = String.valueOf(seq);
        seqValue = DatePlaceholderUtil.datePlaceholderHandle(seqValue, date);
        return seqValue;
    }

    @Override
    public Boolean recoverySequence(String seqCode, String seqNo) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setSerialNumber(seqNo);
        seqParamsDto.setDay(SequenceConstant.DAY_BASE);
        return sequenceQueueFactory.recoverySequence(seqParamsDto, new Date());
    }

    @Override
    public String getDaySwitchSequence(String seqCode, Date date, String... params) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        setParams(seqParamsDto, params);
        Object seq = sequenceQueueFactory.getSequenceBase(seqParamsDto, date);
        String sequence = String.valueOf(seq);
        return PlaceholderRuleUtil.placeholderHandle(sequence, params);
    }

    @Override
    public String getDaySwitchSequenceWithDatePlaceholder(String seqCode, Date date, boolean enableDatePlaceholder, String... params) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setEnableDatePlaceholder(enableDatePlaceholder);
        setParams(seqParamsDto, params);
        Object seq = sequenceQueueFactory.getSequenceBase(seqParamsDto, date);
        String sequence = String.valueOf(seq);
        sequence=PlaceholderRuleUtil.placeholderHandle(sequence, params);
        if (enableDatePlaceholder) {
            sequence = DatePlaceholderUtil.datePlaceholderHandle(sequence, date);
        }
        return sequence;
    }

    @Override
    public SeqTransferDto getOptionalSequence(String seqCode, String seqVal) {
        return getOptionalSequence(seqCode, seqVal, null);
    }

    @Override
    public SeqTransferDto getOptionalSequence(String seqCode, String seqVal, Integer requestNumber) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqVal = Optional.ofNullable(seqVal).orElseGet(() -> String.valueOf(RandomUtil.getRandomLong(1)));
        seqParamsDto.setSeqVal(seqVal);
        seqParamsDto.setRequestNumber(requestNumber);
        seqParamsDto.setSeqDate(System.currentTimeMillis());
        seqParamsDto.setDay(SequenceConstant.DAY_BASE);
        ResultResponse result = seqConsumer.getOptionalSequenceFormServer(seqParamsDto);
        SeqTransferDto dto = Optional.ofNullable(result.getData()).map(data -> JSON.parseObject(data.toString(), SeqTransferDto.class)).orElseGet(SeqTransferDto::new);
        List<Object> seqList = new ArrayList<>();
        Optional.ofNullable(dto.getList()).ifPresent(list -> list.forEach(obj -> seqList.add(CheckRuleUtil.checkRuleHandle(String.valueOf(obj)))));
        dto.setSequenceCode(seqCode);
        dto.setSeqVal(seqVal);
        dto.setList(seqList);
        dto.setMsg(result.getMsg());
        dto.setCode(result.getCode());
        return dto;
    }

    @Override
    public SeqTransferDto selectedOptionalSequence(String seqCode, String seqNo) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setSerialNumber(seqNo);
        ResultResponse result = seqConsumer.selectedOptionalSequence(seqParamsDto);
        SeqTransferDto seqTransferDto = new SeqTransferDto();
        seqTransferDto.setCode(result.getCode());
        seqTransferDto.setMsg(result.getMsg());
        return seqTransferDto;
    }

    @Override
    public SeqTransferDto cancelOptionalSequence(String seqCode, String seqNo) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setSerialNumber(seqNo);
        ResultResponse result = seqConsumer.cancelOptionalSequence(seqParamsDto);
        SeqTransferDto seqTransferDto = new SeqTransferDto();
        seqTransferDto.setCode(result.getCode());
        seqTransferDto.setMsg(result.getMsg());
        return seqTransferDto;
    }


    @Override
    public String getSnowFlake() {
        return SnowflakeGenerator.nextIdStr();
    }

    private void setParams(SeqParamsDto seqParamsDto, String... params) {
        if (!ObjectUtils.isEmpty(params)) {
            seqParamsDto.setParams(Arrays.asList(params));
        }
    }
}
