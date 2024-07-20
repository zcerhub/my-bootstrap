package com.base.api.exception;

public class DaoException extends RuntimeException{


    private String code ;  //异常对应的返回码
    private String msg;  //异常对应的描述信息

    public DaoException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
    public DaoException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

}
