package com.dap.paas.console.basic.job;

import org.quartz.Job;

/**
 * base job
 *
 * @author Arlo
 * @date 2021/10/12
 **/
public interface BaseJob extends Job {

    /**
     * job名称
     *
     * @return String
     */
    default String getJobName() {
        return null;
    }

    /**
     * job组名
     *
     * @return String
     */
    default String getJobGroup() {
        return null;
    }

    /**
     * job名称
     *
     * @return String
     */
    default String getTriggerName() {
        return null;
    }

    /**
     * job组名
     *
     * @return String
     */
    default String getTriggerGroup() {
        return null;
    }

}
