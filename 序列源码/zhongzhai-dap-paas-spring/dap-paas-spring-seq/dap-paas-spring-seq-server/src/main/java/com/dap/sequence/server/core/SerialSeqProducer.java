package com.dap.sequence.server.core;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.SeqProducer;
import com.dap.sequence.api.dto.DateRuleInfo;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqCallbackDto;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.entity.SeqRecoveryRecord;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqOptionalRecordService;
import com.dap.sequence.server.service.SeqRecoveryRecordService;
import com.dap.sequence.server.service.impl.SeqNumService;
import com.dap.sequence.server.utils.SpringUtils;
import com.dap.sequence.server.utils.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * @Description: 连续序列
 * @Author:
 * @Date: update 2023/6/25 14:54
 * @Version: 1.0.0
 */
public class SerialSeqProducer implements SeqProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SerialSeqProducer.class);

    private final SeqNumService seqNumService = SpringUtils.getApplicationContext().getBean(SeqNumService.class);

    private final SeqOptionalRecordService seqOptionalRecordService = SpringUtils.getApplicationContext().getBean(SeqOptionalRecordService.class);

    private final SeqRecoveryRecordService seqRecoveryRecordService = SpringUtils.getApplicationContext().getBean(SeqRecoveryRecordService.class);

    /**
     * 异步回收序列
     *
     * @param seqCallbackDto seqCallbackDto
     * @param callBackNumList callBackNumList
     */
    @Override
    public void loadSeq(SeqCallbackDto seqCallbackDto, List callBackNumList) {
        new Thread(() -> {
            if (!CollectionUtils.isEmpty(callBackNumList)) {
                // 获得服务端队列
                String key = SeqKeyUtils.getSeqCacheKey(seqCallbackDto.getSequenceCode(), seqCallbackDto.getDay(), seqCallbackDto.getTenantId());
                LinkedBlockingDeque<Object> blockingQueue = (LinkedBlockingDeque<Object>) SeqHolder.getMap().get(key);
                LOG.info("序列回收开始，编号：{}，回收数量：{}，起始：{}，结束：{}，回收前队列数量：{}", key, callBackNumList.size(), callBackNumList.get(0), callBackNumList.get(callBackNumList.size() - 1), blockingQueue.size());
                try {
                    for (int i = callBackNumList.size() - 1; i >= 0; i--) {
                        blockingQueue.putFirst(callBackNumList.get(i));
                    }
                    LOG.info("序列回收结束，编号：{}，回收数量：{}，回收后队列数量：{}", key, callBackNumList.size(), blockingQueue.size());
                } catch (InterruptedException e) {
                    LOG.error("loadSeq error.msg = {}", e.getMessage(), e);
                }
            }
        }).start();
    }

    /**
     * 创建序列并缓存
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     */
    @Override
    public synchronized void createSeqAndCache(SeqObj seqObj, SeqParamsDto seqParamsDto) {
        if (seqObj == null) {
            return;
        }
        String seqCacheKey = seqParamsDto.seqCacheKey();
        // 每个批次需要生成的数量
        Integer requestNumber = Optional.ofNullable(seqParamsDto.getRequestNumber()).filter(num -> num != 0).orElse(seqObj.getRequestNumber());
        // 获取服务端缓存
        LinkedBlockingDeque<Object> blockingQueue = (LinkedBlockingDeque<Object>) SeqHolder.getSequenceMapBySequenceCode(seqCacheKey);
        blockingQueue = Optional.ofNullable(blockingQueue).orElseGet(() -> {
            LinkedBlockingDeque<Object> deque = new LinkedBlockingDeque<>();
            SeqHolder.putSequenceMap(seqCacheKey, deque);
            return deque;
        });
        // 获取数据库中回收池的序列 开启回收的才查询回收表
        if (StringUtils.equals(seqObj.getServerRecoverySwitch(), SequenceConstant.RECOVERY_SWITCH_ON) && seqParamsDto.isAsync()) {
            LOG.warn("序列【{}】开启回收功能，优先从回收表获取回收序列", seqCacheKey);
            seqNumService.addCallbackSeq(seqObj, seqParamsDto, blockingQueue);
        }
        // 同步生产只生产本次请求的 可能有别的线程已经执行并补足了缓存
        int queueSize = blockingQueue.size();
        if (!seqParamsDto.isAsync() && queueSize >= requestNumber) {
            LOG.info("上一个线程已经添加序列并满足同步请求，blockingQueue = {}， requestNumber = {}", queueSize, requestNumber);
            return;
        }

        int createNum = seqParamsDto.isAsync() ? seqObj.getSequenceNumber() - queueSize : requestNumber - queueSize;
        LOG.info("序列:{} 开始组装, queueSize={}, 需要生成数量:{}", seqCacheKey, blockingQueue.size(), createNum);
        if (createNum > 0) {
            seqParamsDto.setCreateNumber(createNum);
            List<Object> objects = createSeq(seqObj, seqParamsDto);
            blockingQueue.addAll(objects);
            LOG.info("序列:{} 结束组装, queueSize={}, 实际生成数量:{}", seqCacheKey, blockingQueue.size(), objects.size());
        }
    }

    /**
     * 创建序列
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     * @return List
     * @throws SequenceException  SequenceException
     */
    @Override
    public List<Object> createSeq(SeqObj seqObj, SeqParamsDto seqParamsDto) {
        String seqCacheKey = seqParamsDto.seqCacheKey();
        List<Rule> rules = SeqHolder.getRulesMap().get(seqObj.getId());
        if (CollectionUtils.isEmpty(rules)) {
            throw new SequenceException(ExceptionEnum.SEQ_RULE_EMPTY);
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Object> seqList = addSeq(seqObj, seqParamsDto, rules, false);
        stopWatch.stop();
        LOG.info("序列：{},组装耗时：{} ms , queueSize={}", seqCacheKey, stopWatch.getTotalTimeMillis(), seqList.size());
        return seqList;
    }

    @Override
    public List<Object> createIncreaseSeq(SeqObj seqObj, SeqParamsDto seqParamsDto) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Rule> rules = SeqHolder.getRulesMap().get(seqObj.getId());
        if (CollectionUtils.isEmpty(rules)) {
            throw new SequenceException(ExceptionEnum.SEQ_RULE_EMPTY);
        }
        if (seqParamsDto.getCreateNumber() == null || seqParamsDto.getCreateNumber() == 0) {
            // 配置请求数量
            seqParamsDto.setCreateNumber(Optional.ofNullable(seqObj.getRequestNumber()).filter(num -> num != 0).orElse(1));
        }
        List<Object> list = addSeq(seqObj, seqParamsDto, rules, false);
        stopWatch.stop();
        LOG.error("创建耗时：{}", stopWatch.getTotalTimeMillis());
        return list;
    }

    @Override
    public List<Object> createRecoverySeq(SeqObj seqObj, SeqParamsDto seqParamsDto) {
        List<Rule> rules = SeqHolder.getRulesMap().get(seqObj.getId());
        if (CollectionUtils.isEmpty(rules)) {
            throw new SequenceException(ExceptionEnum.SEQ_RULE_EMPTY);
        }
        if (seqParamsDto.getCreateNumber() == null || seqParamsDto.getCreateNumber() == 0) {
            // 配置请求数量
            seqParamsDto.setCreateNumber(Optional.ofNullable(seqObj.getRequestNumber()).filter(num -> num != 0).orElse(1));
        }
        // 查询锁定记录表
        List<SeqRecoveryRecord> seqRecoveryRecords = seqRecoveryRecordService.selectRecoveryForUpdate(seqParamsDto, seqObj);
        List<Object> seqList = seqRecoveryRecords.stream().map(SeqRecoveryRecord::getSerialNumber).collect(Collectors.toList());
        int needCreateSize = seqParamsDto.getCreateNumber() - seqList.size();
        if (needCreateSize <= 0) {
            return seqList;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 配置请求数量
        seqParamsDto.setCreateNumber(needCreateSize);
        List<Object> list = addSeq(seqObj, seqParamsDto, rules, false);
        // 保存到数据库
        seqRecoveryRecordService.saveRecoveryRecord(seqParamsDto, seqObj, list);
        seqList.addAll(list);
        stopWatch.stop();
        LOG.error("创建耗时：{}", stopWatch.getTotalTimeMillis());
        return seqList;
    }

    @Override
    public List<Object> createOptional(SeqObj seqObj, SeqParamsDto seqParamsDto) throws SequenceException {
        List<Rule> rules = SeqHolder.getRulesMap().get(seqObj.getId());
        if (CollectionUtils.isEmpty(rules)) {
            throw new SequenceException(ExceptionEnum.SEQ_RULE_EMPTY);
        }
        // 自选请求校验数字规则中是否有开启自选能力
        NumberRuleInfo numberRuleInfo = rules.stream()
                .filter(ru -> ru.getRuleType().equals(SequenceConstant.NUMBER_RULE))
                .map(Rule::getRuleData)
                .map(data -> (NumberRuleInfo) data)
                .filter(ruleInfo -> ruleInfo.getOptional() != null && ruleInfo.getOptional())
                .findFirst()
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_OPTIONAL_RULE_EMPTY));
        int optionalBit = String.valueOf(numberRuleInfo.getNumberEnd()).length();
        String seqValue = seqParamsDto.getSeqVal();
        int length = seqValue.length();
        // 自选参数长度大于自选规则配置长度 向左截取最大长度
        if (optionalBit < length) {
            seqValue = seqValue.substring(length - 1 - optionalBit, length - 1);
            seqParamsDto.setSeqVal(seqValue);
        }
        if (optionalBit == seqValue.length()) {
            seqParamsDto.setRequestNumber(1);
        }
        // 获取已经生产可用自选序列并计算需要补充数量
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LinkedList<Object> seqList = seqOptionalRecordService.queryRecoveryOptional(seqParamsDto, seqObj);
        stopWatch.stop();
        LOG.error("自选查询耗时：{}", stopWatch.getTotalTimeMillis());
        StopWatch stopWatch1 = new StopWatch();
        stopWatch1.start();
        // 已生成可使用的不满足请求数量 同步生产补充
        if (seqParamsDto.getCreateNumber() > 0) {
            seqList.addAll(addSeq(seqObj, seqParamsDto, rules, true));
        }
        stopWatch1.stop();
        LOG.error("自选生产耗时：{}", stopWatch1.getTotalTimeMillis());
        return seqList;
    }

    private List<Object> addSeq(SeqObj seqObj, SeqParamsDto seqParamsDto, List<Rule> rules, boolean isOptional) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 记录时间规则用于组装序列的时间串
        Map<String, String> timeMap = new HashMap<>();
        // 记录数字规则用于组装序列的数字列表
        Map<String, LinkedList<Long>> numMap = new HashMap<>();
        // 规则组装统一传入参数
        RuleParams ruleParams = new RuleParams();
        // 配置自选规则在规则拼接中需要的参数
        if (isOptional) {
            ruleParams.setOptional(true);
            ruleParams.setSeqVal(seqParamsDto.getSeqVal());
        }
        for (Rule rule : rules) {
            String ruleType = rule.getRuleType();
            if (SequenceConstant.DATE_RUlE.equals(ruleType)) {
                boolean enableDatePlaceholder=seqParamsDto.isEnableDatePlaceholder();
//                如果日期没有开启日期占位符替换功能，则在服务端进行日期的格式化
                if (!enableDatePlaceholder) {
                    // 时间规则预处理 减少格式化时间次数
                    DateRuleInfo dateRuleInfo = (DateRuleInfo) rule.getRuleData();
                    SimpleDateFormat sdf = new SimpleDateFormat(dateRuleInfo.getDateFormat());
                    // 优先使用用户输入的工作时间 默认系统时间
                    Date date = seqParamsDto.getSeqDate() == null ? new Date() : new Date(seqParamsDto.getSeqDate());
                    timeMap.put(rule.getRuleId(), sdf.format(date));
                }else{
//                否则，开启日期占位符替换功能
                    DateRuleInfo dateRuleInfo = (DateRuleInfo) rule.getRuleData();
                    String dateFormat = dateRuleInfo.getDateFormat();
                    timeMap.put(rule.getRuleId(), StrUtil.getDatePlaceholder(dateFormat));
                }
            } else if (SequenceConstant.NUMBER_RULE.equals(ruleType)) {
                // 自选和非自选统一处理 返回可使用的数量序列 用于后续组装
                StopWatch stopWatch2 = new StopWatch();
                stopWatch2.start();
                LinkedList<Long> nums = isOptional ? seqNumService.updateNumOptional(rule, seqObj, seqParamsDto, ruleParams) : seqNumService.updateNum(rule, seqObj, seqParamsDto);
                stopWatch2.stop();
                LOG.error("数据库事务耗时：{}", stopWatch2.getTotalTimeMillis());
                // 多个数字规则时候 需要使用规则id标识
                numMap.put(rule.getRuleId(), nums);
            } else if (SequenceConstant.ENUM_RUlE.equals(ruleType)) {
                // 枚举规则特殊处理
                seqNumService.updateEnum(rule, seqParamsDto);
            }
        }
        // 保存数字列表到组装参数 ruleParams
        ruleParams.setNumMap(numMap);
        // 保存时间串到组装参数中
        ruleParams.setTime(timeMap);
        if (!numMap.isEmpty()) {
            // 数字规则不为空 多个数字规则 以能够提供最小生产数量的数字规则数量作为可用于组装的数量
            seqParamsDto.setCreateNumber(numMap.values().stream().mapToInt(LinkedList::size).min().orElse(0));
        }
        List<Object> seqList = new LinkedList<>();
        LinkedList<SeqOptionalRecord> seqOptionalRecords = new LinkedList<>();
        for (int j = 0; j < seqParamsDto.getCreateNumber(); j++) {
            StringBuilder seq = new StringBuilder();
            for (Rule rule : rules) {
                seq.append(SeqHolder.getRuleHandlerByName(rule.getRuleType()).generateRule(seqObj, rule, ruleParams));
            }
            String seqNo = seq.toString();
            seqList.add(seq);
            if (isOptional) {
                // 自选请求需要保存自选生产列表
                seqOptionalRecords.add(seqNumService.buildOptionalSeq(seqParamsDto, ruleParams, seqNo));
            }
        }
        // 保存自选数据
        if (!seqOptionalRecords.isEmpty()) {
            StopWatch stopWatch1 = new StopWatch();
            stopWatch1.start();
            seqNumService.saveBatchOptional(seqOptionalRecords);
            stopWatch1.stop();
            LOG.error("自选【{}】记录入库：{}", seqParamsDto.getSeqVal(), stopWatch1.getTotalTimeMillis());
        }
        stopWatch.stop();
        LOG.error("组装耗时：{}", stopWatch.getTotalTimeMillis());
        return seqList;
    }
}
