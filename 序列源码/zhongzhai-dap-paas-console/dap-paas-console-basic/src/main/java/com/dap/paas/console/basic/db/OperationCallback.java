package com.dap.paas.console.basic.db;

/**
 * 业务操作接口，需返回值
 *
 */
public interface OperationCallback<A, R> {

    /**
     * 业务执行逻辑
     *
     * @param a 请求入参对想想
     * @return 返回值
     */
    R execute(A a);
}
