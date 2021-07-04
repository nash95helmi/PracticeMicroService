package com.practice.modulebatch;

import com.practice.modulebatch.constant.ExecutorConstant;
import com.practice.modulebatch.exception.BatchErrorType;
import com.practice.modulebatch.exception.BatchProcessException;
import com.practice.modulebatch.service.BatchManagerService;
import com.practice.modulebatch.vo.BatchHistoryVO;
import com.practice.modulebatch.vo.BatchJobVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
                    Long diff = (t.getNextFireTime().getTime() - t.getPreviousFireTime().getTime()) / getIntervalTermInMS(batchJob.getIntervalTerm());
                    if (Long.compare(diff, batchJob.getInterval().longValue()) != 0) {
                        LOGGER.info("[CustomQuartzJob - {}] Reschedule using SimpleScheduler {}", jobName, jobName);
                        QuartzConfig config = new QuartzConfig();
                        context.getScheduler().rescheduleJob(triggerKey,
                                config.buildNewTrigger(triggerKey, batchJob.getInterval(), batchJob.getIntervalTerm()));
                    }
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

        if (StringUtils.isNotEmpty(triggerBy))
            userId = triggerBy;

        if (StringUtils.isNotEmpty(workingDay))
            LOGGER.info(WORKING_DAY+" :["+workingDay+"]");

        if (StringUtils.isNotEmpty(starterType))
            LOGGER.info(ExecutorConstant.Input.STARTER_TYPE +" :["+starterType+"]");

        String histId = "-";
        String triggerBy = "-";

        try {
            BatchHistoryVO historyVo = createBatchRecord(jobName, userId, workingDir, starterType);
//            BatchHistoryVO historyVo = null;
            if (Objects.nonNull(historyVo)) {
                histId = historyVo.getId();
                triggerBy = historyVo.getCreatedBy();
            } else {
                triggerBy = userId;
            }

            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addString("jobName", jobName)
                    .addString(ExecutorConstant.Input.JOB_NAME, jobName)
                    .addString(ExecutorConstant.Input.TRIGGERED_BY, triggerBy)
                    .addString(ExecutorConstant.Input.WORKING_DIR, workingDir)
                    .addString(ExecutorConstant.Input.STARTER_TYPE, starterType)
                    .addString(WORKING_DAY, workingDay)
                    .addDate(ExecutorConstant.Input.PROCESS_DATE, processDate)
                    .addString(ExecutorConstant.KEY_BATCH_HISTORY_ID, histId)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            try {
                this.beforeJob(jobName);

                org.springframework.batch.core.Job job = jobLocator.getJob(jobName);
                JobExecution jobExecution = jobLauncher.run(job, params);
                List<StepExecution> stepExecList = new ArrayList<>(jobExecution.getStepExecutions());

                StepExecution stepExecution = "BatchJobTest".equals(jobName) ?
                        stepExecList.get(1) : stepExecList.get(0);
//                if ("BatchJobTest".equals(jobName)) {
//                    stepExecution = stepExecList.get(1);
//                } else {
//                    stepExecution = stepExecList.get(0);
//                }

                if (stepExecution.getReadCount() == stepExecution.getWriteCount()) {
                    numSuccess = stepExecution.getWriteCount();
                    numFail = 0;
                } else {
                    numSuccess = stepExecution.getWriteCount();
                    numFail = stepExecution.getReadCount() - stepExecution.getWriteCount();
                }
                LOGGER.info("[CustomQuartzJob : {}] ***** COMMIT COUNT : {} \n" + "***** FILTER COUNT : {} \n" +
                        "***** PROCESS SKIP COUNT : {} \n" + "***** READ COUNT : {} \n" +
                        "***** READ SKIP COUNT : {} \n" + "***** ROLLBACK COUNT : {} \n" + "***** SKIP COUNT : {} \n" +
                        "***** WRITE COUNT : {} \n" + "***** WRITE SKIP COUNT : {} \n",
                        jobName, stepExecution.getCommitCount(), stepExecution.getFilterCount(), stepExecution.getProcessSkipCount(),
                        stepExecution.getReadCount(), stepExecution.getReadSkipCount(), stepExecution.getRollbackCount(),
                        stepExecution.getSkipCount(), stepExecution.getWriteCount(), stepExecution.getWriteSkipCount());

                checKStepUpdateExecutionFailure(stepExecution, jobExecution);
            } catch (Exception e) {
                exception = e;
                LOGGER.error("[CustomQuartzJob-{}] FATAL ERROR : execution()", jobName, e);
            }
            endAndUpdateBatchRecord(params, historyVo);
        } catch (Exception e) {
            LOGGER.error("[CustomQuartzJob-{}] FATAL ERROR : ", jobName, e);
        } finally {
            LOGGER.info("[CustomQuartzJob-{}] ***** numSuccess : {}", jobName, numSuccess);
            LOGGER.info("[CustomQuartzJob-{}] ***** numFail : {}", jobName, numFail);
        }
    }

    private void endAndUpdateBatchRecord(JobParameters params, BatchHistoryVO historyVo) {
    }

    private BatchHistoryVO createBatchRecord(String jobName, String userId, String workingDir, String starterType) {
        return null;
    }

    private void checKStepUpdateExecutionFailure(StepExecution stepExecution, JobExecution jobExecution) {
    }

    private void beforeJob(String jobName) {
        if ("PurgeCreditCardJob".equals(jobName)) {
            int runningJobsCount = jobExplorer.findRunningJobExecutions(jobName).size();
            if (runningJobsCount > 1)
                throw new BatchProcessException(BatchErrorType.SYSTEM_GENERAL_ERROR,
                        "This "+jobName+" already running");
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
