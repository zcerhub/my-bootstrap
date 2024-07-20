package com.dap.sequence.client.service.impl;

import com.dap.sequence.api.dto.EnumRuleInfo;
import com.dap.sequence.api.dto.NumberRuleInfo;
import com.dap.sequence.api.dto.Rule;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.client.core.SequenceQueue;
import com.dap.sequence.client.dao.SeqOptionalRecordDao;
import com.dap.sequence.client.dto.RuleParams;
import com.dap.sequence.client.entity.SeqCurNum;
import com.dap.sequence.client.entity.SeqOptionalRecord;
import com.dap.sequence.client.entity.SeqRecycleInfo;
import com.dap.sequence.client.enums.NumberCircleEnum;
import com.dap.sequence.client.holder.SeqHolder;
import com.dap.sequence.client.service.SeqCurNumService;
import com.dap.sequence.client.service.SeqOptionalPadService;
import com.dap.sequence.client.service.SeqOptionalRecordService;
import com.dap.sequence.client.service.SeqRecycleInfoService;
import com.dap.sequence.client.strategy.*;
import com.dap.sequence.client.utils.SequenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.*;
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

    private static final Integer INTERVAL_VALUE = 10000;

    private int sequenceQueueCapacity = 100;
    /**
     * 从队列中获取序列的超时时间
     */
    private int timeout;
    /**
     * 针对不同达到最大值的配置，选择不同的生成序号数值的方法
     */
    private static Map<NumberCircleEnum, NumberProduceStrategy> numberProduceStrategyMap = new HashMap<>(4);

    static {
        numberProduceStrategyMap.put(NumberCircleEnum.CONTINUING_TO_GROW, new ContinueGrowProduceStrategy());
        numberProduceStrategyMap.put(NumberCircleEnum.STARTING_FROM_SCRATCH, new StartFromScratchProduceStrategy());
        numberProduceStrategyMap.put(NumberCircleEnum.MAXIMUM_VALUE, new MaxValueProduceStrategy());
        numberProduceStrategyMap.put(NumberCircleEnum.THROW_AN_EXCEPTION, new MaxSignProduceStrategy());
    }


    /**
     * 生产序号数值
     *
     * @param rule         rule:数值规则
     * @param seqObj       seqObj
     * @param seqParamsDto seqParamsDto：请求参数
     * @return LinkedList
     */
    @Transactional(rollbackFor = Exception.class, timeout = 3)
    public LinkedList<Long> produceNormalSeqNumList(Rule rule, SeqObj seqObj, SeqParamsDto seqParamsDto) {
        //请求获取序号数量
        int reqSeqSize;
        if(seqParamsDto.getCreateNumber() == null){
            reqSeqSize = seqParamsDto.getRequestNumber();
        }else{
            reqSeqSize = seqParamsDto.getCreateNumber();
        }

        //获取数字规则是否开启自选能力，默认不开启
        NumberRuleInfo numberRuleInfo = (NumberRuleInfo) rule.getRuleData();
        boolean isOptional = Optional.ofNullable(numberRuleInfo.getOptional()).orElse(false);
        //如果数字规则开启了自选，有可能生产处序号已被自选使用，请求数量扩大
        if (isOptional) {
            // 自选尽可能多的一次获取到可用数字，生产两倍
            reqSeqSize = reqSeqSize * 2;
        }
        // 锁定当前规则自增记录表 全局唯一 开启自选能力的序列：自选序号请求和普通序号请求同时只能有一个可操作
        // 获取当前序列
        SeqCurNum seqCurNum = getSeqCurNum(seqParamsDto, rule);
        //利用for update锁定自增记录表行记录，并获取最新记录
        seqCurNum = seqCurNumService.selectForUpdateById(seqCurNum.getId());
        String nowVal = seqCurNum.getCurVal();
        // 结合数字规则中的起始值、当前值及步长，计算序号起始值
        long step = numberRuleInfo.getNumberStep();
        long start = getNumStart(numberRuleInfo, nowVal);
        // 计算结束值
        long end = start + reqSeqSize * step;
        // 到达最大值时的处理流程
        LinkedList<Long> numList = numberProduceStrategyMap.get(NumberCircleEnum.sign(numberRuleInfo.getNumberCircle())).produceNum(
                start, reqSeqSize, numberRuleInfo, seqParamsDto);
        if (CollectionUtils.isEmpty(numList)) {
            LOG.warn("序列[{}]规则[{}]达到最大值无法构建可用序列", seqObj.getSequenceCode(), rule.getRuleName());
            return numList;
        }
        //如果数字规则开启了自选，则要排除掉已经选中的自选序导记录
        String seqCode = seqObj.getSequenceCode();
       if (isOptional) {
            //已生成的自选序号中移除己选中的记录
            // 开启自选 需要过滤已经被自选的序号 通过范围过滤start <= optionalValue <= end  减少操作数据库次数
            removeOptionSeqRecord(seqCode, start, end, numList);
            List<SeqOptionalRecord> optionalList;
            // 数字规则可生产未达到最大值且可用的数字列表 < 需要创建的数量 持续创建
            while (numList.size() < seqParamsDto.getCreateNumber() && !seqParamsDto.isMax()) {
                // 上一次补充已经使用end  当前补充start = end + step
                start = end + step;
                LinkedList<Long> curProduceList = numberProduceStrategyMap.get(NumberCircleEnum.sign(numberRuleInfo.getNumberCircle())).produceNum(
                        start, reqSeqSize, numberRuleInfo, seqParamsDto);
                //当无法在生产序号的数值时 ，break
                if (CollectionUtils.isEmpty(curProduceList)) {
                    break;
                }
                numList.addAll(curProduceList);
                // 计算结束值  用于范围查询和最大值记录
                end = start + reqSeqSize * step;
                //已生成的自选序号中移除已选中的记录
                removeOptionSeqRecord(seqCode,start, end, numList);
            }
            // 已经自增到最大值且可用的已经没有 直接返回
            if (CollectionUtils.isEmpty(numList)) {
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
        return numList;
    }

    /**
     * 根据起始自选值，查询已经选中的记录，从生成的序号数值中移除掉
     *
     * @param seqCode
     * @param start
     * @param end
     * @param numList
     */
    private void removeOptionSeqRecord(String seqCode, long start, long end, LinkedList<Long> numList) {
        List<SeqOptionalRecord> optionalList = seqOptionalRecordService.getRecordByValue(seqCode, start, end);
        // 去除已经自选的序号
        if (!optionalList.isEmpty()) {
            numList.removeAll(optionalList.stream().map(SeqOptionalRecord::getOptionalValue).collect(Collectors.toList()));
            seqOptionalRecordService.updateFilterStatus(optionalList);
        }
    }

    /**
     * 更新数字结束值
     *
     * @param rule         rule
     * @param seqObj       seqObj
     * @param seqParamsDto seqParamsDto
     */
    @Transactional(rollbackFor = Exception.class, timeout = 3)
    public LinkedList<Long> produceOptionalSeqNumList(Rule rule, SeqObj seqObj, SeqParamsDto seqParamsDto, RuleParams ruleParams) {
        NumberRuleInfo numberRuleInfo = (NumberRuleInfo) rule.getRuleData();
        if (numberRuleInfo.getOptional()) {
            // 锁定当前规则自增记录表 全局唯一 开启自选能力的序列：自选序号请求和普通序号请求同时只能有一个可操作
            SeqCurNum seqCurNum = getAndSaveIfNoExist(rule.getRuleId(), seqParamsDto);
            seqParamsDto.setCurNumId(seqCurNum.getId());
            // 序列可能有多个数字规则 开启自选的特殊处理
            String seqVal = seqParamsDto.getSeqVal();
            // 获取补位值初始值
            long start = getPadStart(seqCurNum.getCurVal(), seqVal, INTERVAL_VALUE, numberRuleInfo.getNumberEnd());
            long startFixed = start;
            // 记录自选值和补位值 用于自选记录实体构建
            Map<Long, Long> padMaps = new HashMap<>();
            boolean isMax = false;
            LinkedList<Long> numList = new LinkedList<>();
            long step = numberRuleInfo.getNumberStep();
            long numberEnd = numberRuleInfo.getNumberEnd();
            // 输入参数等于或者大于自选位数，特殊处理
            if (seqVal.length() >= String.valueOf(numberEnd).length() && Long.parseLong(seqVal) <= numberEnd) {
                long seq = Long.parseLong(seqVal);
                padMaps.put(seq, seq);
                numList.add(seq);
                removeOptRecordFromNumList(seqObj.getSequenceCode(), seq, seq, numList);
                return numList;
            }
            //请求序号数量
            int reqSeqSize = seqParamsDto.getCreateNumber();
            while (numList.size() < reqSeqSize && !isMax) {
                for (int i = 0; i < reqSeqSize; i++) {
                    // 补位值 = 起始值 + 循环数 * 步长  开启自选能力，建议步长为1，否则跳号较多
                    long padValue = start + i * step;
                    // 自选值 = 补位值 拼接 自选参数
                    long seq = Long.parseLong(padValue + seqVal);
                    //如果序列到了最大值则停止生产自选序号
                    String curVal = seqCurNum.getCurVal();
                    if (seq > numberEnd || seq <= Long.parseLong(curVal)) {
                        LOG.warn("自选序列[{}]规则[{}]已达到最大值", seqObj.getSequenceCode(), rule.getRuleName());
                        isMax = true;
                        break;
                    }
                    padMaps.put(seq, padValue);
                    numList.add(seq);
                }
                String numberEndStr = String.valueOf(numberEnd);
                String padMax = numberEndStr.substring(0, numberEndStr.length()- seqVal.length());
                //计算自选序号数值去掉自选参数后的结束值； 自选序号数值：end字符串+seqVal
                long end = Math.min(start + reqSeqSize *step, Long.parseLong(padMax));
                long endOptionalSeq = Long.parseLong(end + seqVal);
                long startOptionalSeq = Long.parseLong(start + seqVal);
                //查询自选表中该范围内已经被选中的序号
                removeOptRecordFromNumList(seqObj.getSequenceCode(), startOptionalSeq, endOptionalSeq, numList);
                start = start + reqSeqSize * step;
            }
            LinkedList<Long> numIncreaseList = new LinkedList<>();
            //达到最大值后处理逻辑
            if (isMax) {
                //获取补位值初始值
                long originalPad = getPadStart(seqCurNum.getCurVal(), seqVal, 0, numberRuleInfo.getNumberEnd());
                //去自选库中查询已经被选中的序号
                removeOptRecordFromNumList(seqObj.getSequenceCode(), Long.parseLong(originalPad + seqVal), Long.parseLong(start + seqVal), numList);
                if (reqSeqSize > numList.size()) {
                    //根据补位值的原始值和初始值来生产序列，然后排除掉已经选中的序列，再截取需要数量的序号
                    for (long i = originalPad; i < startFixed; i++) {
                        long seq = Long.parseLong(i + seqVal);
                        numIncreaseList.add(seq);
                    }
                    long supplementNum = (long) reqSeqSize - numList.size();
                    if (numIncreaseList.size() <= supplementNum) {
                        numList.addAll(numIncreaseList);
                    } else {
                        numList.addAll(numIncreaseList.subList((int) (numIncreaseList.size() - supplementNum), numIncreaseList.size()));
                    }
                    removeOptRecordFromNumList(seqObj.getSequenceCode(), Long.parseLong(originalPad + seqVal), Long.parseLong(start + seqVal), numList);
                }
            }
            numList = new LinkedList<>(numList.size() > reqSeqSize ? numList.subList(0, reqSeqSize) : numList);
            Collections.sort(numList);

            // 保存自选值信息
            ruleParams.setPadMaps(padMaps);
            return numList;
        } else {
            return getOptionalNormalNumList(rule, seqObj, seqParamsDto, numberRuleInfo);
        }
    }

    /**
     *
     * @param rule 序号配置中的数字规则
     * @param seqObj 序列配置对象
     * @param seqParamsDto 序号清求参数
     * @param numberRuleInfo
     */
    private LinkedList<Long> getOptionalNormalNumList(Rule rule, SeqObj seqObj, SeqParamsDto seqParamsDto, NumberRuleInfo numberRuleInfo) {
        // 锁定当前规则自增记录表 全局唯一 开启自选能力的序列：自选序号请求和普通序号请求同时只能有一个可操作
        SeqCurNum seqCurNum = getAndSaveIfNoExist(rule.getRuleId(), seqParamsDto);
        seqCurNumService.selectForUpdateById(seqCurNum.getId());
        // 获取普通数字规则其实值
        long start = getNumStart(numberRuleInfo, seqCurNum.getCurVal());
        long step = numberRuleInfo.getNumberStep();
        long numberEnd = numberRuleInfo.getNumberEnd();
        //请求的序号数量
        int reqSeqSize = seqParamsDto.getCreateNumber();
        LinkedList<Long> numList = new LinkedList<>();
        for (int i = 0; i < reqSeqSize; i++) {
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
        return numList;
    }

    /**
     * 移除选中的记录
     *
     * @param seqCode
     * @param startOptionalSeq
     * @param endOptionalSeq
     * @param numList
     */
    private void removeOptRecordFromNumList(String seqCode, long startOptionalSeq, long endOptionalSeq, LinkedList<Long> numList) {
        SeqOptionalRecord seqOptionalRecordParam = new SeqOptionalRecord();
        seqOptionalRecordParam.setSeqCode(seqCode);
        seqOptionalRecordParam.setStart(startOptionalSeq);
        seqOptionalRecordParam.setEnd(endOptionalSeq);
        List<SeqOptionalRecord> seqOptionalRecords = seqOptionalRecordDao.getRecordByValue(seqOptionalRecordParam);
        Optional.ofNullable(seqOptionalRecords).map(list -> list.stream().map(SeqOptionalRecord::getOptionalValue).collect(Collectors.toList())).ifPresent(numList::removeAll);
    }

    /**
     * 数字规则的起始信和当前序号中的数字段，结台数字规则步长计算下一个序号开始的数字段
     * 起始值或者当前序号的数字段+步长
     *
     * @param numberRuleInfo 数字规则，使用数字规则的起始值和步长
     * @param curNum 当前序掠准号的数字段
     * @return
     */
    private static long getNumStart(NumberRuleInfo numberRuleInfo, String curNum) {
        long step = numberRuleInfo.getNumberStep();
        long numStart = numberRuleInfo.getNumberStart();
        long curVal = Long.parseLong(curNum);
        // 普通自增 < 配置起始值 使用起始值 反之说明当前值已被使用 需要添加步长
        return curVal < numStart ? numStart : curVal + step;
    }

    /**
     *
     * @param curVal
     * @param seqVal
     * @param increase
     * @param numberEnd
     * @return
     */
    private static long getPadStart(String curVal, String seqVal, long increase, long numberEnd) {
        long nowVal = Long.parseLong(curVal);
        long seqValLong = Long.parseLong(seqVal);
        long intervalValue = nowVal + increase;
        double pow = Math.pow(10, seqVal.length());
        //判断补位值是否到达最大值
        String numberEndStr =String.valueOf(numberEnd);
        //计算自选序号数值最大值左侧补位数值最大值:padMax;自选序号数值最大值:最大补位值+自选segVa
        String padMax = numberEndStr.substring(0,numberEndStr.length()- seqVal.length());
        return Math.min((long)Math.ceil((intervalValue -seqValLong)/ pow), Long.parseLong(padMax));
    }

    /**
     * 构建自选记录
     *
     * @param seqParamsDto
     * @param ruleParams
     * @param seqNo
     * @return
     */
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

    /**
     * 锁定当前规则自增记录表
     *
     * @param ruleId
     * @param seqParamsDto
     * @return
     */
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
     * @param rule         enumRuleInfo
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
     * @param seqObj        seqObj
     * @param seqParamsDto  seqParamsDto
     * @param blockingQueue blockingQueue
     */
    @Transactional(rollbackFor = Exception.class, timeout = 60)
    public void addCallbackSeq(SeqObj seqObj, SeqParamsDto seqParamsDto, SequenceQueue sequenceQueue) {
        // 可补仓大小
        int sequenceQueueSize= sequenceQueue.getQuenList().size();
        int replenishmentSize = seqObj.getSequenceNumber() - sequenceQueueSize;
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
        List<Object> seqList = new ArrayList<>();
        Optional.ofNullable(recycleInfos)
                .map(infos -> infos.size() > replenishmentSize ? infos.subList(0, replenishmentSize) : infos)
                .map(infos -> infos.stream()
                        .map(seqRecycleInfo -> {
                            seqList.add(seqRecycleInfo.getRecycleCode());
                            return seqRecycleInfo.getId();
                        })
                        .collect(Collectors.toList()))
                .filter(ids -> !ids.isEmpty())
                .ifPresent(ids -> seqRecycleInfoService.delObjectByIds(ids));
        sequenceQueue = new SequenceQueue(seqList, sequenceQueueCapacity, timeout, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
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
