package com.dap.sequence.client.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @className RuleParams
 * @description TODO
 * @author renle
 * @date 2023/12/29 11:16:57 
 * @version: V23.06
 */
public class RuleParams {

    private Map<String, String> time;
    /**
     * 占位符参数
     */
    private List<String> params;

    private Map<String, LinkedList<Long>> enumMap;

    private Long padValue;

    private Long optionalValue;

    private Map<Long, Long> padMaps;

    /**
     * 自选参数
     */
    private String seqVal;

    /**
     * 数字序号段
     */
    private Map<String, LinkedList<Long>> numMap;

    private boolean isOptional;
    private Map<String, String> specialChar;

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public Map<String, String> getSpecialChar() {
        return specialChar;
    }

    public void setSpecialChar(Map<String, String> specialChar) {
        this.specialChar = specialChar;
    }

    public RuleParams() {
    }

    public RuleParams(Map<String, String> time) {
        this.time = time;
    }

    public Map<String, String> getTime() {
        return time;
    }

    public void setTime(Map<String, String> time) {
        this.time = time;
    }

    public Map<Long, Long> getPadMaps() {
        return padMaps;
    }

    public void setPadMaps(Map<Long, Long> padMaps) {
        this.padMaps = padMaps;
    }

    public Long getPadValue() {
        return padValue;
    }

    public void setPadValue(Long padValue) {
        this.padValue = padValue;
    }

    public Long getOptionalValue() {
        return optionalValue;
    }

    public void setOptionalValue(Long optionalValue) {
        this.optionalValue = optionalValue;
    }

    public String getSeqVal() {
        return seqVal;
    }

    public void setSeqVal(String seqVal) {
        this.seqVal = seqVal;
    }

    public void setOptional(Long padValue, String seqVal) {
        this.seqVal = seqVal;
        this.padValue = padValue;
    }

    public Map<String, LinkedList<Long>> getNumMap() {
        return numMap;
    }

    public void setNumMap(Map<String, LinkedList<Long>> numMap) {
        this.numMap = numMap;
    }

    public boolean getOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public Map<String, LinkedList<Long>> getEnumMap() {
        return enumMap;
    }

    public void setEnumMap(Map<String, LinkedList<Long>> enumMap) {
        this.enumMap = enumMap;
    }
}
