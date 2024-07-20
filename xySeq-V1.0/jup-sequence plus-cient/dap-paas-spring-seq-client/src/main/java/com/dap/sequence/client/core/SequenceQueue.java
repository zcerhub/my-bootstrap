package com.dap.sequence.client.core;

import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.SequenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @className SequenceQueue
 * @description 序列队列类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class SequenceQueue {

    private static final Logger logger = LoggerFactory.getLogger(SequenceQueue.class);
    /**
     * 异步补仓线程是否持有锁
     */
    private AtomicBoolean isHold = new AtomicBoolean(false);

    /**
     * 序列临时缓存
     */
    private volatile BlockingQueue<Object> cacheBlockingQueue = null;

    /**
     * 序列缓存集合
     */
    private volatile BlockingQueue<BlockingQueue<Object>> quenList = null;

    private Date lastGetTime;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 序列编码
     */
    private String seqCode;
    /**
     * 时间
     */
    private String day;
    /**
     * 号段数量
     */
    private int publishCount = 1000;
    /**
     * 从队列中获取序列的超时时间 默认2秒
     */
    private long timeout = 2000;

    /**
     * 下一日的序列是否存在
     */
    private final AtomicBoolean hasNextDaySeq = new AtomicBoolean(false);

    /**
     * 设置锁 保证一个序列只会执行一次
     * 第一个调用返回false 后面都返回true
     *
     * @return Boolean
     */
    public boolean setNextDaySeqIfAbsent() {
        return hasNextDaySeq.getAndSet(true);
    }

    public SequenceQueue() {

    }

    public SequenceQueue(List<Object> list, int queueLength, long timeout, String seqCode, String day) {
        this.seqCode = seqCode;
        this.day = day;
        this.timeout = timeout;
        this.quenList = new LinkedBlockingQueue<>(queueLength);
        this.quenList.add(new ArrayBlockingQueue<>(list.size(), true, list));
        this.createTime = new Date();
    }

    /**
     * 从quenList 中获取序列号段
     * @return
     */
    public synchronized BlockingQueue<Object> getCacheBlockingQueue() {
        try {
            this.lastGetTime = new Date();
            if (this.cacheBlockingQueue == null || this.cacheBlockingQueue.isEmpty()) {
                cacheBlockingQueue = this.quenList.poll(timeout, TimeUnit.MILLISECONDS);
            }
            return cacheBlockingQueue;
        } catch (InterruptedException e) {
            logger.error("从cacheBlockingQueue中poll序列异常", e);
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR);
        }
    }

    /**
     * 获取序列
     *
     * @return Object
     */
    public synchronized Object getCacheSeq() {
        try {
            this.lastGetTime = new Date();
            if (this.cacheBlockingQueue == null || this.cacheBlockingQueue.isEmpty()) {
                cacheBlockingQueue = this.quenList.poll(timeout, TimeUnit.MILLISECONDS);
            }
            return cacheBlockingQueue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("从cacheBlockingQueue中poll序列异常", e);
            throw new SequenceException(ExceptionEnum.GET_SEQ_QUEUE_ERROR);
        }
    }


    public void setCacheBlockingQueue(BlockingQueue<Object> cacheBlockingQueue) {
        this.cacheBlockingQueue = cacheBlockingQueue;
    }

    public BlockingQueue<BlockingQueue<Object>> getQuenList() {
        return this.quenList;
    }

    public void setQuenList(BlockingQueue<BlockingQueue<Object>> quenList) {
        this.quenList = quenList;
    }

    public int getPublishCount() {
        return publishCount;
    }

    public void setPublishCount(int publishCount) {
        this.publishCount = publishCount;
    }

    public BlockingQueue<Object> getBufferQueue() {
        return this.cacheBlockingQueue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSeqCode() {
        return seqCode;
    }

    public void setSeqCode(String seqCode) {
        this.seqCode = seqCode;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public AtomicBoolean getIsHold() {
        return isHold;
    }

    public void setIsHold(AtomicBoolean isHold) {
        this.isHold = isHold;
    }

    public AtomicBoolean getHasNextDaySeq() {
        return hasNextDaySeq;
    }

    public Date getLastGetTime() {
        return lastGetTime;
    }

    public void setLastGetTime(Date lastGetTime) {
        this.lastGetTime = lastGetTime;
    }
}
