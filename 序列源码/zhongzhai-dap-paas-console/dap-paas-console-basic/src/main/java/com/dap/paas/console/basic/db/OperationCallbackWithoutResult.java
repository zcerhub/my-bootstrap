package com.dap.paas.console.basic.db;

/**
 * 业务操作接口，无需返回值
 *
 */
public interface OperationCallbackWithoutResult<A> {

    /**
     * 业务执行无需返回值
     *
     * @param a 请求入参
     */
    void executeWithoutResult(A a);
}
