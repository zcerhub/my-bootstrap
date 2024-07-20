package com.dap.sequence.client.core;


import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.DateUtils;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.api.util.SequenceUtils;
import com.dap.sequence.client.entity.SeqDesignPo;
import com.dap.sequence.client.entity.SeqInstanceRule;
import com.dap.sequence.client.factory.AbstractSeqFlowEngine;
import com.dap.sequence.client.holder.SeqHolder;
import com.dap.sequence.client.service.SeqCacheCleaningService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @className SequenceQueueFactory
 * @description 序列队列工厂类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SequenceQueueFactory {

    private static final Logger logger = LoggerFactory.getLogger(SequenceQueueFactory.class);

    private static final int INT_10 = 10;

    private static final int INT_100 = 100;

    private static final int NEGATIVE_2 = -2;

    private static final int DEFAULT_CACHE_THRESHOLD = 30;

    /**
     * 序列对象缓存
     */
    public static final ConcurrentHashMap<String, SequenceQueue> SEQUENCE_QUEUE_MAP = new ConcurrentHashMap<>();
    /**
     * 序列本地缓存规则
     */
    public static final ConcurrentHashMap<String, SeqDesignPo> Seq_Design_MAP = new ConcurrentHashMap<>();

    /**
     * 序列编排规则
     */
    public static final ConcurrentHashMap<String, List<SeqInstanceRule>> Seq_rule_MAP = new ConcurrentHashMap<>();

    /**
     * 序列设计器对象缓存
     */
    public static final ConcurrentHashMap<String, SeqObj> SEQUENCE_DESIGN_MAP = new ConcurrentHashMap<>();

    /**
     * 线程名称
     */
    private static final ThreadFactory NAME_FACTORY = new ThreadFactoryBuilder().setNameFormat("seq-thread-makeUpWarehouse-%d").build();

    /**
     * 异步线程
     */
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(INT_10, INT_100, INT_10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(200), NAME_FACTORY);

    /**
     * 队列最大容量
     */
    private int sequenceQueueCapacity = 100;

    /**
     * 缓存号段数量 ，默认双号段 压测时可适当调大一点
     */
    private int bufferCount = 2;

    /**
     * 从队列中获取序列的超时时间
     */
    private int timeout;


    SeqConsumer seqConsumer;
    SeqCacheCleaningService seqCacheCleaningService;

    public SequenceQueueFactory(SeqConsumer seqConsumer) {
        this.seqConsumer = seqConsumer;
    }

    public SequenceQueueFactory(SeqConsumer seqConsumer, SeqCacheCleaningService seqCacheCleaningService) {
        this.seqConsumer = seqConsumer;
        this.seqCacheCleaningService = seqCacheCleaningService;
    }

    /**
     * 获取序列参数校验
     *
     * @param seqParamsDto
     * @param date
     */
    private void checkSeq(SeqParamsDto seqParamsDto, Date date) {
        SeqObj seqDesignPo = SEQUENCE_DESIGN_MAP.get(seqParamsDto.getSeqCode());
        if (seqDesignPo == null) {
            seqDesignPo = getSeqDesignObj(seqParamsDto);
        }
        String callbackMode = Optional.ofNullable(seqDesignPo).map(SeqObj::getCallbackMode).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_NOT_FOUND,seqParamsDto.getSeqCode()));
        if (StringUtils.isBlank(seqParamsDto.getDay())) {
            seqParamsDto.setDay(SequenceUtils.getSeqDay(callbackMode, date));
        }
        seqParamsDto.setSeqDate(date.getTime());
        seqParamsDto.setCreateNumber(seqDesignPo.getRequestNumber());
    }

    /**
     * 获取序列对象
     * @param seqParamsDto
     * @return
     */
    public synchronized SeqObj getSeqDesignObj(SeqParamsDto seqParamsDto) {
        if (SEQUENCE_DESIGN_MAP.containsKey(seqParamsDto.getSeqCode())) {
            return SEQUENCE_DESIGN_MAP.get(seqParamsDto.getSeqCode());
        }
        SeqObj seqObj = seqConsumer.getSeqDesignObj(seqParamsDto.getSeqCode());
        if (seqObj != null) {
            SEQUENCE_DESIGN_MAP.put(seqParamsDto.getSeqCode(), seqObj);
        }
        return seqObj;
    }

    /**
     * 获取自选序列
     *
     * @param seqParamsDto seqParamsDto
     * @param date         date
     * @return Object
     */
    public List<Object> getIncreaseSequence(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(getIncreaseSeqFormServer(seqParamsDto)).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode()));
    }

    /**
     * 获取回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @param date         date
     * @return Object
     */
    public Object getRecoverySequence(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(getRecoverySeqFormServer(seqParamsDto)).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode()));
    }

    /**
     * 获取回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @param date         date
     * @return Object
     */
    public Boolean recoverySequence(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(recoverySequence(seqParamsDto)).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode()));
    }


    /**
     * 获取自选序列
     *
     * @param seqParamsDto seqParamsDto
     * @param date         date
     * @return Object
     */
    public Object getSequenceBase(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(getSequenceFromCache(seqParamsDto)).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode()));
    }


    /**
     * 从队列中获取序列
     *
     * @param seqParamsDto seqParamsDto
     * @return Object
     */
    private Object getSequenceFromCache(SeqParamsDto seqParamsDto) {
        String clientCacheKey = seqParamsDto.clientCacheKey();
        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(seqParamsDto.clientCacheKey());
        if (isQueueEmpty(sequenceQueue)) {
            if ("1".equals(seqParamsDto.getSequenceProdType())){
                SeqDesignPo seqDesignPo = Seq_Design_MAP.get(seqParamsDto.getSeqCode());
                logger.warn("sequenceQueue 序列不存在,生产类型为：本地生成,本地生成中, seqCode:{} ", clientCacheKey);
                FirstLocalCreateSeq(seqDesignPo,seqParamsDto);
                SequenceQueue newSequenceQueue = SEQUENCE_QUEUE_MAP.get(clientCacheKey);
                return  newSequenceQueue.getCacheSeq();
            }else {
                logger.debug("sequenceQueue 不存在从服务端获取的, seqCode:{} ", clientCacheKey);
                return getFirstSeqFormServer(seqParamsDto);
            }
        } else {
            return getSequenceFromLocalCache(seqParamsDto, sequenceQueue);
        }
    }

    /**
     * 队列是否为空
     *
     * @param sequenceQueue
     * @return
     */
    private boolean isQueueEmpty(SequenceQueue sequenceQueue) {
        return null == sequenceQueue || CollectionUtils.isEmpty(sequenceQueue.getCacheBlockingQueue());
    }

    /**
     * 获取本地缓存中序列
     *
     *
     * @param seqParamsDto  seqParamsDto
     * @param sequenceQueue sequenceQueue
     */
    public Object getSequenceFromLocalCache(SeqParamsDto seqParamsDto, SequenceQueue sequenceQueue) {
        Object seq = sequenceQueue.getCacheSeq();
        syncMakeUpWare(seqParamsDto, sequenceQueue);
        if (!sequenceQueue.setNextDaySeqIfAbsent()) {
            SeqObj seqDesignPo = SEQUENCE_DESIGN_MAP.get(seqParamsDto.getSeqCode());
            String dateBefore;
            switch (seqDesignPo.getCallbackMode()) {
                case SequenceConstant.CALLBACK_MODE_DAY:
                    getNextDateSeq(seqParamsDto, sequenceQueue);
                    dateBefore = DateUtils.getPrveDay(seqParamsDto.getDay(), NEGATIVE_2);
                    break;
                case SequenceConstant.CALLBACK_MODE_MONTH:
                    dateBefore = DateUtils.getPrveMonth(seqParamsDto.getDay(), NEGATIVE_2);
                    break;
                case SequenceConstant.CALLBACK_MODE_YEAR:
                    dateBefore = DateUtils.getPrveYear(seqParamsDto.getDay(), NEGATIVE_2);
                    break;
                default:
                    logger.debug("全局序列{}不需要清理", seqDesignPo.getSequenceCode());
                    return seq;
            }
            String seqCodeBefore = SeqKeyUtils.getSeqClientCacheKey(seqParamsDto.getSeqCode(), dateBefore);
            logger.debug("清理缓存序列 seqCodeBefore={}", seqCodeBefore);
            SEQUENCE_QUEUE_MAP.remove(seqCodeBefore);
        }
        return seq;
    }

    /**
     * 异步补仓
     *
     * @param seqParamsDto
     * @param sequenceQueue
     */
    private synchronized void syncMakeUpWare(final SeqParamsDto seqParamsDto, final SequenceQueue sequenceQueue) {
        BlockingQueue<Object> queue = sequenceQueue.getCacheBlockingQueue();
        int queueSize = Optional.ofNullable(queue).map(Collection::size).orElse(0);
        // 默认阈值使用量超过30% 开始补仓
        SeqObj seqDesignPo = SEQUENCE_DESIGN_MAP.get(seqParamsDto.getSeqCode());
        Integer clientCacheThreadhold = Optional.ofNullable(seqDesignPo.getClientCacheThreshold()).orElse(DEFAULT_CACHE_THRESHOLD);
        // 默认缓存两段的配置长度
        boolean isMakeUp = sequenceQueue.getPublishCount() * clientCacheThreadhold > queueSize * INT_100;
        if (sequenceQueue.getQuenList().size() < bufferCount && isMakeUp) {
            logger.info("开始补仓seqCode={} , queueSize={}", seqParamsDto.clientCacheKey(), queueSize);
            makeUpWarehouse(seqParamsDto, sequenceQueue);
        }
    }

    /**
     * 异步获取明天的序列
     *
     * @param seqParamsDto
     * @param sequenceQueue
     */
    private void getNextDateSeq(SeqParamsDto seqParamsDto, SequenceQueue sequenceQueue) {
        // 获取明天日期
        String nextDate = DateUtils.getNextDateWithMill(seqParamsDto.getSeqDate());
        SeqParamsDto nextSeqParamsDto = new SeqParamsDto(seqParamsDto.getSeqCode(), nextDate);
        nextSeqParamsDto.setSeqDate(DateUtils.getNextMillWithMill(seqParamsDto.getSeqDate()));
        // 异步获取明天的序列
        logger.debug("从远程服务器获取下一日序列 seqCodeNext={}", nextSeqParamsDto);
        makeUpWarehouseNextDay(nextSeqParamsDto, sequenceQueue);
    }

    /**
     * 首次从远程获取序列数据
     *
     * @param seqParamsDto seqParamsDto
     * @return Object
     */
    public synchronized Object getFirstSeqFormServer(SeqParamsDto seqParamsDto) {
        String clientCacheKey = seqParamsDto.clientCacheKey();
        logger.debug("开始远程获取序列号... , seqCode = {}", clientCacheKey);
        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(clientCacheKey);
        if (sequenceQueue != null && !isQueueEmpty(sequenceQueue)) {
            logger.debug("其他线程已获取到序列seqCode ={}，从本地缓存中获取...", clientCacheKey);
            return getSequenceFromLocalCache(seqParamsDto, sequenceQueue);
        }
        sequenceQueue = getSeqArrayFromServer(seqParamsDto, sequenceQueue);

        return sequenceQueue.getCacheSeq();
    }


    /**
     * 获取远程服务端一组序列
     *
     * @param seqParamsDto  seqParamsDto
     * @param sequenceQueue sequenceQueue
     * @return SequenceQueue SequenceQueue
     * @throws SequenceException SequenceException
     */
    public SequenceQueue getSeqArrayFromServer(SeqParamsDto seqParamsDto, SequenceQueue sequenceQueue) {

        String clientCacheKey = seqParamsDto.clientCacheKey();
        // 1.远程获取序列数据
        SeqTransferDto transferDto = seqConsumer.getSeqFormServer(seqParamsDto);
        if (transferDto == null) {
            logger.error("从服务端获取序列为空，序列编号：{}", clientCacheKey);
            throw new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode());
        }
        List<Object> list = transferDto.getList();
        Object start = getSeq(list);

        logger.debug("从序列服务器获取{}, seqSize={} , start : {}", clientCacheKey,list.size(), start);

        if (sequenceQueue == null) {
            // 2.创建序列缓存对象
            sequenceQueue = new SequenceQueue(list, sequenceQueueCapacity, timeout, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
            // 4.放入缓存中
            SEQUENCE_QUEUE_MAP.put(clientCacheKey, sequenceQueue);
        } else {
            sequenceQueue.getQuenList().add(new ArrayBlockingQueue<>(list.size(), true, list));
        }

        sequenceQueue.setPublishCount(list.size());
        return sequenceQueue;
    }

    /**
     * 远程服务获取严格递增序号
     *
     * @param seqParamsDto seqParamsDto
     * @return seq seq
     */
    public List<Object> getIncreaseSeqFormServer(SeqParamsDto seqParamsDto) {
        SeqTransferDto transferDto = seqConsumer.getIncreaseSeqFormServer(seqParamsDto);
        if (transferDto == null) {
            logger.error("从服务端获取严格递增序列为空，序列编号：{}", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode());
        }
        List<Object> list = transferDto.getList();
        logger.debug("从序列服务器获取严格递增{}, seqSize={} ", seqParamsDto.getSeqCode(),  list.size());
        return list;
    }

    /**
     * 远程服务获取回收序号
     *
     * @param seqParamsDto seqParamsDto
     * @return seq seq
     */
    public Object getRecoverySeqFormServer(SeqParamsDto seqParamsDto) {
        SeqTransferDto transferDto = seqConsumer.getRecoverySeqFormServer(seqParamsDto);
        if (transferDto == null) {
            logger.error("从服务端获取回收序列为空，序列编号：{}", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR,seqParamsDto.getSeqCode());
        }
        List<Object> list = transferDto.getList();
        Object seq = getSeq(list);
        logger.debug("从序列服务器获取回收{}, seqSize={} , seq : {}", seqParamsDto.getSeqCode(), list.size(), seq);
        return seq;
    }

    /**
     * 远程服务回收序号
     *
     * @param seqParamsDto seqParamsDto
     * @return Boolean Boolean
     */
    public Boolean recoverySequence(SeqParamsDto seqParamsDto) {
        SeqTransferDto transferDto = seqConsumer.recoverySequence(seqParamsDto);
        if (transferDto == null) {
            logger.error("从服务端回收序列为空，序列编号：{}", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY,seqParamsDto.getSeqCode());
        }
        logger.debug("从序列服务器回收{}", seqParamsDto.getSeqCode());
        return true;
    }

    /**
     * 获取第一个序列
     *
     * @param list
     * @return
     */
    private static Object getSeq(List<Object> list) {
        return Optional.ofNullable(list).filter(objects -> !CollectionUtils.isEmpty(objects)).map(objects -> objects.get(0)).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
    }

    /**
     * 序列补仓
     *
     * @param seqParamsDto  seqParamsDto
     * @param sequenceQueue sequenceQueue
     */
    public void makeUpWarehouse(final SeqParamsDto seqParamsDto, final SequenceQueue sequenceQueue) {
        AtomicBoolean isHold = sequenceQueue.getIsHold();
        if (!isHold.getAndSet(true)) {
            EXECUTOR.execute(() -> {
                if("1".equals(seqParamsDto.getSequenceProdType())){
                    AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
                    LocalSeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
                    try {
                        getSeqArrayFormLocal(seqParamsDto, seqProducer,sequenceQueue);
                        isHold.compareAndSet(true,false);
                    } catch (Exception e) {
                        logger.error("客户端异步补仓异常", e);
                        throw new SequenceException(ExceptionEnum.CLIENT_MAKEUP_WAREHOUSE_ERROR);
                    }
                }else {
                try {
                    getSeqArrayFromServer(seqParamsDto, sequenceQueue);
                    isHold.compareAndSet(true,false);
                } catch (Exception e) {
                    logger.error("补仓异常", e);
                }
                }
            });
        }
    }

    /**
     * 序列补仓 -补第二天的 如果补仓失败重置补仓状态
     *
     * @param seqParamsDto  seqParamsDto
     * @param sequenceQueue sequenceQueue
     */
    public void makeUpWarehouseNextDay(final SeqParamsDto seqParamsDto, final SequenceQueue sequenceQueue) {
        logger.debug("开始补仓第二天seqCode={}", seqParamsDto);
        EXECUTOR.execute(() -> {
            if("1".equals(seqParamsDto.getSequenceProdType())){
                AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
                LocalSeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
                try {
                    getSeqArrayFormLocal(seqParamsDto, seqProducer,null);
                } catch (Exception e) {
                    logger.error("补仓第二天异常.msg = {}", e.getMessage(), e);
                    //重置补仓状态
                    sequenceQueue.getHasNextDaySeq().compareAndSet(true, false);
                }
            }else {
                try {
                    getSeqArrayFromServer(seqParamsDto, null);
                } catch (Exception e) {
                    logger.error("补仓第二天异常.", e);
                    //重置补仓状态
                    sequenceQueue.getHasNextDaySeq().compareAndSet(true, false);
                }
            }
        });
    }

    public void setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
    }

    public void setSequenceQueueCapacity(int sequenceQueueCapacity) {
        this.sequenceQueueCapacity = sequenceQueueCapacity;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /***
     *
     * 获取序列信息
     *
     * @param seqCode
     * @return {@link SeqObj}
     * @throws
     * @author yzp
     * @date 2024/6/11 0011 15:57
     */
    public static SeqObj getSeqObj(String seqCode) {
        if (Objects.isNull(seqCode)) {
            return null;
        }
        return SequenceQueueFactory.SEQUENCE_DESIGN_MAP.get(seqCode);
    }
    public void FirstLocalCreateSeq(SeqDesignPo seqDesignPo, SeqParamsDto seqParamsDto) {
        try {
            AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
            if (seqFlowEngine == null || seqFlowEngine.getSeqProducer() == null) {
                logger.warn("序列[{}]引擎未加载完成,不进行序号生成", seqDesignPo.getSequenceName());
                return;
            }
            LocalSeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
            seqProducer.createSeqAndCache(seqFlowEngine.getSeqObj(), seqParamsDto);

        }
        catch (Exception e) {
            logger.error("cacheSequence {} error.msg = {}", seqDesignPo.getSequenceCode(), e.getMessage(), e);
        }
    }
    public void refreshLocalCache(SeqDesignPo seqDesignPo, SeqParamsDto seqParamsDto) {
        try {
            AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
            if (seqFlowEngine == null || seqFlowEngine.getSeqProducer() == null) {
                logger.warn("序列[{}]引擎未加载完成,不进行序号生成", seqDesignPo.getSequenceName());
                return;
            }
            LocalSeqProducer seqProducer = seqFlowEngine.getSeqProducer().get();
            seqProducer.refreshSeqAndCache(seqFlowEngine.getSeqObj(), seqParamsDto);

        }
        catch (Exception e) {
            logger.error("cacheSequence {} error.msg = {}", seqDesignPo.getSequenceCode(), e.getMessage(), e);
        }
    }

    public SequenceQueue getSeqArrayFormLocal(SeqParamsDto seqParamsDto,  LocalSeqProducer seqProducer,SequenceQueue sequenceQueue) {
        AbstractSeqFlowEngine seqFlowEngine = SeqHolder.getEngineMap().get(seqParamsDto.engineCacheKey());
        String clientCacheKey = seqParamsDto.clientCacheKey();
        // 1.远程获取序列数据
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //SeqTransferDto transferDto = seqConsumer.getSeqFormServer(seqParamsDto);
        List<Object> objects = seqProducer.createSeq(seqFlowEngine.getSeqObj(), seqParamsDto);
        stopWatch.stop();
        if (objects.size() == 0) {
            logger.warn("本地生成为空，序列编号：{}", clientCacheKey);
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR);
        }
        //List<Object> list = transferDto.getList();
        Object start = getSeq(objects);
        logger.info("本地生成序列{}序列耗时：{} , seqSize={} , start : {}", clientCacheKey, stopWatch.getTotalTimeMillis(), objects.size(), start);

        if (sequenceQueue == null) {
            // 2.创建序列缓存对象
            sequenceQueue = new SequenceQueue(objects, sequenceQueueCapacity, timeout, seqParamsDto.getSeqCode(), seqParamsDto.getDay());
            // 4.放入缓存中
            SEQUENCE_QUEUE_MAP.put(clientCacheKey, sequenceQueue);
        } else {
            sequenceQueue.getQuenList().add(new ArrayBlockingQueue<>(objects.size(), true, objects));
        }

        sequenceQueue.setPublishCount(objects.size());
        return sequenceQueue;
    }
}
