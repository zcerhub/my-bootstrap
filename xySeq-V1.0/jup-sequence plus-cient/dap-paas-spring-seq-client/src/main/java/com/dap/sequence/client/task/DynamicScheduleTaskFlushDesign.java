package com.dap.sequence.client.task;


import com.dap.sequence.api.core.SeqConsumer;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.properties.ConfigBeanValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;


/**
 * @className DynamicScheduleTaskFlushDesign
 * @description 定时获取集群下所有的实例信息
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
@Configuration
@EnableScheduling
public class DynamicScheduleTaskFlushDesign implements SchedulingConfigurer {

    @Autowired
    public ConfigBeanValue cb;

    @Autowired
    private SeqConsumer seqConsumer;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if ("off".equals(StringUtils.trimAllWhitespace(cb.getRefreshTime()))) {
            return;
        }
        taskRegistrar.addTriggerTask(
                // 1.添加任务内容(Runnable)
                () -> SequenceQueueFactory.SEQUENCE_DESIGN_MAP.forEach((k, v) -> {
                    v = seqConsumer.getSeqDesignObj(k);
                }),
                // 2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cb.getRefreshTime()).nextExecutionTime(triggerContext);
                }
        );
    }
}
