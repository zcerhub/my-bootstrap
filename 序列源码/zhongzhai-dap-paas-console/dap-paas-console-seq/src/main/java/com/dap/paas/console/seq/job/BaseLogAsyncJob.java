package com.dap.paas.console.seq.job;


import com.base.sys.async.service.BaseLogAuditAsyncService;
import com.base.sys.auth.log.OperateTypeAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author qqqab
 */
@Configuration
@EnableScheduling
@EnableAsync
public class BaseLogAsyncJob {
    @Autowired
    BaseLogAuditAsyncService operateLogAsyncService;

    @Async
    @Scheduled(initialDelayString = "${gientech.initialDelay}", fixedRateString = "${gientech.fixedRateString}")
    public void execute(){
        operateLogAsyncService.asynFailedLogAudits(OperateTypeAspect.SEQ);
    }
}
