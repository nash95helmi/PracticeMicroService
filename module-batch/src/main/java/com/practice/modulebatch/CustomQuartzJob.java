package com.practice.modulebatch;

import lombok.Getter;
import lombok.Setter;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Getter
@Setter
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CustomQuartzJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }
}
