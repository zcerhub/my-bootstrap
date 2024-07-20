package com.dap.sequence.api.core;


/**
 * @className SeqFlowEngine
 * @description 序列引擎接口
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public interface SeqFlowEngine {

    /**
     * 执行方法，引擎加载启动时需要执行的方法，完成数据装载，生产，以及服务发布。
     */
    void run() ;


    /**
     * 刷新方法，完成配置数据的动态刷新，用于动态更新配置对象
     */
    void refresh();
}
