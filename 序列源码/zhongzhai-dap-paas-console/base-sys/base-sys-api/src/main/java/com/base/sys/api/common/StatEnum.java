package com.base.sys.api.common;

/**
 * 成功失败枚举类
 * @author XIAYUANA
 */

public enum StatEnum {
    SUCCESS(1,"成功"),
    FAILD(0,"失败"),
    REPEAT(-1,"重复"),
    INNER_ERROR(-2,"系统异常"),
    DATA_ERROR(-3,"数据异常");

    private  int state;

    private  String stateInfo;

    StatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static StatEnum stateOf(int index){
        for(StatEnum state: values()){
            if(state.getState() == index){
                return state;
            }
        }
        return  null;
    }
}
