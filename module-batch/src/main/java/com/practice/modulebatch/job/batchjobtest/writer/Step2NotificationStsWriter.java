package com.practice.modulebatch.job.batchjobtest.writer;

import com.practice.modulebatch.exception.BatchProcessException;
import com.practice.modulebatch.job.batchjobtest.bo.NotificationStsBO;
import com.practice.modulebatch.service.BatchManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class Step2NotificationStsWriter extends CompositeItemWriter<NotificationStsBO>
        implements StepExecutionListener, ItemWriteListener<NotificationStsBO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Step2NotificationStsWriter.class);
    private JobParameters jobParams;
    private StepExecution stepExecution;
    private List<BatchProcessException> failedDetails;
    private EntityManager em;

    public void setBatchEntityManager(EntityManager em) {
        this.em = em;
    }

    public EntityManager getBatchPrimaryEM() {
        return em;
    }

    private BatchManagerService batchManagerService;
    private DataSource dataSource;
    private List<String> failedDetailKeys;

    public Step2NotificationStsWriter(BatchManagerService batchManagerService, DataSource dataSource) {
        super();
        this.batchManagerService = batchManagerService;
        this.dataSource = dataSource;
    }

    @Override
    public void beforeWrite(List<? extends NotificationStsBO> items) {
        LOGGER.info("Step2NotificationStsWriter beforeWrite");
        if (items.size() > 0) {
            NotificationStsBO item = items.get(0);
        }
    }

    @Override
    public void afterWrite(List<? extends NotificationStsBO> items) {
        Long totalRec = 0L;
        if (stepExecution.getExecutionContext().containsKey("total.record")) {
            totalRec = stepExecution.getExecutionContext().getLong("total.record") + 1;
        }
        stepExecution.getExecutionContext().getLong("total.record",++totalRec);

        LOGGER.info("Step2NotificationStsWriter afterwrite completed");
    }

    @Override
    public void onWriteError(Exception exception, List<? extends NotificationStsBO> items) {
        LOGGER.error("Error at Step2NotificationStsWriter", exception);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("Step2NotificationStsWriter beforeStep");
        this.stepExecution = stepExecution;

        failedDetailKeys = new ArrayList<>();
        jobParams = stepExecution.getJobParameters();
        failedDetailKeys = new ArrayList<>();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
//        if (!failedDetails.isEmpty()) {
//            LOGGER.info("Step2NotificationStsWriter afterstep Failed, {}", failedDetailKeys.toString());
//            return ExitStatus.FAILED;
//        }
        LOGGER.info("Step2NotificationStsWriter afterstep Complete");
        return ExitStatus.COMPLETED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setDelegates(new ArrayList<ItemWriter<? super NotificationStsBO>>());
    }

    @Override
    public void write(final List<? extends  NotificationStsBO> items) throws Exception{
        LOGGER.info("[Step2NotificationStsWriter] write");
        if (!items.isEmpty() && items.size() > 0) {
            items.forEach(f -> {
                LOGGER.info("[Step2NotificationStsWriter] write item : {}", f.toString());
            });
        }
    }
}
