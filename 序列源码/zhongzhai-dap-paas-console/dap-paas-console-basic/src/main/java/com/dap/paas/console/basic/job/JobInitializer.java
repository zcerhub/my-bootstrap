/*
 * @Descripttion: 
 * @version: 
 * @Author: tangpy
 * @Date: 2023-10-07 10:20:25
 * @LastEditors: tangpy
 * @LastEditTime: 2023-10-07 10:23:50
 */
package com.dap.paas.console.basic.job;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ServiceLoader;

/**
 * job初始化器
 *
 * @author Arlo
 * @date 2021/10/12
 **/
@Component
public class JobInitializer implements ApplicationRunner, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobInitializer.class);

    private Environment environment;

    /**
     * 类级Key前缀
     */
    private String schedulerCronKeyPre = "scheduler.";
    /**
     * 全局key
     */
    private String schedulerCronKey = "scheduler.cron";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        ServiceLoader<BaseJob> jobs = ServiceLoader.load(BaseJob.class);
        for (BaseJob baseJob : jobs) {
            String cronClassKey = new StringBuilder(schedulerCronKeyPre).append(baseJob.getClass().getSimpleName().toString()).append(".").append("cron").toString();
            if (StringUtils.isEmpty(environment.getProperty(cronClassKey)) && StringUtils.isEmpty(environment.getProperty(schedulerCronKey))) {
                continue;
            } else {
                Class<? extends BaseJob> jobClass = baseJob.getClass();
                String className = jobClass.getName();
                JobKey jobKey = JobKey.jobKey(getValue(className, baseJob.getJobName()),
                        getValue(className, baseJob.getJobGroup()));
                TriggerKey triggerKey = TriggerKey.triggerKey(getValue(className, baseJob.getTriggerName()),
                        getValue(className, baseJob.getTriggerGroup()));
                if (checkExist(scheduler, jobKey, triggerKey)) {
                    continue;
                }
                JobDetail jobDetail = JobBuilder.newJob(jobClass)
                        .withIdentity(jobKey)
                        .build();
                String cronVal = StringUtils.isNotEmpty(environment.getProperty(cronClassKey)) ? environment.getProperty(cronClassKey) : environment.getProperty(schedulerCronKey);
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .startNow()
                        .withSchedule(CronScheduleBuilder.cronSchedule(cronVal))
                        .build();
                LOGGER.info("schedule job [{}] start success! cron: [{}]", className, cronVal);
                scheduler.scheduleJob(jobDetail, trigger);
            }

        }
    }

    private static String getValue(String className, String sourceValue) {
        return (sourceValue == null) ? className : sourceValue;
    }

    private static boolean checkExist(Scheduler scheduler, JobKey jobKey, TriggerKey triggerKey) throws SchedulerException {
        if (scheduler.checkExists(jobKey)) {
            String jk = JSON.toJSONString(jobKey);
            LOGGER.warn("jobKey is exist: {}", jk);
            return true;
        }
        if (scheduler.checkExists(triggerKey)) {
            String tk = JSON.toJSONString(triggerKey);
            LOGGER.warn("triggerKey is exist: {}", tk);
            return true;
        }
        return false;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
