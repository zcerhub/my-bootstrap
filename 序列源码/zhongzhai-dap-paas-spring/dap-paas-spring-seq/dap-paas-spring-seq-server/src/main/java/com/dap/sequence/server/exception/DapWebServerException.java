package com.dap.sequence.server.exception;

import com.dap.sequence.api.commons.ResultEnum;

/**
 * 自定义异常类，继承RuntimeException
 *
 * @author zpj
 * @date 2021/10/28
 */
public class DapWebServerException extends RuntimeException {
    private static final long serialVersionUID = 1246866093608034923L;

    /**
     * 异常码
     */
    private Integer code;

    public DapWebServerException(ResultEnum rs) {
        super(rs.getMsg());
        this.code = rs.getCode();
    }

    public DapWebServerException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public DapWebServerException(String msg) {
        super(msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
