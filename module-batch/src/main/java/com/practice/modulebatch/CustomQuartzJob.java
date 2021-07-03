package com.practice.modulebatch;

import com.practice.modulebatch.exception.BatchErrorType;
import com.practice.modulebatch.exception.BatchProcessException;
import com.practice.modulebatch.service.BatchManagerService;
import com.practice.modulebatch.vo.BatchJobVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class CustomQuartzJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomQuartzJob.class);
    private static final String WORKING_DAY = "input.working.day";
    private static final String DEFAULT_BATCH_USER_ID = "5ebaa7818723494d8962e9f69777d43c";

    private String jobName;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;
    protected Exception exception = null;
    //Counter to keep track how many time job success/fail
    protected volatile int numSuccess;
    protected volatile int numFail;
    private String triggerBy;
    private String workingDir;
    private String starterType;
    private String workingDay;
    private Date processDate;
    //Input parameter
    protected String userId = DEFAULT_BATCH_USER_ID;
    //Value of file Name that batch used(created, deleted or read)
    protected String fileName;
    private BatchManagerService batchService;
    private JobExplorer jobExplorer;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if(StringUtils.isEmpty(jobName))
            throw new BatchProcessException(BatchErrorType.SYSTEM_GENERAL_ERROR,
                    "Batch could not started as '"+jobName+"' was not found");

        BatchJobVO batchJob = batchService.getBatchJobByName(jobName, false);

        if (batchJob == null)
            throw new BatchProcessException(BatchErrorType.APPLICATION_DATA_NOT_FOUND,
                    "Batch could not started as '"+jobName+"' data was not found");

        try {
            JobKey jobKey = JobKey.jobKey(jobName);
            List<? extends Trigger> list = context.getScheduler().getTriggersOfJob(jobKey);
            for (Trigger t : list) {
                TriggerKey triggerKey = t.getKey();

                if(batchJob.getInterval() != null) {
//                    Long diff = (t.getNextFireTime().getTime() - t.getPreviousFireTime()) / getIntervalTermInMS(batchJob.getIntervalTerm());

                } else {
                    LOGGER.info("[CustomQuartzJob - {}] Reschedule using CronScheduler {}", jobName, jobName);
                    QuartzConfig config = new QuartzConfig();
                    context.getScheduler().rescheduleJob(triggerKey,
                            config.buildNewTrigger(triggerKey, batchJob.getInterval(), batchJob.getIntervalTerm()));
                }
            }
        } catch (Exception e) {
            LOGGER.error("[executeInternal] FATAL ERROR job : {}, err : {}", jobName, e.getMessage());
        }
    }

    private int getIntervalTermInMS(String intervalTerm) {
        switch (intervalTerm.toUpperCase()) {
            case "F" : return 1;
            case "S" : return 1000;
            case "M" : return (60 * 1000);
            case "H" : return (60 * 60 * 1000);
            default : return 0;
        }
    }
}
