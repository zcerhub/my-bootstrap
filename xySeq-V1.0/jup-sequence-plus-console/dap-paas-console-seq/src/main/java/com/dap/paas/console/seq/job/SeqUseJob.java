package com.dap.paas.console.seq.job;

import com.dap.paas.console.basic.job.BaseJob;
import com.dap.paas.console.basic.utils.SpringContext;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.seq.dao.SeqUseConditionDao;
import com.dap.paas.console.seq.feign.SeqUseConditionFeignApi;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @className SeqUseJob
 * @description 序列使用记录清理任务
 * @date 2024/2/1 17:14:29
 * @version: V24.03
 */
public class SeqUseJob implements BaseJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeqUseJob.class);

    /**
     * 清理三个月之前的数据
     */
    private static final int INT_90 = -90;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LOGGER.warn("***** 序列使用记录数据定时清理开始 *****");
        SeqUseConditionFeignApi seqUseConditionFeignApi = SpringContext.getBeanNonnull(SeqUseConditionFeignApi.class);
        // 保留三个月(90天数据)数据
        Map<String, Object> map = new HashMap<>();
        Date date = getDate(INT_90);
        LOGGER.warn("需要清理的时间截止日期为：{}", date);
        map.put("time", date);
        ResultResponse<Integer> resultResponse = seqUseConditionFeignApi.deleteAttributeData(map);
        LOGGER.warn("清理使用记录条数：{}", resultResponse.getData());
        LOGGER.warn("***** 序列使用记录数据定时清理结束 *****");
    }

    private static Date getDate(int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    @Override
    public String getJobName() {
        return "seq-use";
    }

    @Override
    public String getJobGroup() {
        return "seq-use";
    }

    @Override
    public String getTriggerName() {
        return "seq-use";
    }

    @Override
    public String getTriggerGroup() {
        return "seq-use";
    }
}
