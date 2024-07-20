package com.dap.sequence.server.service.impl;

import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.dto.DateRuleInfo;
import com.dap.sequence.api.dto.EnumRuleInfo;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.RuleParams;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.server.dao.SeqOptionalRecordDao;
import com.dap.sequence.server.entity.SeqCurNum;
import com.dap.sequence.server.entity.SeqOptionalPad;
import com.dap.sequence.server.entity.SeqOptionalRecord;
import com.dap.sequence.server.entity.SeqRecycleInfo;
import com.dap.sequence.server.holder.SeqHolder;
import com.dap.sequence.server.service.SeqCurNumService;
import com.dap.sequence.server.service.SeqOptionalPadService;
import com.dap.sequence.server.service.SeqOptionalRecordService;
import com.dap.sequence.server.service.SeqRecycleInfoService;
import com.dap.sequence.server.utils.SequenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import static com.dap.sequence.api.constant.SequenceConstant.OPTIONAL_STATUS_UN_FILTER;
import static com.dap.sequence.api.constant.SequenceConstant.OPTIONAL_STATUS_UN_USE;

/**
 * 异步生产序列类
 *
 * @author zpj
 * @date 2021/07/01
 */
@Component
public class SeqNumService {
    private static final Logger LOG = LoggerFactory.getLogger(SeqNumService.class);

    @Autowired
    private SeqCurNumService seqCurNumService;

    @Autowired
    private SeqRecycleInfoService seqRecycleInfoService;

    @Autowired
    private SeqOptionalPadService seqOptionalPadService;

    @Autowired
    private SeqOptionalRecordService seqOptionalRecordService;

    @Autowired
    private SeqOptionalRecordDao seqOptionalRecordDao;

    //@Transactional(rollbackFor = Exception.class, timeout = 3)
    //public List<Object> addSeq(SeqObj seqObj, SeqParamsDto seqParamsDto, List<Rule> rules, boolean isOptional) {
    //    StopWatch stopWatch = new StopWatch();
    //    stopWatch.start();
    //    // 记录时间规则用于组装序列的时间串
    //    Map<String, String> timeMap = new HashMap<>();
    //    // 记录数字规则用于组装序列的数字列表
    //    Map<String, LinkedList<Long>> numMap = new HashMap<>();
    //    // 规则组装统一传入参数
    //    RuleParams ruleParams = new RuleParams();
    //    // 配置自选规则在规则拼接中需要的参数
    //    if (isOptional) {
    //        ruleParams.setOptional(true);
    //        ruleParams.setSeqVal(seqParamsDto.getSeqVal());
    //    }
    //    for (Rule rule : rules) {
    //        String ruleType = rule.getRuleType();
    //        if (SequenceConstant.DATE_RUlE.equals(ruleType)) {
    //            // 时间规则预处理 减少格式化时间次数
    //            DateRuleInfo dateRuleInfo = (DateRuleInfo) rule.getRuleData();
    //            SimpleDateFormat sdf = new SimpleDateFormat(dateRuleInfo.getDateFormat());
    //            // 优先使用用户输入的工作时间 默认系统时间
    //            Date date = seqParamsDto.getSeqDate() == null ? new Date() : new Date(seqParamsDto.getSeqDate());
    //            timeMap.put(rule.getRuleId(), sdf.format(date));
    //        } else if (SequenceConstant.NUMBER_RULE.equals(ruleType)) {
    //            // 自选和非自选统一处理 返回可使用的数量序列 用于后续组装
    //            LinkedList<Long> nums = isOptional ? updateNumOptional(rule, seqObj, seqParamsDto, ruleParams) : updateNum(rule, seqObj, seqParamsDto);
    //            // 多个数字规则时候 需要使用规则id标识
    //            numMap.put(rule.getRuleId(), nums);
    //        } else if (SequenceConstant.ENUM_RUlE.equals(ruleType)) {
    //            // 枚举规则特殊处理
    //            updateEnum(rule, seqParamsDto);
    //        }
    //    }
    //    // 保存数字列表到组装参数 ruleParams
    //    ruleParams.setNumMap(numMap);
    //    // 保存时间串到组装参数中
    //    ruleParams.setTime(timeMap);
    //    if (!numMap.isEmpty()) {
    //        // 数字规则不为空 多个数字规则 以能够提供最小生产数量的数字规则数量作为可用于组装的数量
    //        seqParamsDto.setCreateNumber(numMap.values().stream().mapToInt(LinkedList::size).min().orElse(0));
    //    }
    //    List<Object> seqList = new LinkedList<>();
    //    LinkedList<SeqOptionalRecord> seqOptionalRecords = new LinkedList<>();
    //    for (int j = 0; j < seqParamsDto.getCreateNumber(); j++) {
    //        StringBuilder seq = new StringBuilder();
    //        for (Rule rule : rules) {
    //            seq.append(SeqHolder.getRuleHandlerByName(rule.getRuleType()).generateRule(seqObj, rule, ruleParams));
    //        }
    //        String seqNo = seq.toString();
    //        seqList.add(seq);
    //        if (isOptional) {
    //            // 自选请求需要保存自选生产列表
    //            seqOptionalRecords.add(buildOptionalSeq(seqParamsDto, ruleParams, seqNo));
    //        }
    //    }
    //    // 保存自选数据
    //    if (!seqOptionalRecords.isEmpty()) {
    //        StopWatch stopWatch1 = new StopWatch();
    //        stopWatch1.start();
    //        seqOptionalRecordDao.saveBatchOptional(seqOptionalRecords);
    //        stopWatch1.stop();
    //        LOG.error("自选【{}】记录入库：{}", seqParamsDto.getSeqVal(), stopWatch1.getTotalTimeMillis());
    //    }
    //    stopWatch.stop();
    //    LOG.error("组装耗时：{}", stopWatch.getTotalTimeMillis());
    //    return seqList;
    //}


    /**
     * 更新数字结束值
     *
     * @param rule rule
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     * @return LinkedList
     */
    @Transactional(rollbackFor = Exception.class, timeout = 3)
    public LinkedList<Long> updateNum(Rule rule, SeqObj seqObj, SeqParamsDto seqParamsDto) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String seqCode = seqObj.getSequenceCode();
        int createSize = seqParamsDto.getCreateNumber();
        NumberRuleInfo numberRuleInfo = (NumberRuleInfo) rule.getRuleData();
        // 获取数字规则是否开启自选能力 默认不开启
        boolean isOptional = Optional.ofNullable(numberRuleInfo.getOptional()).orElse(false);
        if (isOptional) {
            // 自选尽可能多的一次获取到可用数字，生产两倍
            createSize = createSize * 2;
        }
        // 锁定当前规则自增记录表 全局唯一 开启自选能力的序列：自选序号请求和普通序号请求同时只能有一个可操作
        SeqCurNum seqCurNum = getSeqCurNum(seqParamsDto, rule);
        seqCurNum = seqCurNumService.selectForUpdateById(seqCurNum.getId());
        String nowVal = seqCurNum.getCurVal();
        long step = numberRuleInfo.getNumberStep();
        // 获取起始值 起始值满足条件
        long start = getNumStart(numberRuleInfo, nowVal);
        // 计算结束值
        long end = start + createSize * step;
        LinkedList<Long> numList = seqBuild(seqParamsDto, createSize, start, numberRuleInfo);
        if (numList.isEmpty()) {
            LOG.warn("序列[{}]规则[{}]达到最大值无法构建可用序列", seqCode, rule.getRuleName());
            return numList;
        }
        if (isOptional) {
            // 开启自选 需要过滤已经被自选的序号 通过范围过滤start <= optionalValue <= end  减少操作数据库次数
            List<SeqOptionalRecord> optionalList = seqOptionalRecordService.getRecordByValue(seqCode, start, end);
            // 去除已经自选的序号
            if (!optionalList.isEmpty()) {
                numList.removeAll(optionalList.stream().map(SeqOptionalRecord::getOptionalValue).collect(Collectors.toList()));
                seqOptionalRecordService.updateFilterStatus(optionalList);
            }
            // 数字规则可生产未达到最大值且可用的数字列表 < 需要创建的数量 持续创建
            while (numList.size() < seqParamsDto.getCreateNumber() && !seqParamsDto.isMax()) {
                // 上一次补充已经使用end  当前补充start = end + step
                start = end + step;
                numList.addAll(seqBuild(seqParamsDto, createSize, start, numberRuleInfo));
                if (numList.isEmpty()) {
                    continue;
                }
                // 计算结束值  用于范围查询和最大值记录
                end = start + createSize * step;
                optionalList = seqOptionalRecordService.getRecordByValue(seqCode, start, end);
                if (!optionalList.isEmpty()) {
                    numList.removeAll(optionalList.stream().map(SeqOptionalRecord::getOptionalValue).collect(Collectors.toList()));
                    seqOptionalRecordService.updateFilterStatus(optionalList);
                }
            }
            // 已经自增到最大值且可用的已经没有 直接返回
            if (numList.isEmpty()) {
                LOG.warn("序列[{}]自选规则[{}]达到最大值无法构建可用序列", seqCode, rule.getRuleName());
                return numList;
            }
            // 截取需要的请求数量
            if (numList.size() > seqParamsDto.getCreateNumber()) {
                numList = new LinkedList<>(numList.subList(0, seqParamsDto.getCreateNumber()));
            }
        }
        seqCurNum.setCurVal(String.valueOf(numList.getLast()));
        seqCurNum.setSeqLock(seqCurNum.getSeqLock() + 1);
        // 记录自增记录
        seqCurNumService.updateObject(seqCurNum);
        stopWatch.stop();
        LOG.error("操作数据库时间开销为：{}", stopWatch.getTotalTimeMillis());
        return numList;
    }

    private static LinkedList<Long> seqBuild(SeqParamsDto seqParamsDto, int createSize, long start, NumberRuleInfo numberRuleInfo) {
        LinkedList<Long> numList = new LinkedList<>();
        long step = numberRuleInfo.getNumberStep();
        long numberEnd = numberRuleInfo.getNumberEnd();
        for (int i = 0; i < createSize; i++) {
            long seq = start + i * step;
            if (seq > numberEnd) {
                // 配置到达最大值报错 特殊处理 返回可以新增的数字列表 直接报错无法返回最后一段的可用序号
                if (numberRuleInfo.getNumberCircle() == SequenceConstant.NUMBER_MAX_STRATEGY_EXCEPTION) {
                    // 设置最大值标识
                    seqParamsDto.setMax(true);
                    break;
                } else {
                    // 其余规则可以继续增长 可以继续新增序列
                    seq = SequenceUtil.getSeqNumberCurrentData(seq, numberRuleInfo);
                }
            }
            numList.add(seq);
        }
        return numList;
    }

    /**
     * 更新数字结束值
     *
     * @param rule rule
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     */
    @Transactional(rollbackFor = Exception.class, timeout = 3)
    public LinkedList<Long> updateNumOptional(Rule rule, SeqObj seqObj, SeqParamsDto seqParamsDto, RuleParams ruleParams) {
        LinkedList<Long> numList = new LinkedList<>();
        int needSize = seqParamsDto.getCreateNumber();
        NumberRuleInfo numberRuleInfo = (NumberRuleInfo) rule.getRuleData();
        long step = numberRuleInfo.getNumberStep();
        long numberEnd = numberRuleInfo.getNumberEnd();
        // 锁定当前规则自增记录表 全局唯一 开启自选能力的序列：自选序号请求和普通序号请求同时只能有一个可操作
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqCurNum seqCurNum = getAndSaveIfNoExist(rule.getRuleId(), seqParamsDto);
        //SeqCurNum seqCurNum = getSeqCurNum(seqParamsDto, rule);
        //seqCurNumService.selectForUpdateById(seqCurNum.getId());
        stopWatch.stop();
        LOG.warn("锁住规则【{}】数字递增耗时：{}", rule.getRuleName(), stopWatch.getTotalTimeMillis());
        if (numberRuleInfo.getOptional()) {
            seqParamsDto.setCurNumId(seqCurNum.getId());
            // 序列可能有多个数字规则 开启自选的特殊处理
            String seqCode = seqObj.getSequenceCode();
            String seqVal = seqParamsDto.getSeqVal();
            // 获取当前自选参数补位值
            StopWatch stopWatch1 = new StopWatch();
            stopWatch1.start();
            SeqOptionalPad optionalPad = seqOptionalPadService.getOptionalPad(seqCode, seqVal);
            optionalPad = seqOptionalPadService.selectForUpdatePadById(optionalPad.getId());
            stopWatch1.stop();
            LOG.warn("查询自选【{}】数字递增耗时：{}", seqVal, stopWatch1.getTotalTimeMillis());
            // 获取补位值初始值
            long start = getPadStart(seqCurNum.getCurVal(), optionalPad.getPaddindValue(), seqVal, step);
            // 记录自选值和补位值 用于自选记录实体构建
            Map<Long, Long> padMaps = new HashMap<>();
            // 初次起始值start满足条件
            for (int i = 0; i < needSize; i++) {
                // 输入参数等于或者大于自选位数，特殊处理
                if (seqVal.length() >= String.valueOf(numberEnd).length() && Long.parseLong(seqVal) <= numberEnd) {
                    long seq = Long.parseLong(seqVal);
                    padMaps.put(seq, seq);
                    numList.add(seq);
                    break;
                }
                // 补位值 = 起始值 + 循环数 * 步长  开启自选能力，建议步长为1，否则跳号较多
                long padValue = start + i * step;
                // 自选值 = 补位值 拼接 自选参数
                long seq = Long.parseLong(padValue + seqVal);
                // 自选请求不允许最大值报错 返回可组装的最大自选序号即可
                if (seq > numberEnd) {
                    LOG.warn("自选序列[{}]规则[{}]已达到最大补位值", seqObj.getSequenceCode(), rule.getRuleName());
                    break;
                }
                padMaps.put(seq, padValue);
                numList.add(seq);
            }
            // 保存自选值信息
            ruleParams.setPadMaps(padMaps);
            if (!numList.isEmpty()) {
                // 自选值最后一个一定是最大值
                optionalPad.setPaddindValue(padMaps.get(numList.getLast()));
                optionalPad.setOptionalValue(numList.getLast());
                // 刷新自选补位值信息
                StopWatch stopWatch2 = new StopWatch();
                stopWatch2.start();
                seqOptionalPadService.updateOptionalPad(optionalPad);
                stopWatch2.stop();
                LOG.warn("更新自选【{}】数字递增耗时：{}", seqVal, stopWatch2.getTotalTimeMillis());
            }
        } else {
            seqCurNumService.selectForUpdateById(seqCurNum.getId());
            // 获取普通数字规则其实值
            long start = getNumStart(numberRuleInfo, seqCurNum.getCurVal());
            // 初始起始值start满足条件
            for (int i = 0; i < needSize; i++) {
                // 自增值 = 起始 + 循环次数 * 步长
                long seq = start + i * step;
                if (seq > numberEnd) {
                    LOG.warn("自选序列[{}]规则[{}]已达到最大补位值", seqObj.getSequenceCode(), rule.getRuleName());
                    break;
                }
                numList.add(seq);
            }
            seqCurNum.setCurVal(numList.getLast() + "");
            seqCurNum.setSeqLock(seqCurNum.getSeqLock() + 1);
            // 刷新普通数字规则自增记录
            seqCurNumService.updateObject(seqCurNum);
        }
        return numList;
    }

    private static long getNumStart(NumberRuleInfo numberRuleInfo, String curNum) {
        long step = numberRuleInfo.getNumberStep();
        long numStart = numberRuleInfo.getNumberStart();
        long curVal = Long.parseLong(curNum);
        // 普通自增 < 配置起始值 使用起始值 反之说明当前值已被使用 需要添加步长
        return curVal < numStart ? numStart : curVal + step;
    }

    private static long getPadStart(String curVal, long pad, String seqVal, long step) {
        long nowVal = Long.parseLong(curVal);
        // 获取到的补位值已经使用 需要加步长
        pad = pad + step;
        long value = Long.parseLong(pad + seqVal);
        // 自选值 > 普通自增值 说明普通自增不可能和自选有重号可能 直接返回补位值
        if (value > nowVal) {
            return pad;
        }
        // 自选值 < 普通自增 说明自增号可能已经占用部分自选号 需要计算自选补位值初始值
        int nowLength = curVal.length();
        int valLength = seqVal.length();
        // 减少补位自增次数 直接通过自选参数位数截取普通自增数字 left和right标识截取的左右两段
        String left = curVal.substring(0, nowLength - valLength);
        String right = curVal.substring(nowLength - valLength, nowLength);
        long leftValue = Long.parseLong(left);
        long rightValue = Long.parseLong(right);
        // 比较right和自选参数值大小 自选参数值 > left 说明right满足条件 直接返回right 反之则说明left需要+1才满足条件
        return Long.parseLong(seqVal) > rightValue ? leftValue : leftValue + 1;
    }

    public SeqOptionalRecord buildOptionalSeq(SeqParamsDto seqParamsDto, RuleParams ruleParams, String seqNo) {
        SeqOptionalRecord optionalRecord = new SeqOptionalRecord();
        optionalRecord.setOptionalValue(ruleParams.getOptionalValue());
        optionalRecord.setPaddindValue(ruleParams.getPadValue());
        optionalRecord.setSerialNumber(seqNo);
        optionalRecord.setSeqValue(seqParamsDto.getSeqVal());
        optionalRecord.setCreateDate(new Date());
        optionalRecord.setOptionalStatus(OPTIONAL_STATUS_UN_USE);
        optionalRecord.setFilterStatus(OPTIONAL_STATUS_UN_FILTER);
        optionalRecord.setCurNumId(seqParamsDto.getCurNumId());
        optionalRecord.setSeqCode(seqParamsDto.getSeqCode());
        optionalRecord.setSeqLock(0);
        optionalRecord.setId(SequenceUtil.getUUID32());
        return optionalRecord;
    }

    public SeqCurNum getSeqCurNum(SeqParamsDto seqParamsDto, Rule rule) {
        String key = rule.getRuleId() + ":" + seqParamsDto.getDay();
        SeqCurNum seqCurNum = SeqHolder.getSeqCulNum(key);
        if (seqCurNum == null) {
            seqCurNum = getAndSaveIfNoExist(rule.getRuleId(), seqParamsDto);
            SeqHolder.setSeqCulHandlers(key, seqCurNum);
        }
        return seqCurNum;
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

    /**
     * 更新枚举结束值
     *
     * @param rule enumRuleInfo
     * @param seqParamsDto seqParamsDto
     */
    @Transactional(rollbackFor = Exception.class, timeout = 3)
    public void updateEnum(Rule rule, SeqParamsDto seqParamsDto) {
        EnumRuleInfo enumRuleInfo = (EnumRuleInfo) rule.getRuleData();
        SeqCurNum seqCurNum = getSeqCurNum(seqParamsDto, rule);
        SeqCurNum updatePo = seqCurNumService.selectForUpdateById(seqCurNum.getId());
        int index = enumRuleInfo.getEnumStore().indexOf(updatePo.getCurVal()) + seqParamsDto.getCreateNumber() * enumRuleInfo.getNumberStep();
        if (index > enumRuleInfo.getEnumStore().size() - 1) {
            index = (index - enumRuleInfo.getEnumStore().size()) % (enumRuleInfo.getEnumStore().size());
        } else if (index == enumRuleInfo.getEnumStore().size() - 1) {
            LOG.info("last enum index");
        } else if (index < 0) {
            index = 0;
        } else {
            index = index + enumRuleInfo.getNumberStep();
        }
        updatePo.setCurVal(enumRuleInfo.getEnumStore().get(index));
        updatePo.setSeqLock(updatePo.getSeqLock() + 1);
        seqCurNumService.updateObject(updatePo);
    }


    /**
     * 使用回收序列补仓
     *
     * @param seqObj seqObj
     * @param seqParamsDto seqParamsDto
     * @param blockingQueue blockingQueue
     */
    @Transactional(rollbackFor = Exception.class, timeout = 60)
    public void addCallbackSeq(SeqObj seqObj, SeqParamsDto seqParamsDto, LinkedBlockingDeque<Object> blockingQueue) {
        // 可补仓大小
        int replenishmentSize = seqObj.getSequenceNumber() - blockingQueue.size();
        String seqDesignId = seqObj.getId();
        String rqDay = seqParamsDto.getDay();
        String tenantId = seqParamsDto.getTenantId();
        Map<String, Object> map = new HashMap<>();
        map.put("seqDesignId", seqDesignId);
        map.put("rqDay", rqDay);
        map.put("tenantId", tenantId);
        map.put("limit", replenishmentSize);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<SeqRecycleInfo> recycleInfos = seqRecycleInfoService.getSeqForUpdate(map);
        stopWatch.stop();
        LOG.warn("序号【{}】回收查询耗时：{}", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis());
        StopWatch stopWatch1 = new StopWatch();
        stopWatch1.start();
        Optional.ofNullable(recycleInfos)
                .map(infos -> infos.size() > replenishmentSize ? infos.subList(0, replenishmentSize) : infos)
                .map(infos -> infos.stream()
                        .map(seqRecycleInfo -> {
                            blockingQueue.add(seqRecycleInfo.getRecycleCode());
                            return seqRecycleInfo.getId();
                        })
                        .collect(Collectors.toList()))
                .filter(ids -> !ids.isEmpty())
                .ifPresent(ids -> seqRecycleInfoService.delObjectByIds(ids));
        stopWatch1.stop();
        LOG.warn("序号【{}】回收删除耗时：{}", seqParamsDto.getSeqCode(), stopWatch1.getTotalTimeMillis());
    }

    /**
     * 保存自选序号
     *
     * @param seqOptionalRecords seqOptionalRecords
     */
    public void saveBatchOptional(LinkedList<SeqOptionalRecord> seqOptionalRecords) {
        seqOptionalRecordDao.saveBatchOptional(seqOptionalRecords);
    }
}
