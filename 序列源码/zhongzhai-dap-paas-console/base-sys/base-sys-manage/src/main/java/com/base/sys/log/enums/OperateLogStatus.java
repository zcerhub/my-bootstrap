package com.base.sys.log.enums;

/**
 * @author qqqab
 */

public enum OperateLogStatus {
    SEND_SUC(1, "日志同步成功"),
    SEND_FAIL(0, "日志同步失败");
    private final Integer code;
    private final String description;

    OperateLogStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OperateLogStatus parse(Integer code) {
        for (OperateLogStatus operateType : OperateLogStatus.values()) {
            if (operateType.code.equals(code)) {
                return operateType;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
