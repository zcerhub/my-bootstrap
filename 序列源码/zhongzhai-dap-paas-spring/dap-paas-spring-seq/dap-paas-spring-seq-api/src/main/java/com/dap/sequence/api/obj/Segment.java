package com.dap.sequence.api.obj;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @className Segment
 * @description TODO
 * @author renle
 * @date 2024/01/23 10:05:28 
 * @version: V23.06
 */
public class Segment extends LinkedBlockingQueue<Object> {

    /**
     * 步长
     */
    public volatile int step;

    /**
     * 最大值
     */
    private volatile long max;


    /**
     * buffer
     */
    private SegmentBuffer buffer;

    public Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public SegmentBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        return "Segment(" + ",max:" +
                max +
                ",step:" +
                step +
                ")";
    }
}
