package com.base.sys.auth.token.util;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 链式map
 *
 * @author Chill
 */
public class Kv extends LinkedCaseInsensitiveMap<Object> {

    private Kv() {
        super();
    }

    /**
     * 创建Kv
     *
     * @return Kv
     */
    public static Kv create() {
        return new Kv();
    }

    public static <K, V> HashMap<K, V> newMap() {
        return new HashMap<>(16);
    }

    /**
     * 设置列
     *
     * @param attr  属性
     * @param value 值
     * @return 本身
     */
    public Kv set(String attr, Object value) {
        this.put(attr, value);
        return this;
    }

    /**
     * 设置全部
     *
     * @param map 属性
     * @return 本身
     */
    public Kv setAll(Map<? extends String, ?> map) {
        if (map != null) {
            this.putAll(map);
        }
        return this;
    }

    /**
     * 设置列，当键或值为null时忽略
     *
     * @param attr  属性
     * @param value 值
     * @return 本身
     */
    public Kv setIgnoreNull(String attr, Object value) {
        if (attr != null && value != null) {
            set(attr, value);
        }
        return this;
    }

    public Object getObj(String key) {
        return super.get(key);
    }

    /**
     * 获得特定类型值
     *
     * @param <T>          值类型
     * @param attr         字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String attr, T defaultValue) {
        final Object result = get(attr);
        return (T) (result != null ? result : defaultValue);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public String getStr(String attr) {
        return toStr(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Integer getInt(String attr) {
        return toInt(get(attr), -1);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Long getLong(String attr) {
        return toLong(get(attr), -1L);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Float getFloat(String attr) {
        return toFloat(get(attr), 0);
    }

    public Double getDouble(String attr) {
        return toDouble(get(attr), 0);
    }


    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Boolean getBool(String attr) {
        return toBoolean(get(attr), null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public byte[] getBytes(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Date getDate(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Time getTime(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Timestamp getTimestamp(String attr) {
        return get(attr, null);
    }

    /**
     * 获得特定类型值
     *
     * @param attr 字段名
     * @return 字段值
     */
    public Number getNumber(String attr) {
        return get(attr, null);
    }

    @Override
    public Kv clone() {
        Kv clone = new Kv();
        clone.putAll(this);
        return clone;
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return {String}
     */
    public static String toStr(Object str, String defaultValue) {
        if (null == str || str.equals("null")) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    public static int toInt(@Nullable final Object str, final int defaultValue) {
        return NumberUtils.toInt(String.valueOf(str), defaultValue);
    }

    public static long toLong(@Nullable final Object str, final long defaultValue) {
        return NumberUtils.toLong(String.valueOf(str), defaultValue);
    }

    public static float toFloat(@Nullable final Object str, final float defaultValue) {
        return NumberUtils.toFloat(String.valueOf(str), defaultValue);
    }

    public static double toDouble(@Nullable final Object str, final double defaultValue) {
        return NumberUtils.toDouble(String.valueOf(str), defaultValue);
    }

    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value != null) {
            String val = String.valueOf(value);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

}
