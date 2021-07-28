package com.practice.modulebatch.job.batchjobtest.processor;

import com.practice.modulebatch.constant.ExecutorConstant;
import com.practice.modulebatch.exception.BatchProcessException;
import com.practice.modulebatch.job.batchjobtest.bo.NotificationStsBO;
import com.practice.modulebatch.service.BatchManagerService;
import com.practice.modulebatch.vo.BatchHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Step2NotificationStsProcessor implements ItemProcessor<NotificationStsBO, NotificationStsBO>, StepExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step2NotificationStsProcessor.class);

    private BatchManagerService batchManagerService;
    private List<BatchProcessException> failedDetails;
    private List<String> failedDetailKeys;
    private EntityManager em;

    public void setBatchEntityManager(EntityManager em) {
        this.em = em;
    }

    public EntityManager getBatchPrimaryEM() {
        return em;
    }

    private JobParameters jobParams;
    private StepExecution stepExecution;
    private int sleepInterval;
    private int failedCount;
    private int terminateLimit;

    public Step2NotificationStsProcessor(BatchManagerService batchManagerService, int sleepInterval, int terminateLimit) {
        super();
        this.batchManagerService = batchManagerService;
        this.sleepInterval = sleepInterval;
        this.terminateLimit = terminateLimit;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("Step2NotificationStsProcessor beforeStep");
        this.stepExecution = stepExecution;
        failedDetails = new ArrayList<>();
        failedDetailKeys = new ArrayList<>();
        jobParams = stepExecution.getJobParameters();
        failedCount = 0;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!failedDetails.isEmpty()) {
            if (batchManagerService != null) {
                try {
                    LOGGER.info("Step2NotificationStsProcessor afterstep");
                    BatchHistoryVO history = new BatchHistoryVO();
                    String historyId = jobParams.getString(ExecutorConstant.KEY_BATCH_HISTORY_ID);
                    history = retrieveBatchHistoryVo(historyId);
                    LOGGER.info("Failed details size : {}", failedDetails.size());
                    batchManagerService.createFailedDetail(history, failedDetails);
                } catch (Exception e) {
                    LOGGER.error("[BATCH] Error found in job Parameters");
                }
            }
            return ExitStatus.FAILED;
        }
        return ExitStatus.COMPLETED;
    }

    private BatchHistoryVO retrieveBatchHistoryVo(String historyId) {
        String sqlString = null;
        sqlString = "select * from batch_history where id = :id";
        List<BatchHistoryVO> resultList = getBatchPrimaryEM().createNativeQuery(sqlString, BatchHistoryVO.class)
                .setParameter("id", historyId).getResultList();

        if (resultList == null || resultList.isEmpty())
            return null;

        return resultList.get(0);
    }

    @Override
    public NotificationStsBO process(NotificationStsBO item) throws Exception {
        if (Objects.nonNull(item)) {
            LOGGER.info("[Step2NotificationStsProcessor] process, item : {}", item.toString());
        }
        return item;
    }
}
