package com.dap.sequence.api.obj;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @className SegmentBuffer
 * @description TODO
 * @author renle
 * @date 2024/01/23 10:06:03 
 * @version: V23.06
 */

public class SegmentBuffer {

    /**
     * key,唯一值
     */
    private String key;

    /**
     * 双buffer段数组，这里只有2段
     */
    private Segment[] segments;

    /**
     * 当前使用的segment的index
     */
    private volatile int currentPos;

    /**
     * 下一个segment是否处于可切换状态
     */
    private volatile boolean nextReady;

    /**
     * 线程是否在运行中
     */
    private final AtomicBoolean threadRunning;

    /**
     * 读写锁
     */
    private final ReadWriteLock lock;

    /**
     * 步长
     */
    private volatile int step;

    /**
     * 最大数量不超过50000
     */
    public static final int MAX_STEP = 50000;

    /**
     * 更新Segment时时间戳
     */
    private volatile long updateTimestamp;

    public SegmentBuffer() {
        segments = new Segment[]{new Segment(this), new Segment(this)};
        currentPos = 0;
        nextReady = false;
        threadRunning = new AtomicBoolean(false);
        lock = new ReentrantReadWriteLock();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取当前的segment
     */
    public Segment getCurrent() {
        return segments[currentPos];
    }

    /**
     * 获取下一个segment
     */
    public int nextPos() {
        return (currentPos + 1) % 2;
    }

    /**
     * 切换segment
     */
    public void switchPos() {
        currentPos = nextPos();
    }

    /**
     * segmentBuffer加读锁
     */
    public Lock rLock() {
        return lock.readLock();
    }

    /**
     * segmentBuffer加写锁
     */
    public Lock wLock() {
        return lock.writeLock();
    }

    public Segment[] getSegments() {
        return segments;
    }

    public void setSegments(Segment[] segments) {
        this.segments = segments;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public AtomicBoolean getThreadRunning() {
        return threadRunning;
    }

    public boolean isNextReady() {
        return nextReady;
    }

    public void setNextReady(boolean nextReady) {
        this.nextReady = nextReady;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SegmentBuffer{");
        sb.append("key='").append(key).append('\'');
        sb.append(", segments=").append(Arrays.toString(segments));
        sb.append(", currentPos=").append(currentPos);
        sb.append(", nextReady=").append(nextReady);
        sb.append(", threadRunning=").append(threadRunning);
        sb.append(", step=").append(step);
        sb.append(", updateTimestamp=").append(updateTimestamp);
        sb.append('}');
        return sb.toString();
    }
}
