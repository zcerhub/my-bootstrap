package com.dap.sequence.client.core;


import cn.hutool.core.util.StrUtil;
import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.api.dto.SeqParamsDto;
import com.dap.sequence.api.dto.SeqTransferDto;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import com.dap.sequence.api.lock.LockImpl;
import com.dap.sequence.api.obj.SegmentBuffer;
import com.dap.sequence.api.obj.SeqObj;
import com.dap.sequence.api.util.DateUtils;
import com.dap.sequence.api.util.SeqKeyUtils;
import com.dap.sequence.api.util.SequenceUtils;
import com.dap.sequence.client.constant.SequenceProperties;
import com.dap.sequence.client.service.SeqCacheCleaningService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.*;

/**
 * @className SequenceQueueFactory
 * @description 序列队列工厂类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SequenceQueueFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceQueueFactory.class);

    private static final int DEFAULT_CACHE_THRESHOLD = 30;

    private static final int INT_10 = 10;

    private static final int INT_100 = 100;

    private static final long LONG_100 = 100L;

    private static final int NEGATIVE_2 = -2;

    /**
     * 从队列中获取序列的超时时间 默认5秒
     */
    private static final long TIMEOUT = 5000;

    /**
     * 序列对象缓存
     */
    public static final ConcurrentHashMap<String, SequenceQueue> SEQUENCE_QUEUE_MAP = new ConcurrentHashMap<>();

    /**
     * 序列l队列缓存
     */
    public static final ConcurrentHashMap<String, ArrayBlockingQueue<Object>> SEQUENCE_CACHE = new ConcurrentHashMap<>();

    /**
     * 双buffer缓存
     */
    private static final Map<String, SegmentBuffer> BUFFER_MAP = new ConcurrentHashMap<>();

    /**
     * 一个Segment维持时间为1分钟
     */
    private static final long SEGMENT_DURATION = 60 * 1000L;

    /**
     * 序列设计器对象缓存
     */
    public static final ConcurrentHashMap<String, SeqObj> SEQUENCE_DESIGN_MAP = new ConcurrentHashMap<>();

    /**
     * 异步线程
     */
    private static ExecutorService executor = new ThreadPoolExecutor(INT_10, INT_100, INT_10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(20));

    /**
     * 服务端请求锁
     */
    private final LockImpl<String> lockByKey = new LockImpl<>();

    /**
     * 序列是否启用缓存
     */
    private boolean useSequenceCache = true;

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

    private SeqCacheCleaningService seqCacheCleaningService;

    public SequenceQueueFactory(SeqConsumer seqConsumer) {
        this.seqConsumer = seqConsumer;
    }


    private void checkSeq(SeqParamsDto seqParamsDto, Date date) {
        SeqObj seqDesignPo = SEQUENCE_DESIGN_MAP.get(seqParamsDto.getSeqCode());
        if (seqDesignPo == null) {
            seqDesignPo = getSeqDesignObj(seqParamsDto);
        }
        String callbackMode = Optional.ofNullable(seqDesignPo).map(SeqObj::getCallbackMode).orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_NOT_FOUND));
        if (StringUtils.isBlank(seqParamsDto.getDay())) {
            seqParamsDto.setDay(SequenceUtils.getSeqDay(callbackMode, date));
        }
        seqParamsDto.setSeqDate(date.getTime());
    }

    private synchronized SeqObj getSeqDesignObj(SeqParamsDto seqParamsDto) {
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
     * @param date date
     * @return Object
     */
    public Object getIncreaseSequence(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(getIncreaseSeqFormServer(seqParamsDto))
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
    }

    /**
     * 获取回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @param date date
     * @return Object
     */
    public Object getRecoverySequence(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(getRecoverySeqFormServer(seqParamsDto))
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
    }

    /**
     * 获取回收序列
     *
     * @param seqParamsDto seqParamsDto
     * @param date date
     * @return Object
     */
    public Boolean recoverySequence(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(recoverySequence(seqParamsDto))
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
    }


    /**
     * 获取自选序列
     *
     * @param seqParamsDto seqParamsDto
     * @param date date
     * @return Object
     */
    public Object getSequenceBase(SeqParamsDto seqParamsDto, Date date) {
        checkSeq(seqParamsDto, date);
        return Optional.ofNullable(getSequenceFromCache(seqParamsDto))
                .orElseThrow(() -> new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY));
    }


    /**
     * 从队列中获取序列
     *
     * @param seqParamsDto  seqParamsDto
     * @return Object
     */
    private Object getSequenceFromCache(SeqParamsDto seqParamsDto) {
        String clientCacheKey = seqParamsDto.clientCacheKey();
        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(seqParamsDto.clientCacheKey());
        if (isQueueEmpty(sequenceQueue)) {
            LOGGER.warn("sequenceQueue 不存在从服务端获取的, seqCode:{} ", clientCacheKey);
            return getFirstSeqFormServer(seqParamsDto);
        } else {
            return getSequenceFromLocalCache(seqParamsDto, sequenceQueue);
        }
    }


    /**
     * 异步补充
     *
     * @param seqParamsDto seqParamsDto
     */
    public void asyncSupplement(SeqParamsDto seqParamsDto) {
        if (isAsyncSupplement(seqParamsDto)) {
            executor.execute(() -> {
                try {
                    LOGGER.info("***** seqCode = {} 开始异步补仓 *****", seqParamsDto.clientCacheKey());
                    getSeqFormServer(seqParamsDto);
                } catch (Exception e) {
                    LOGGER.error("补仓异常.msg:{}", e.getMessage(), e);
                }
            });
        }
    }

    private boolean isAsyncSupplement(SeqParamsDto seqParamsDto) {
        String seqCode = seqParamsDto.getSeqCode();
        SeqObj seqObj = SEQUENCE_DESIGN_MAP.get(seqCode);
        if (seqObj == null || seqObj.getSequenceNumber() == 0) {
            return false;
        }
        if (isEmpty(seqParamsDto.clientCacheKey())) {
            return true;
        }
        ArrayBlockingQueue<Object> queue = SEQUENCE_CACHE.get(seqParamsDto.clientCacheKey());
        int queueSize = Optional.ofNullable(queue).map(ArrayBlockingQueue::size).orElse(0);
        // 默认阈值使用量超过30% 开始补仓
        int threshold = Optional.ofNullable(seqObj.getClientCacheThreshold()).orElse(DEFAULT_CACHE_THRESHOLD);
        // 默认缓存两段的配置长度
        return seqObj.getRequestNumber() * bufferCount * (INT_100 - threshold) > queueSize * INT_100;
    }

    private static boolean isEmpty(String cacheKey) {
        ArrayBlockingQueue<Object> queue = SEQUENCE_CACHE.get(cacheKey);
        return queue == null || queue.isEmpty();
    }

    /**
     * 获取远程服务端一组序列
     *
     * @param seqParamsDto seqParamsDto
     */
    public void getSeqFormServer(SeqParamsDto seqParamsDto) {
        String clientCacheKey = seqParamsDto.clientCacheKey();
        boolean isReleaseLock = false;
        try {
            isReleaseLock = lockByKey.tryLock(clientCacheKey);
            if (isReleaseLock) {
                LOGGER.info("开始远程获取序列号... , seqCode = {}", clientCacheKey);
                // 判断序列缓存是否存在 不存在初始化
                SeqObj seqObj = SEQUENCE_DESIGN_MAP.get(seqParamsDto.getSeqCode());
                if (!SEQUENCE_CACHE.containsKey(clientCacheKey)) {
                    SEQUENCE_CACHE.put(clientCacheKey, new ArrayBlockingQueue<>(seqObj.getRequestNumber() * sequenceQueueCapacity));
                }
                // 计算补仓数量
                ArrayBlockingQueue<Object> queue = SEQUENCE_CACHE.get(clientCacheKey);
                // 首次获取考虑分批次处理
                int createNum = seqObj.getRequestNumber() * bufferCount - queue.size();
                if (createNum <= 0) {
                    return;
                }
                seqParamsDto.setRequestNumber(createNum);
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                SeqTransferDto transferDto = seqConsumer.getSeqFormServer(seqParamsDto);
                stopWatch.stop();
                if (transferDto == null) {
                    LOGGER.warn("从服务端获取序列为空，序列编号：{}", clientCacheKey);
                    throw new SequenceException(ExceptionEnum.SEQ_FROM_SERVER_EMPTY);
                }
                List<Object> list = transferDto.getList();
                Object start = getSeq(list);
                LOGGER.info("从序列服务器获取{}序列耗时：{} , seqSize={} , start : {}", clientCacheKey, stopWatch.getTotalTimeMillis(), list.size(), start);
                queue.addAll(list);
                SEQUENCE_CACHE.put(clientCacheKey, queue);
            }
        } finally {
            if (isReleaseLock) {
                lockByKey.unlock(clientCacheKey);
            }
        }
    }

    private boolean isQueueEmpty(SequenceQueue sequenceQueue) {
        return null == sequenceQueue || CollectionUtils.isEmpty(sequenceQueue.getCacheBlockingQueue());
    }

    /**
     * 获取本地缓存中序列
     *
     * @param seqParamsDto seqParamsDto
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
                    LOGGER.info("全局序列{}不需要清理", seqDesignPo.getSequenceCode());
                    return seq;
            }
            if (StrUtil.equals(SequenceConstant.CALLBACK_MODE_DAY, seqDesignPo.getCallbackMode())) {
                seqCacheCleaningService.cleanHistorySeqCache(seqParamsDto);
            }else{
                String seqCodeBefore = SeqKeyUtils.getSeqClientCacheKey(seqParamsDto.getSeqCode(), dateBefore);
                LOGGER.info("清理缓存序列 seqCodeBefore={}", seqCodeBefore);
                SEQUENCE_QUEUE_MAP.remove(seqCodeBefore);
            }
        }
        return seq;
    }

    private synchronized void syncMakeUpWare(final SeqParamsDto seqParamsDto, final SequenceQueue sequenceQueue) {
        BlockingQueue<Object> queue = sequenceQueue.getCacheBlockingQueue();
        int queueSize = Optional.ofNullable(queue).map(Collection::size).orElse(0);
        if (sequenceQueue.getQuenList().size() < bufferCount && SequenceProperties.isNeedExpansionRatio(sequenceQueue.getPublishCount(), queueSize)) {
            LOGGER.info("开始补仓seqCode={} , queueSize={}", seqParamsDto.clientCacheKey(), queueSize);
            makeUpWarehuse(seqParamsDto, sequenceQueue);
        }
    }

    private void getNextDateSeq(SeqParamsDto seqParamsDto, SequenceQueue sequenceQueue) {
        // 获取明天日期
        String nextDate = DateUtils.getNextDateWithMill(seqParamsDto.getSeqDate());
        SeqParamsDto nextSeqParamsDto = new SeqParamsDto(seqParamsDto.getSeqCode(), nextDate);
//        如果当前序列开启日期占位符替换，则下日也同样开启
        if (seqParamsDto.isEnableDatePlaceholder()) {
            nextSeqParamsDto.setEnableDatePlaceholder(true);
        }
        nextSeqParamsDto.setSeqDate(DateUtils.getNextMillWithMill(seqParamsDto.getSeqDate()));

        // 异步获取明天的序列
        LOGGER.info("从远程服务器获取下一日序列 seqCodeNext={}", nextSeqParamsDto);
        makeUpWarehuseNextDay(nextSeqParamsDto, sequenceQueue);
    }

    /**
     * 首次从远程获取序列数据
     *
     * @param seqParamsDto seqParamsDto
     * @return Object
     */
    public synchronized Object getFirstSeqFormServer(SeqParamsDto seqParamsDto) {
        String clientCacheKey = seqParamsDto.clientCacheKey();
        LOGGER.info("开始远程获取序列号... , seqCode = {}", clientCacheKey);
        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(clientCacheKey);
        if (sequenceQueue != null && !isQueueEmpty(sequenceQueue)) {
            LOGGER.info("其他线程已获取到序列seqCode ={}，从本地缓存中获取...", clientCacheKey);
            return getSequenceFromLocalCache(seqParamsDto, sequenceQueue);
        }
        sequenceQueue = getSeqArrayFormServer(seqParamsDto, sequenceQueue);

        //5.检查缓存状态I
        if (this.checkQueueStatus(clientCacheKey)) {
            return sequenceQueue.getCacheSeq();
        }
        LOGGER.warn("序列key {} 未就绪", clientCacheKey);
        return null;
    }


    /**
     * 获取远程服务端一组序列
     *
     * @param seqParamsDto seqParamsDto
     * @param sequenceQueue sequenceQueue
     * @return SequenceQueue SequenceQueue
     * @throws SequenceException SequenceException
     */
    public SequenceQueue getSeqArrayFormServer(SeqParamsDto seqParamsDto, SequenceQueue sequenceQueue) {

        String clientCacheKey = seqParamsDto.clientCacheKey();
        // 1.远程获取序列数据
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqTransferDto transferDto = seqConsumer.getSeqFormServer(seqParamsDto);
        stopWatch.stop();
        if (transferDto == null) {
            LOGGER.warn("从服务端获取序列为空，序列编号：{}", clientCacheKey);
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR);
        }
        List<Object> list = transferDto.getList();
        Object start = getSeq(list);

        LOGGER.info("从序列服务器获取{}序列耗时：{} , seqSize={} , start : {}", clientCacheKey, stopWatch.getTotalTimeMillis(), list.size(), start);

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
    public Object getIncreaseSeqFormServer(SeqParamsDto seqParamsDto) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqTransferDto transferDto = seqConsumer.getIncreaseSeqFormServer(seqParamsDto);
        stopWatch.stop();
        if (transferDto == null) {
            LOGGER.warn("从服务端获取严格递增序列为空，序列编号：{}", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR);
        }
        List<Object> list = transferDto.getList();
        Object seq = getSeq(list);
        LOGGER.info("从序列服务器获取严格递增{}序列耗时：{} , seqSize={} , seq : {}", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis(), list.size(), seq);
        return seq;
    }

    /**
     * 远程服务获取回收序号
     *
     * @param seqParamsDto seqParamsDto
     * @return seq seq
     */
    public Object getRecoverySeqFormServer(SeqParamsDto seqParamsDto) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqTransferDto transferDto = seqConsumer.getRecoverySeqFormServer(seqParamsDto);
        stopWatch.stop();
        if (transferDto == null) {
            LOGGER.warn("从服务端获取回收序列为空，序列编号：{}", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR);
        }
        List<Object> list = transferDto.getList();
        Object seq = getSeq(list);
        LOGGER.info("从序列服务器获取回收{}序列耗时：{} , seqSize={} , seq : {}", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis(), list.size(), seq);
        return seq;
    }

    /**
     * 远程服务回收序号
     *
     * @param seqParamsDto seqParamsDto
     * @return Boolean Boolean
     */
    public Boolean recoverySequence(SeqParamsDto seqParamsDto) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SeqTransferDto transferDto = seqConsumer.recoverySequence(seqParamsDto);
        stopWatch.stop();
        if (transferDto == null) {
            LOGGER.warn("从服务端回收序列为空，序列编号：{}", seqParamsDto.getSeqCode());
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR);
        }
        LOGGER.info("从序列服务器回收{}序列耗时：{}", seqParamsDto.getSeqCode(), stopWatch.getTotalTimeMillis());
        return true;
    }

    private static Object getSeq(List<Object> list) {
        return Optional.ofNullable(list)
                .filter(objects -> !CollectionUtils.isEmpty(objects))
                .map(objects -> objects.get(0))
                .orElseThrow(() -> new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR));
    }

    /**
     * 序列补仓
     *
     * @param seqParamsDto seqParamsDto
     * @param sequenceQueue sequenceQueue
     */
    public void makeUpWarehuse(final SeqParamsDto seqParamsDto, final SequenceQueue sequenceQueue) {
        executor.execute(() -> {
            try {
                getSeqArrayFormServer(seqParamsDto, sequenceQueue);
            } catch (Exception e) {
                LOGGER.error("补仓异常", e);
            }
        });
    }


    /**
     * 序列补仓 -补第二天的 如果补仓失败重置补仓状态
     *
     * @param seqParamsDto seqParamsDto
     * @param sequenceQueue sequenceQueue
     */
    public void makeUpWarehuseNextDay(final SeqParamsDto seqParamsDto, final SequenceQueue sequenceQueue) {
        LOGGER.info("开始补仓第二天seqCode={}", seqParamsDto);
        executor.execute(() -> {
            try {
                getSeqArrayFormServer(seqParamsDto, null);
            } catch (Exception e) {
                LOGGER.error("补仓第二天异常.msg = {}", e.getMessage(), e);
                //重置补仓状态
                sequenceQueue.setHasNextDaySeq(false);
            }
        });
    }

    private Boolean checkQueueStatus(String clientCacheKey) {
        SequenceQueue sequenceQueue = SEQUENCE_QUEUE_MAP.get(clientCacheKey);
        return null != sequenceQueue;
    }

    public void setUseSequenceCache(boolean useSequenceCache) {
        this.useSequenceCache = useSequenceCache;
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

    public SeqCacheCleaningService getSeqCacheCleaningService() {
        return seqCacheCleaningService;
    }

    public void setSeqCacheCleaningService(SeqCacheCleaningService seqCacheCleaningService) {
        this.seqCacheCleaningService = seqCacheCleaningService;
    }

    public static ConcurrentHashMap<String, SequenceQueue> getSequenceQueueMap() {
        return SEQUENCE_QUEUE_MAP;
    }

}
