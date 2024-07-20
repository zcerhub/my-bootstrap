package com.dap.sequence.client.core;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.*;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.client.dto.RuleParams;
import com.dap.sequence.client.entity.SeqRecoveryRecord;
import com.dap.sequence.client.holder.SeqHolder;
import com.dap.sequence.client.service.SeqDesignService;
import com.dap.sequence.client.service.SeqOptionalRecordService;
import com.dap.sequence.client.service.SeqRecoveryRecordService;
import com.dap.sequence.client.service.impl.SeqNumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import static com.dap.sequence.client.core.SequenceQueueFactory.SEQUENCE_QUEUE_MAP;

/**
 * @Description: 连续序列
 * @Author:
 * @Date: update 2023/6/25 14:54
 * @Version: 1.0.0
 */
@Service
public class LocalSerialSeqProducer implements LocalSeqProducer , ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(LocalSerialSeqProducer.class);

    private static SeqNumService seqNumService;

    private static  SeqOptionalRecordService seqOptionalRecordService;
   private static SeqRecoveryRecordService seqRecoveryRecordService;
    @Autowired
    private SeqDesignService seqDesignService;
    /**
     * 异步回收序列
     *
     * @param seqCallbackDto  seqCallbackDto
     * @param callBackNumList callBackNumList
     */
    /**
     * 队列最大容量
     */
    private int sequenceQueueCapacity = 100;
    /**
     * 从队列中获取序列的超时时间
     */
    private int timeout;
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
     * @param seqObj       seqObj
     * @param seqParamsDto seqParamsDto
     */
    @Override
    public synchronized void createSeqAndCache(SeqObj seqObj, SeqParamsDto seqParamsDto) {
        if (seqObj == null) {
            return;
        }
        String clientCacheKey = seqParamsDto.clientCacheKey();
        // 每个批次需要生成的数量
        //Integer requestNumber = Optional.ofNullable(seqParamsDto.getRequestNumber()).filter(num -> num != 0).orElse(seqObj.getRequestNumber());
        //  获取客户端缓存，判断是否存在，若不存在则进行初始化

        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(clientCacheKey);
        if (isQueueEmpty(sequenceQueue)) {
            LOG.warn("sequenceQueue 不存在进行本地生成, seqCode:{} ", clientCacheKey);
            //return getFirstSeqFormServer(seqParamsDto);
            // 获取数据库中回收池的序列 开启回收的才查询回收表 异步进行，需要时放开
            /*if (StringUtils.equals(seqObj.getServerRecoverySwitch(), SequenceConstant.RECOVERY_SWITCH_ON) && seqParamsDto.isAsync()) {
                LOG.warn("序列【{}】开启回收功能，优先从回收表获取回收序列", clientCacheKey);
                seqNumService.addCallbackSeq(seqObj, seqParamsDto, sequenceQueue);
            }*/

            int createNum =  seqObj.getRequestNumber();
            LOG.info("序列:{} 开始组装, 需要生成数量:{}", clientCacheKey, createNum);
            if (createNum > 0) {
                seqParamsDto.setCreateNumber(createNum);
                List<Object> objects = createSeq(seqObj, seqParamsDto);
                sequenceQueue = new SequenceQueue(objects, sequenceQueueCapacity, timeout, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
                SEQUENCE_QUEUE_MAP.put(clientCacheKey, sequenceQueue);
                LOG.info("序列:{} 结束组装, queueSize={}, 实际生成数量:{}", clientCacheKey, sequenceQueue.getQuenList().size(), objects.size());
            }
        }
        System.out.println(SEQUENCE_QUEUE_MAP);


    }
    /**
     * 跟新备用序列并缓存
     *
     * @param seqObj       seqObj
     * @param seqParamsDto seqParamsDto
     */
    @Override
    public void refreshSeqAndCache(SeqObj seqObj, SeqParamsDto seqParamsDto) throws SequenceException {

        if (seqObj == null) {
            return;
        }
        String clientCacheKey = seqParamsDto.clientCacheKey();
        // 每个批次需要生成的数量
        Integer requestNumber = Optional.ofNullable(seqParamsDto.getRequestNumber()).filter(num -> num != 0).orElse(seqObj.getRequestNumber());
        // 清除备用系列缓存
        if (!SEQUENCE_QUEUE_MAP.get(clientCacheKey).getQuenList().isEmpty()){
            SEQUENCE_QUEUE_MAP.get(clientCacheKey).getQuenList().remove();
        }
        int createNum = seqObj.getRequestNumber();
        LOG.info("序列:{} 备用缓存已清理，并重新生成，生成数量为{}", clientCacheKey,createNum);
        if (createNum > 0) {
            SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(clientCacheKey);
            seqParamsDto.setCreateNumber(createNum);
            List<Object> objects = createSeq(seqObj, seqParamsDto);
            sequenceQueue = new SequenceQueue(objects, sequenceQueueCapacity, timeout, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
            SEQUENCE_QUEUE_MAP.put(clientCacheKey, sequenceQueue);
            //SEQUENCE_CACHE.put(clientCacheKey,SequenceQueue);
            LOG.info("序列:{} 结束组装,  实际生成数量:{}", clientCacheKey,objects.size());
        }
        System.out.println(SEQUENCE_QUEUE_MAP);


    }

    /**
     * 创建序列
     *
     * @param seqObj       seqObj
     * @param seqParamsDto seqParamsDto
     * @return List
     * @throws SequenceException SequenceException
     */
    @Override
    public List<Object> createSeq(SeqObj seqObj, SeqParamsDto seqParamsDto) {
        String seqCacheKey = seqParamsDto.seqCacheKey();
        List<Rule> rules = SeqHolder.getRulesMap().get(seqObj.getId());
        System.out.println(rules);
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
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        LinkedList<Object> seqList = seqOptionalRecordService.queryRecoveryOptional(seqParamsDto, seqObj);
//        stopWatch.stop();
//        LOG.error("自选查询耗时：{}", stopWatch.getTotalTimeMillis());
        StopWatch stopWatch1 = new StopWatch();
        stopWatch1.start();
        // 已生成可使用的不满足请求数量 同步生产补充
//        if (seqParamsDto.getCreateNumber() > 0) {
//            seqList.addAll(addSeq(seqObj, seqParamsDto, rules, true));
//        }

        List<Object> seqList = addSeq(seqObj, seqParamsDto, rules, true);


        stopWatch1.stop();
        LOG.error("自选生产耗时：{}", stopWatch1.getTotalTimeMillis());
        return seqList;
    }

/*    private List<Object> addSeq(SeqObj seqObj, SeqParamsDto seqParamsDto, List<Rule> rules, boolean isOptional) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 记录时间规则用于组装序列的时间串
        Map<String, String> timeMap = new HashMap<>(16);
        // 记录数字规则用于组装序列的数字列表
        Map<String, LinkedList<Long>> numMap = new HashMap<>(16);
        //记录片换行规则用于组装序列的数字列表
        Map<String,String> specialcharMap = new HashMap<>(  16);
        // 规则组装统一传入参数
        RuleParams ruleParams = new RuleParams();
        // 配置自选规则在规则拼接中需要的参数
        if (isOptional) {
            ruleParams.setOptional(true);
            ruleParams.setSeqVal(seqParamsDto.getSeqVal());
        }
        LinkedList<Long> nums = new LinkedList<>();
        for (Rule rule : rules) {
            String ruleType = rule.getRuleType();
            if (SequenceConstant.DATE_RULE.equals(ruleType)) {
                // 时间规则预处理 减少格式化时间次数
                DateRuleInfo dateRuleInfo = (DateRuleInfo) rule.getRuleData();
                SimpleDateFormat sdf = new SimpleDateFormat(dateRuleInfo.getDateFormat());
                // 优先使用用户输入的工作时间 默认系统时间
                Date date = seqParamsDto.getSeqDate() == null ? new Date() : new Date(seqParamsDto.getSeqDate());
                timeMap.put(rule.getRuleId(), sdf.format(date));
            } else if (SequenceConstant.NUMBER_RULE.equals(ruleType)) {
                // 自选和非自选统一处理 返回可使用的数量序列 用于后续组装
                StopWatch stopWatch2 = new StopWatch();
                stopWatch2.start();
                //如果isOptional==false为普通序列，走produceNormalSeqNumList方法，如果isOptional==true为自选序列，走produceOptionalSeqNumList方法
                //nums = isOptional ? seqNumService.produceOptionalSeqNumList(rule, seqObj, seqParamsDto, ruleParams) :
                nums = seqNumService.produceNormalSeqNumList(rule, seqObj, seqParamsDto);
                stopWatch2.stop();
                LOG.error("数据库事务耗时：{}", stopWatch2.getTotalTimeMillis());
                // 多个数字规则时候 需要使用规则id标识
                numMap.put(rule.getRuleId(), nums);
            } else if (SequenceConstant.ENUM_RULE.equals(ruleType)) {
                // 枚举规则特殊处理
                seqNumService.updateEnum(rule, seqParamsDto);
            }else if (SequenceConstant.SPECIAL_RULE.equals(ruleType)) {
                // 替换符规则特殊处理
                SpecialCharRuleInfo specialCharRuleInfo = (SpecialCharRuleInfo) rule.getRuleData();
                String placeholderInfo = "{" + specialCharRuleInfo.getPlaceholderRule() + ","+(specialCharRuleInfo.getPlaceholderRule() == 0 ? "" : specialCharRuleInfo.getPlaceholderLength() + "" ) + "}";
                specialcharMap.put(rule.getRuleId(),placeholderInfo);

            }

        }
        // 保存数字列表到组装参数 ruleParams
        ruleParams.setNumMap(numMap);
        // 保存时间串到组装参数中
        ruleParams.setTime(timeMap);
        //保存替换行到组装参数中
        ruleParams.setSpecialChar(specialcharMap);

        if (!numMap.isEmpty()) {
            // 数字规则不为空 多个数字规则 以能够提供最小生产数量的数字规则数量作为可用于组装的数量
            seqParamsDto.setCreateNumber(numMap.values().stream().mapToInt(LinkedList::size).min().orElse(0));
        }
        List<Object> seqList = new LinkedList<>();
        //实际生产序列数量
        int actualNum = Math.min(nums.size(), seqParamsDto.getCreateNumber());
//        LinkedList<SeqOptionalRecord> seqOptionalRecords = new LinkedList<>();
        for (int j = 0; j < actualNum; j++) {
            StringBuilder seq = new StringBuilder();
            for (Rule rule : rules) {
                seq.append(SeqHolder.getRuleHandlerByName(rule.getRuleType()).generateRule(seqObj, rule, ruleParams));
            }
            seqList.add(seq);
           }
        stopWatch.stop();
        LOG.error("组装耗时：{}", stopWatch.getTotalTimeMillis());
        return seqList;
    }*/

    private List<Object> addSeq(SeqObj seqObj, SeqParamsDto seqParamsDto, List<Rule> rules, boolean isOptional) {
        // 记录时间规则用于组装序列的时间串
        Map<String, String> timeMap = new HashMap<>(16);
        // 记录数字规则用于组装序列的数字列表
        Map<String, LinkedList<Long>> numMap = new HashMap<>(16);
        //记录片换行规则用于组装序列的数字列表
        Map<String,String> specialcharMap = new HashMap<>(  16);
        // 规则组装统一传入参数
        RuleParams ruleParams = new RuleParams();
        // 配置自选规则在规则拼接中需要的参数
        if (isOptional) {
            ruleParams.setOptional(true);
            ruleParams.setSeqVal(seqParamsDto.getSeqVal());
        }
        // 实际生产序列的数量
        LinkedList<Long> nums = new LinkedList<>();
        for (Rule rule : rules) {
            String ruleType = rule.getRuleType();
            if (SequenceConstant.DATE_RULE.equals(ruleType)) {
                // 时间规则预处理 减少格式化时间次数
                DateRuleInfo dateRuleInfo = (DateRuleInfo) rule.getRuleData();
                SimpleDateFormat sdf = new SimpleDateFormat(dateRuleInfo.getDateFormat());
                // 优先使用用户输入的工作时间 默认系统时间
                Date date = seqParamsDto.getSeqDate() == null ? new Date() : new Date(seqParamsDto.getSeqDate());
                timeMap.put(rule.getRuleId(), sdf.format(date));
            } else if (SequenceConstant.NUMBER_RULE.equals(ruleType)) {
                // 自选和非自选统一处理 返回可使用的数量序列 用于后续组装
                nums = isOptional ? seqNumService.produceOptionalSeqNumList(rule, seqObj, seqParamsDto, ruleParams) :
                        seqNumService.produceNormalSeqNumList(rule, seqObj, seqParamsDto);
                // 多个数字规则时候 需要使用规则id标识
                numMap.put(rule.getRuleId(), nums);
            } else if (SequenceConstant.ENUM_RULE.equals(ruleType)) {
                // 枚举规则特殊处理
                seqNumService.updateEnum(rule, seqParamsDto);
            } else if (SequenceConstant.SPECIAL_RULE.equals(ruleType)) {
                // 替换符规则特殊处理
                SpecialCharRuleInfo specialCharRuleInfo = (SpecialCharRuleInfo) rule.getRuleData();
                String placeholderInfo = "{" + specialCharRuleInfo.getPlaceholderRule() + ","+(specialCharRuleInfo.getPlaceholderRule() == 0 ? "" : specialCharRuleInfo.getPlaceholderLength() + "" ) + "}";
                specialcharMap.put(rule.getRuleId(),placeholderInfo);

            }
        }
        // 保存数字列表到组装参数 ruleParams
        ruleParams.setNumMap(numMap);
        // 保存时间串到组装参数中
        ruleParams.setTime(timeMap);
        //保存替换行到组装参数中
        ruleParams.setSpecialChar(specialcharMap);
        List<Object> seqList = new LinkedList<>();
        int actualNum = Math.min(nums.size(), seqParamsDto.getCreateNumber());
        for (int j = 0; j < actualNum; j++) {
            StringBuilder seq = new StringBuilder();
            for (Rule rule : rules) {
                seq.append(SeqHolder.getRuleHandlerByName(rule.getRuleType()).generateRule(seqObj, rule, ruleParams));
            }
            seqList.add(seq);
        }
        return seqList;
    }

    private List<Object> addIncreaseSeq(SeqObj seqObj, SeqParamsDto seqParamsDto, List<Rule> rules, boolean isOptional) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 记录时间规则用于组装序列的时间串
        Map<String, String> timeMap = new HashMap<>();
        // 记录数字规则用于组装序列的数字列表
        Map<String, LinkedList<Long>> numMap = new HashMap<>();
        // 记录枚举规则用于组装序列的索引列表
        Map<String, LinkedList<Long>> enumMap = new HashMap<>();
        // 规则组装统一传入参数
        RuleParams ruleParams = new RuleParams();
        for (Rule rule : rules) {
            String ruleType = rule.getRuleType();
            String ruleId = rule.getRuleId();
            if (SequenceConstant.DATE_RULE.equals(ruleType)) {
                // 时间规则预处理 减少格式化时间次数
                DateRuleInfo dateRuleInfo = (DateRuleInfo) rule.getRuleData();
                SimpleDateFormat sdf = new SimpleDateFormat(dateRuleInfo.getDateFormat());
                // 优先使用用户输入的工作时间 默认系统时间
                Date date = seqParamsDto.getSeqDate() == null ? new Date() : new Date(seqParamsDto.getSeqDate());
                timeMap.put(ruleId, sdf.format(date));
            } else if (SequenceConstant.NUMBER_RULE.equals(ruleType)) {
                // 自选和非自选统一处理 返回可使用的数量序列 用于后续组装
                StopWatch stopWatch2 = new StopWatch();
                stopWatch2.start();
                LinkedList<Long> nums = new LinkedList<>(getBatchSequence(ruleId, seqParamsDto.getCreateNumber()));
                stopWatch2.stop();
                LOG.error("数据库事务耗时：{}", stopWatch2.getTotalTimeMillis());
                // 多个数字规则时候 需要使用规则id标识
                numMap.put(ruleId, nums);
            } else if (SequenceConstant.ENUM_RULE.equals(ruleType)) {
                // 枚举规则特殊处理
                LinkedList<Long> nums = new LinkedList<>(getBatchSequence(ruleId, seqParamsDto.getCreateNumber()));
                enumMap.put(ruleId, nums);
            }
        }
        ruleParams.setEnumMap(enumMap);
        // 保存数字列表到组装参数 ruleParams
        ruleParams.setNumMap(numMap);
        // 保存时间串到组装参数中
        ruleParams.setTime(timeMap);
        if (!numMap.isEmpty()) {
            // 数字规则不为空 多个数字规则 以能够提供最小生产数量的数字规则数量作为可用于组装的数量
            seqParamsDto.setCreateNumber(numMap.values().stream().mapToInt(LinkedList::size).min().orElse(0));
        }
        List<Object> seqList = new LinkedList<>();
        for (int j = 0; j < seqParamsDto.getCreateNumber(); j++) {
            StringBuilder seq = new StringBuilder();
            for (Rule rule : rules) {
                seq.append(SeqHolder.getRuleHandlerByName(rule.getRuleType()).generateRule(seqObj, rule, ruleParams));
            }
            seqList.add(seq);
        }
        stopWatch.stop();
        LOG.error("组装耗时：{}", stopWatch.getTotalTimeMillis());
        return seqList;
    }

    private List<Long> getBatchSequence(String seqName, int number) {
        // TODO 获取当前序号 SELECT test_seq.nextval FROM (select level from dual connect by level <![CDATA[ <= ]]> #{param})
        return new ArrayList<>();
    }

    private Long getSequence(String seqName) {
        // TODO 获取下个序号 select test_seq.nextval from dual;
        return -1L;
    }

    private long getCurSequence(String seqName) {
        // TODO 获取当前序号 select test_seq.currval from dual;
        try {

        } catch (Exception e) {
            // 未初始化会报错
            return -1L;
        }
        return -1L;
    }

    private boolean isQueueEmpty(SequenceQueue sequenceQueue) {
        return null == sequenceQueue || CollectionUtils.isEmpty(sequenceQueue.getCacheBlockingQueue());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        seqNumService = applicationContext.getBean(SeqNumService.class);
        seqOptionalRecordService = applicationContext.getBean(SeqOptionalRecordService.class);
        seqRecoveryRecordService = applicationContext.getBean(SeqRecoveryRecordService.class);

    }
}
