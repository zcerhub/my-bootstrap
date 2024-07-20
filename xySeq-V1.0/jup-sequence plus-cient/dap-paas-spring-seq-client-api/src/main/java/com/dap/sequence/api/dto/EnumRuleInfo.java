package com.dap.sequence.api.dto;


import com.dap.sequence.api.dto.base.BaseRuleInfo;

import java.util.Arrays;
import java.util.List;

/**
 * @className EnumRuleInfo
 * @description 枚举规则类
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class EnumRuleInfo extends BaseRuleInfo {

    private volatile int index;

    /**
     * 步长
     */
    private volatile int numberStep;

    private volatile String initData;

    private List<String> enumStore;

    private boolean isInit;


    public EnumRuleInfo() {

    }

    public EnumRuleInfo(Integer numberStep, String initData, String enumStore) {
        this.index = Arrays.asList(enumStore.split(",")).indexOf(initData);
        this.numberStep = numberStep;
        this.initData = initData;
        this.enumStore = Arrays.asList(enumStore.split(","));
    }

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNumberStep() {
        return numberStep;
    }

    public void setNumberStep(int numberStep) {
        this.numberStep = numberStep;
    }

    public List<String> getEnumStore() {
        return enumStore;
    }

    public void setEnumStore(List<String> enumStore) {
        this.enumStore = enumStore;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }
}
