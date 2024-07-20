package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.core.SeqServerProducer;
import com.dap.sequence.api.dto.CheckRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.core.AsyncTask;
import com.dap.sequence.server.factory.AbstractSeqFlowEngine;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqOptionalRecordService;
import com.dap.sequence.server.service.SeqRecoveryRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dap.sequence.api.constant.SequenceConstant.CHECK_RULE;

/**
 * @Description: 消费接口服务端实现
 * @Author: zpj
 * @Date: 2021/2/22 17:10
 * @Version: 1.0.1
 */
@Component
public class SeqServerProducerImpl implements SeqServerProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SeqServerProducerImpl.class);
    private static final Pattern pattern = Pattern.compile("\\d+");
    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private SeqOptionalRecordService seqOptionalRecordService;

    @Autowired
    private SeqRecoveryRecordService seqRecoveryRecordService;

    @Override
    public SeqTransferDto getAndCreateSeq(SeqParamsDto seqParamsDto) throws SequenceException {
        if (StringUtils.isBlank(seqParamsDto.getSeqVal())) {
            LOG.warn("序列编号 {} 自选参数为空!!! ", seqParamsDto.engineCacheKey());
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_PARAM_VALID);
        }
        // 自选参数必须位数字

        Matcher matcher = pattern.matcher(seqParamsDto.getSeqVal());
        if (!matcher.matches()) {
            LOG.warn("序列编号 {} 自选参数非法，必须为纯数字!!! ", seqParamsDto.engineCacheKey());
            throw new SequenceException(ExceptionEnum.SEQ_OPTIONAL_SEQ_VAL_ILLEGAL);
        }
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        if (seqFlowEngine == null) {
            LOG.warn("序列编号 {} 执行引擎未生成!!! ", seqParamsDto.engineCacheKey());
            return new SeqTransferDto();
        }
        SeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
        SeqTransferDto transferDto = new SeqTransferDto();
        transferDto.setSequenceCode(seqParamsDto.getSeqCode());
        transferDto.setSeqVal(seqParamsDto.getSeqVal());
        transferDto.setList(seqProducer.createOptional(seqFlowEngine.getSeqObj(), seqParamsDto));
        return transferDto;
    }

    @Override
    public void selectedOptionalSeq(SeqParamsDto seqParamsDto) {
        // 校验位特殊处理
        replaceCheckRule(seqParamsDto);
        seqOptionalRecordService.selectedOptionalRecord(seqParamsDto);
    }

    @Override
    public void cancelOptionalSeq(SeqParamsDto seqParamsDto) {
        // 校验位特殊处理
        replaceCheckRule(seqParamsDto);
        seqOptionalRecordService.cancelOptionalRecord(seqParamsDto);
    }

    private void replaceCheckRule(SeqParamsDto seqParamsDto) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        SeqObj seqObj = seqFlowEngine.getSeqObj();
        List<Rule> rules = SeqHolder.getRulesMap().get(seqObj.getId());
        // 默认校验位只能放到最后一个
        Rule rule = rules.get(rules.size() - 1);
        if (rule.getRuleType().equals(CHECK_RULE)) {
            // 替换校验位
            CheckRuleInfo checkRuleInfo = (CheckRuleInfo) rule.getRuleData();
            String serialNumber = seqParamsDto.getSerialNumber();
            int checkLength = checkRuleInfo.getCheckLength();
            String check = SeqHolder.getRuleHandlerByName(rule.getRuleType()).generateRule(seqObj, rule, null).toString();
            serialNumber = serialNumber.substring(0, serialNumber.length() - checkLength) + check;
            seqParamsDto.setSerialNumber(serialNumber);
        }
    }

    @Override
    public SeqTransferDto getSeqFormServer(SeqParamsDto seqParamsDto) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        if (seqFlowEngine == null) {
            LOG.warn("序列编号 {} 执行引擎未生成!!! ", seqParamsDto.engineCacheKey());
            return new SeqTransferDto();
        }
        if (seqFlowEngine.getSeqObj().getSequenceNumber() == 0) {
            LOG.warn("严格递增序列[{}]请通过严格递增接口获取!!! ", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.SEQ_INCREASE_REQUEST_ERROR);
        }
        SeqTransferDto transferDto = new SeqTransferDto();
        transferDto.setSequenceCode(seqParamsDto.getSeqCode());
        transferDto.setList(doGetCacheSeq(seqParamsDto));
        return transferDto;
    }

    @Override
    public SeqTransferDto createIncreaseSeq(SeqParamsDto seqParamsDto) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        if (seqFlowEngine == null) {
            LOG.warn("序列编号 {} 执行引擎未生成!!! ", seqParamsDto.engineCacheKey());
            return new SeqTransferDto();
        }
        if (seqFlowEngine.getSeqObj().getSequenceNumber() != 0) {
            LOG.warn("非严格递增序列[{}]请通过普通接口获取!!! ", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.SEQ_NOT_INCREASE_REQUEST_ERROR);
        }
        SeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
        SeqTransferDto transferDto = new SeqTransferDto();
        transferDto.setSequenceCode(seqParamsDto.getSeqCode());
        transferDto.setList(seqProducer.createIncreaseSeq(seqFlowEngine.getSeqObj(), seqParamsDto));
        return transferDto;
    }

    @Override
    public SeqTransferDto createRecoverySeq(SeqParamsDto seqParamsDto) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        if (seqFlowEngine == null) {
            LOG.warn("序列编号 {} 执行引擎未生成!!! ", seqParamsDto.engineCacheKey());
            return new SeqTransferDto();
        }
        SeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
        SeqTransferDto transferDto = new SeqTransferDto();
        transferDto.setSequenceCode(seqParamsDto.getSeqCode());
        transferDto.setList(seqProducer.createRecoverySeq(seqFlowEngine.getSeqObj(), seqParamsDto));
        return transferDto;
    }

    @Override
    public SeqTransferDto recoverySeq(SeqParamsDto seqParamsDto) {
        // 校验回收序号
        if (StringUtils.isBlank(seqParamsDto.getSerialNumber())) {
            LOG.warn("回收序列[{}]要回收的序号为空!!! ", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.BODY_NOT_MATCH);
        }
        SeqTransferDto transferDto = new SeqTransferDto();
        transferDto.setSequenceCode(seqParamsDto.getSeqCode());
        seqRecoveryRecordService.updateRecovery(seqParamsDto);
        return transferDto;
    }

    private List<Object> doGetCacheSeq(SeqParamsDto seqParamsDto) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        SeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
        ArrayList<Object> cacheList = new ArrayList<>();
        // 优先使用请求的数量 默认序列配置数量
        int requestNumber = Optional.ofNullable(seqParamsDto.getRequestNumber()).filter(num -> num != 0).orElse(seqFlowEngine.getSeqObj().getRequestNumber());
        String seqCacheKey = seqParamsDto.seqCacheKey();
        BlockingQueue<Object> unUseQueue = SeqHolder.getSequenceMapBySequenceCode(seqCacheKey);
        // 不够直接同步生产 达到最大值结束补仓
        if ((unUseQueue == null || unUseQueue.size() < requestNumber) && !seqParamsDto.isMax()) {
            seqProducer.createSeqAndCache(seqFlowEngine.getSeqObj(), seqParamsDto);
            unUseQueue = SeqHolder.getSequenceMapBySequenceCode(seqParamsDto.seqCacheKey());
        }
        // 缓存获取序列
        if (unUseQueue != null) {
            unUseQueue.drainTo(cacheList, Math.min(unUseQueue.size(), requestNumber));
        }
        // 异步补充生产序列 达到最大值不继续异步补仓
        if (!seqParamsDto.isMax()) {
            asyncTask.asyncCreate(seqParamsDto, seqProducer, seqFlowEngine);
        }
        // 已达到最大值且序列为空，抛出异常错误
        if (seqParamsDto.isMax() && cacheList.isEmpty()) {
            throw new SequenceException(ExceptionEnum.SEQ_NUM_MAXIMIZE);
        }
        String start;
        String end;
        if (cacheList.isEmpty()) {
            start = "seq-null";
            end = "seq-null";
        } else {
            start = String.valueOf(cacheList.get(0));
            end = cacheList.get(cacheList.size() - 1).toString();
        }
        LOG.info("获取序列:{} 结束, seqSize={}, start:{}, endSeq:{}", seqCacheKey, cacheList.size(), start, end);
        return cacheList;
    }
}
