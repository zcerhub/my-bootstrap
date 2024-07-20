package com.dap.sequence.client.snow;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 *
 * <pre>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00 - 0000000000 -  0000000000
 * </pre>
 * <p>
 * 第一位为未使用(符号位表示正数)，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是4位datacenterId(时钟ID，)和8位workerId(8位的长度最多支持部署256个节点）<br>
 * 最后10位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
 *
 * @author zpj
 * @since 3.0.1
 */
public class Snowflake implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Snowflake.class);

    private final long twepoch;

    /**
     * 时钟ID，时钟发生回拨时，会增加1L
     */
    private static final long clockIdBits = 2L;

    /**
     * workerId所占的位数修改为10位，通过数据库获取
     */
    private final long workerIdBits = 10L;

    /**
     * 修改原有的雪花算法，将虚机或者容器部署的数量提升到1024个，0~1023
     * （最大支持机器节点数0~31，一共32个，废弃）
     */
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 时钟最大回拨4次
     */
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    public static final long MAX_CLOCK_ID = -1L ^ (-1L << clockIdBits);

    /**
     * 序列号修改为10位长度
     * （序列号12位已废弃）
     */
    private final long sequenceBits = 10L;

    /**
     * 机器节点左移10位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据中心节点左移18位
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间毫秒数左移22位
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + clockIdBits;

    /**
     * 其他保持不变时，最大为1023
     * sequence位数由12位转换为10位
     */
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 一个虚机一个workId或者一个容器一个workId
     */
    private final long workerId;

    /**
     * 时钟Id,初始值为0，回拨后会加1
     */
    private long clockId;

    /**
     * 是否使用 {SystemClock} 获取当前时间戳
     */
    private final boolean useSystemClock;

    /**
     * 10位长度的seq
     */
    private long sequence = 0L;

    /**
     * 最近一次获取的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造
     *
     * @param workerId     终端ID
     * @param clockId 时钟ID
     */
    public Snowflake(long workerId, long clockId) {
        this(workerId, clockId, false);
    }

    /**
     * 构造
     *
     * @param workerId         终端ID
     * @param clockId     时钟ID
     * @param isUseSystemClock 是否使用{ SystemClock} 获取当前时间戳
     */
    public Snowflake(long workerId, long clockId, boolean isUseSystemClock) {
        this(null, workerId, clockId, isUseSystemClock);
    }

    /**
     * 雪花算法
     *
     * @param epochDate        初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId         工作机器节点id
     * @param clockId     时钟id
     * @param isUseSystemClock 是否使用 获取当前时间戳
     * @since 5.1.3
     */
    public Snowflake(Date epochDate, long workerId, long clockId, boolean isUseSystemClock) {
        if (null != epochDate) {
            this.twepoch = epochDate.getTime();
        } else {
            // Thu, 04 Nov 2010 01:42:54 GMT
            this.twepoch = 1288834974657L;
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than {} or less than 0", maxWorkerId));
        }
        if (clockId > MAX_CLOCK_ID || clockId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than {} or less than 0", MAX_CLOCK_ID));
        }
        this.workerId = workerId;
        this.clockId = clockId;
        this.useSystemClock = isUseSystemClock;
    }

    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(long id) {
        return id >> workerIdShift & ~(-1L << workerIdBits);
    }

    /**
     * 根据Snowflake的ID，获取时钟ID
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(long id) {
        return id >> dataCenterIdShift & ~(-1L << clockIdBits);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(long id) {
        return (id >> timestampLeftShift & ~(-1L << 41L)) + twepoch;
    }

    /**
     * 下一个ID
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = genTime();
        if (timestamp < lastTimestamp) {
            if (lastTimestamp - timestamp < 2000) {
                // 容忍2秒内的回拨，避免NTP校时造成的异常
                timestamp = lastTimestamp;
            } else {
                clockId = (clockId + 1) & MAX_CLOCK_ID;
                LOGGER.warn("雪花算法时钟发生回拨,时钟标识+1，当前时间：{}，上次使用后最大时间：{}", timestamp, lastTimestamp);

                // 写入磁盘文件
                SnowflakeFile.writerClock(clockId);
            }
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (clockId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 下一个ID（字符串形式）
     *
     * @return ID 字符串形式
     */
    public String nextIdStr() {
        return Long.toString(nextId());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------ Private method start

    /**
     * 循环等待下一个时间
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = genTime();
        // 循环直到操作系统时间戳变化
        while (timestamp == lastTimestamp) {
            timestamp = genTime();
        }
        if (timestamp < lastTimestamp) {
            // 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
            throw new IllegalStateException(
                    String.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
        }
        return timestamp;
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    private long genTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前的雪花算法workId
     *
     * @return
     */
    public long getCurrentWorkerId() {
        return workerId;
    }
}
