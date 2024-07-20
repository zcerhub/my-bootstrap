package com.dap.sequence.client.task;

import com.dap.sequence.api.constant.SequenceConstant;
import com.dap.sequence.api.util.DateUtils;
import com.dap.sequence.client.core.SequenceQueue;
import com.dap.sequence.client.core.SequenceQueueFactory;
import com.dap.sequence.client.properties.ConfigBeanValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
@EnableScheduling
public class SequenceQueueFactoryCacheClearTask implements SchedulingConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(DynamicScheduleTaskUrl.class);

    @Autowired
    public ConfigBeanValue cb;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                this::executeTask,
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cb.getClearCorn()).nextExecutionTime(triggerContext);
                }
        );
    }

    private void executeTask() {
        logger.info("***** 客户端开始清理日切过期缓存 *****");
        String dateBefore = DateUtils.getPrveDate(new Date(), cb.getClearDay());
        ConcurrentHashMap<String, SequenceQueue>  sequence_queue_map=SequenceQueueFactory.getSequenceQueueMap();
        List<String> keys = new ArrayList<>(sequence_queue_map.keySet());
        keys.forEach(key -> {
            String date = StringUtils.substringAfter(key, SequenceConstant.DAY_SWITCH_SPLIT);
            if (date.length() == SequenceConstant.INT_8 && Integer.parseInt(date) < Integer.parseInt(dateBefore)) {
                sequence_queue_map.remove(key);
                logger.info("清理超过{}天的缓存序列 {}", cb.getClearDay(),key);
            }
        });
        logger.info("***** 结束清理日切过期缓存 *****");
    }
}
