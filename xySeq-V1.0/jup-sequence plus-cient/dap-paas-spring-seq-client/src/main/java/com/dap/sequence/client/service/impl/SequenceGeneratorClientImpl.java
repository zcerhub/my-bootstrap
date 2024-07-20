package com.dap.sequence.client.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqOptionalParamsDto;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.util.RandomUtil;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.service.SequenceGeneratorClient;
import com.dap.sequence.client.snow.SnowflakeGenerator;
import com.dap.sequence.client.utils.CheckRuleUtil;
import com.dap.sequence.client.utils.PlaceholderRuleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.dap.sequence.client.core.SequenceQueueFactory.Seq_Design_MAP;

/**
 * @className SequenceGeneratorClientImpl
 * @description 序列接入类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Service
@Validated
public class SequenceGeneratorClientImpl implements SequenceGeneratorClient {

    /*@Autowired
    private SeqFeignApi seqFeignApi;*/

    @Autowired
    private SeqConsumer seqConsumer;

    @Autowired
    private SequenceQueueFactory sequenceQueueFactory;

    /**
     * 根据序列编号获取全局唯一ID序
     *
     * @param seqCode 序列Code编码
     * @param replaceSymbols 占位符规则参数
     * @return
     */
    @Override
    public String getStringSequence(String seqCode, String... replaceSymbols) {
        return getStringSequence(seqCode, null, replaceSymbols);
    }

    /**
     * 根据序列编号获取全局唯一ID序
     *
     * @param seqCode           序列Code编码
     * @param sign              数字规则从头开始标志
     * @param replaceSymbols    占位符规则参数
     * @return
     */
    @Override
    public String getStringSequence(String seqCode, String sign, String... replaceSymbols) {
        SeqDesignPo seqDesignPo = Seq_Design_MAP.get(seqCode);
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        if(null == seqDesignPo){
            seqParamsDto.setSeqCode(seqCode);
            if (ObjectUtil.isNull(SequenceQueueFactory.SEQUENCE_DESIGN_MAP.get(seqCode))) {
                sequenceQueueFactory.getSeqDesignObj(seqParamsDto);
            }
            seqParamsDto.setSequenceProdType(SequenceQueueFactory.SEQUENCE_DESIGN_MAP.get(seqCode).getSequenceProdType());
        }else{
            seqParamsDto = seqDesignPo.fromSeqDesignPo(seqDesignPo);
        }
        seqParamsDto.setDay(sign);
        setParams(seqParamsDto, replaceSymbols);
        Object seq = sequenceQueueFactory.getSequenceBase(seqParamsDto, new Date());
        String sequence = String.valueOf(seq);
        return PlaceholderRuleUtil.placeholderHandle(sequence, replaceSymbols);
    }

    /**
     * 根据序列编号获取全局唯一严格递增ID序
     *
     * @param seqCode        序列Code编码
     * @param replaceSymbols 占位符规则参数
     * @return 序号
     */
    @Override
    public List<String> getIncreaseSequence(String seqCode, Integer requestNumber, String... replaceSymbols) {
        return getIncreaseSequence(seqCode, null,requestNumber, replaceSymbols);
    }

    /**
     * 根据序列编号获取全局唯一严格递增ID序
     *
     * @param seqCode 序列Code编码
     * @param sign    数字规则从头开始标志
     * @param requestNumber
     * @param replaceSymbols  占位符规则参数
     * @return
     */
    @Override
    public List<String> getIncreaseSequence(String seqCode, String sign, Integer requestNumber, String... replaceSymbols) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setDay(sign);
        seqParamsDto.setCreateNumber(requestNumber);
        setParams(seqParamsDto, replaceSymbols);
        List<Object> seqList = sequenceQueueFactory.getIncreaseSequence(seqParamsDto, new Date());
        List<String> list = new ArrayList<>();
        seqList.forEach(seq -> list.add(PlaceholderRuleUtil.placeholderHandle((String) seq, replaceSymbols)));
        return list;
    }

    /**
     * 获取普通非自选序列
     *
     * @param seqCode 序列Code编码
     * @return 序号
     */
    @Override
    public String getRecoverySequence(String seqCode) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setDay(SequenceConstant.DAY_BASE);
        Object seq = sequenceQueueFactory.getRecoverySequence(seqParamsDto, new Date());
        return String.valueOf(seq);
    }

    /**
     * 回收普通非自选序列
     *
     * @param seqCode 序列Code编码
     * @param sequence 回收序号
     * @return
     */
    @Override
    public boolean recoverySequence(String seqCode, String sequence) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        seqParamsDto.setSerialNumber(sequence);
        seqParamsDto.setDay(SequenceConstant.DAY_BASE);
        return sequenceQueueFactory.recoverySequence(seqParamsDto, new Date());
    }

    /**
     * 获取每日切换序列
     *
     * @param seqCode 序列Code编码
     * @param localDate 序列生产时间
     * @param replaceSymbols 占位符规则参数
     * @return
     */
    @Override
    public String getCycleSequence(String seqCode, LocalDate localDate, String... replaceSymbols) {
        SeqParamsDto seqParamsDto = new SeqParamsDto();
        seqParamsDto.setSeqCode(seqCode);
        setParams(seqParamsDto, replaceSymbols);
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
        Date date = Date.from(instant);
        Object seq = sequenceQueueFactory.getSequenceBase(seqParamsDto, date);
        String sequence = String.valueOf(seq);
        return PlaceholderRuleUtil.placeholderHandle(sequence, replaceSymbols);
    }

    /**
     * 自选序号获取
     *
     * @param seqCode 序列Code编码
     * @param optionalValue 自选号段参数
     * @return 序列段
     */
    @Override
    public SeqTransferDto getOptionalSequence(String seqCode, String optionalValue) {
        return getOptionalSequence(seqCode, optionalValue, 1);
    }

    /**
     * 自选序号获取并指定获取数量
     *
     * @param seqCode 序列Code编码
     * @param optionalValue 自选号段参数
     * @param requestNumber 获取数量
     * @return 序列段
     */
    @Override
    public SeqTransferDto getOptionalSequence(String seqCode, String optionalValue, Integer requestNumber) {
        SeqOptionalParamsDto seqOptionalParamsDto = new SeqOptionalParamsDto();
        seqOptionalParamsDto.setSeqCode(seqCode);
        optionalValue = Optional.ofNullable(optionalValue).orElseGet(() -> String.valueOf(RandomUtil.getRandomLong(1)));
        seqOptionalParamsDto.setSeqVal(optionalValue);
        seqOptionalParamsDto.setRequestNumber(requestNumber);
        seqOptionalParamsDto.setSeqDate(System.currentTimeMillis());
        seqOptionalParamsDto.setDay(SequenceConstant.DAY_BASE);
        ResultResponse<SeqTransferDto> result = seqConsumer.getOptionalSequenceFormServer(seqOptionalParamsDto);
        SeqTransferDto dto = Optional.ofNullable(result.getData()).orElseGet(SeqTransferDto::new);
        List<Object> seqList = new ArrayList<>();
        Optional.ofNullable(dto.getList()).ifPresent(list -> list.forEach(obj -> seqList.add(CheckRuleUtil.checkRuleHandle(String.valueOf(obj)))));
        dto.setSequenceCode(seqCode);
        dto.setSeqVal(optionalValue);
        dto.setList(seqList);
        dto.setMsg(result.getMsg());
        dto.setCode(result.getCode());
        return dto;
    }

    /**
     * 选中序号
     *
     * @param seqCode seqCode
     * @param seqNo seqNo
     * @return SeqTransferDto
     */
    @Override
    public SeqTransferDto selectedOptionalSequence(String seqCode, String seqNo) {
        SeqOptionalParamsDto seqOptionalParamsDto = new SeqOptionalParamsDto();
        seqOptionalParamsDto.setSeqCode(seqCode);
        seqOptionalParamsDto.setSerialNumber(seqNo);
        ResultResponse<SeqTransferDto> result = seqConsumer.selectedOptionalSequence(seqOptionalParamsDto);
        SeqTransferDto seqTransferDto = new SeqTransferDto();
        seqTransferDto.setCode(result.getCode());
        seqTransferDto.setMsg(result.getMsg());
        return seqTransferDto;
    }

    /**
     * 取消自选
     *
     * @param seqCode seqCode
     * @param sequence seqNo
     * @return SeqTransferDto
     */
    @Override
    public SeqTransferDto cancelOptionalSequence(String seqCode, String sequence) {
        SeqOptionalParamsDto seqOptionalParamsDto = new SeqOptionalParamsDto();
        seqOptionalParamsDto.setSeqCode(seqCode);
        seqOptionalParamsDto.setSerialNumber(sequence);
        ResultResponse<SeqTransferDto> result = seqConsumer.cancelOptionalSequence(seqOptionalParamsDto);
        SeqTransferDto seqTransferDto = new SeqTransferDto();
        seqTransferDto.setCode(result.getCode());
        seqTransferDto.setMsg(result.getMsg());
        return seqTransferDto;
    }

    /**
     * 获取雪花序列
     *
     * @return 序列号
     */
    @Override
    public String getSnowFlake() {
        return SnowflakeGenerator.nextIdStr();
    }

    /**
     * 替换赋值
     *
      * @param seqParamsDto
     * @param replaceSymbols
     */
    private void setParams(SeqParamsDto seqParamsDto, String... replaceSymbols) {
        if (!ObjectUtils.isEmpty(replaceSymbols)) {
            seqParamsDto.setParams(Arrays.asList(replaceSymbols));
        }
    }
}
