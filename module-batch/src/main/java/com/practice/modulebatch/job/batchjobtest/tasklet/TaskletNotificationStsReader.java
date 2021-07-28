package com.practice.modulebatch.job.batchjobtest.tasklet;

import com.practice.modulebatch.constant.ExecutorConstant;
import com.practice.modulebatch.exception.BatchErrorType;
import com.practice.modulebatch.exception.BatchProcessException;
import com.practice.modulebatch.job.batchjobtest.bo.NotificationStsBO;
import com.practice.modulebatch.service.BatchManagerService;
import com.practice.modulebatch.vo.BatchHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskletNotificationStsReader implements Tasklet, StepExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskletNotificationStsReader.class);

    private StepExecution stepExecution;
    private List<String> failedDetailKeys;
    private List<BatchProcessException> failedDetails;
    private JobParameters jobParams;
    private BatchManagerService batchManagerService;
    private EntityManager em;
    private DataSource dataSource;
    private List<NotificationStsBO> notificationStsBOS;
    private int records;
    private String activeProfile;

    public TaskletNotificationStsReader(BatchManagerService batchManagerService, EntityManager em,
                                        DataSource dataSource, int records, String activeProfile) {
        this.batchManagerService = batchManagerService;
        this.em = em;
        this.dataSource = dataSource;
        this.notificationStsBOS = new ArrayList<>();
        this.records = records;
        this.activeProfile = activeProfile;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        failedDetails = new ArrayList<>();
        jobParams = stepExecution.getJobParameters();
        failedDetailKeys = new ArrayList<>();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!failedDetails.isEmpty()) {
            if (batchManagerService != null) {
                try {
//                    BatchHistoryVO history = new BatchHistoryVO();
//                    String historyId = jobParams.getString(ExecutorConstant.KEY_BATCH_HISTORY_ID);
//                    history = retrieveBatchHistoryVo(historyId);
//                    LOGGER.info("Failed details size : {}", failedDetails.size());
//                    batchManagerService.createFailedDetail(history, failedDetails);
                } catch (Exception e) {
                    LOGGER.error("[BATCH] Error found on getting batch id from job parameters");
                }
            }
            stepExecution.setWriteSkipCount(stepExecution.getWriteSkipCount() == 0 ?
                    0 : stepExecution.getWriteSkipCount() - 1);

            return ExitStatus.FAILED;
        }
        return null;
    }

    private BatchHistoryVO retrieveBatchHistoryVo(String historyId) {
        String sqlString = null;
        sqlString = "select * from batch_hisroty where id = :id";
        List<BatchHistoryVO> resultList = em.createNativeQuery(sqlString, BatchHistoryVO.class)
                .setParameter("id", historyId).getResultList();

        if (resultList == null || resultList.isEmpty())
            return null;

        return resultList.get(0);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ");
            sql.append("document_manager");

            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
                ResultSet rs = preparedStatement.executeQuery();
                LOGGER.info("TaskletNotificationStsReader query : {}", sql.toString());
                notificationStsBOS = new ArrayList<>();
                while (rs.next()){
                    NotificationStsBO notificationStsBO = new NotificationStsBO();
                    notificationStsBO.setId(rs.getString("ID"));
                    notificationStsBO.setManagerId(rs.getString("MANAGER_ID"));
                    notificationStsBO.setDocId(rs.getString("DOCUMENT_ID"));
                    notificationStsBO.setCreatedBy(rs.getString("CREATED_BY"));
                    notificationStsBO.setDtCreate(rs.getDate("DT_CREATE"));
                    notificationStsBO.setStatus(rs.getString("STATUS"));
                    notificationStsBOS.add(notificationStsBO);
                }
                LOGGER.info("TaskletNotificationStsReader size : {}", notificationStsBOS.size());
                ExecutionContext stepContext = stepExecution.getJobExecution().getExecutionContext();
                stepContext.put("notificationStsBOS", notificationStsBOS);
            } catch (Exception e) {
                throw new BatchProcessException("Unable to read transaction", e);
            }

        } catch (BatchProcessException e) {
            stepExecution.setReadCount(1);
            stepExecution.setWriteCount(0);

            BatchProcessException failedException = new BatchProcessException(BatchErrorType.SYSTEM_SPRBATCH_ERR, e);

            if (!failedDetailKeys.contains(failedException.getMessage())) {
                failedDetailKeys.add(failedException.getMessage());
                failedDetails.add(failedException);
            }
            throw e;
        }
        return RepeatStatus.FINISHED;
    }
}
