package com.dap.paas.console.basic.db;

/**
 * 资源管理接口
 *
 */
public interface ResourceManage<A, R> {

    /**
     * 根据参数获取资源
     *
     * @return
     */
    default R acquire() {
        return null;
    }

    /**
     * 根据参数获取资源
     *
     * @param a
     * @return
     */
    default R acquire(A a) {
        return null;
    }

    /**
     * 资源释放
     *
     * @param a 请求参数类型
     * @param r 相关资源
     */
    void release(A a, R r);
}
