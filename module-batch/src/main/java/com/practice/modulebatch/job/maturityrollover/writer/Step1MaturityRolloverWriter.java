package com.practice.modulebatch.job.maturityrollover.writer;

import com.practice.modulebase.exception.BatchProcessException;
import com.practice.modulebatch.config.MsPlatSvcDSConfig;
import com.practice.modulebatch.job.maturityrollover.bo.MaturityRolloverBO;
import com.practice.modulebatch.service.BatchManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class Step1MaturityRolloverWriter extends CompositeItemWriter<MaturityRolloverBO>
        implements StepExecutionListener, ItemWriteListener<MaturityRolloverBO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step1MaturityRolloverWriter.class);

    @Autowired
    private MsPlatSvcDSConfig msPlatSvcDSConfig;

    private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private BatchManagerService batchManagerService;
    private JobParameters jobParams;
    private StepExecution stepExecution;
    private JdbcBatchItemWriter<MaturityRolloverBO> insertWriter;
    private JdbcBatchItemWriter<MaturityRolloverBO> deleteWriter;

    private EntityManager em;
    public void setBatchEntityManager(EntityManager em) { this.em = em; }
    public EntityManager getBatchPrimaryEM() { return em; }

    private List<BatchProcessException> failedDetails;
    private List<String> failedDetailKeys;

    private ItemWriter<MaturityRolloverBO> fileWriterDelegate;
    public void setFileWriterDelegate(ItemWriter<MaturityRolloverBO> fileWriterDelegate) { this.fileWriterDelegate = fileWriterDelegate; }
    public ItemWriter<MaturityRolloverBO> getFileWriterDelegate() { return fileWriterDelegate; }

    private String incomingFolder;
    public void setIncomingFolder(String incomingFolder) { this.incomingFolder = incomingFolder; }
    private String incomingFile;
    public void setIncomingFile(String incomingFile) { this.incomingFile = incomingFile; }

    public Step1MaturityRolloverWriter(BatchManagerService batchManagerService) {
        super();
        this.batchManagerService = batchManagerService;
    }

    @Override
    public void beforeWrite(List<? extends MaturityRolloverBO> items) {
        if (!CollectionUtils.isEmpty(items)) {
            MaturityRolloverBO item = items.get(0);
            LOGGER.info("[Step1MaturityRolloverWriter] before write, item = {}", items);
        }
    }

    @Override
    public void afterWrite(List<? extends MaturityRolloverBO> items) {
        Long totalRecord = 0L;
        if (stepExecution.getExecutionContext().containsKey("total.record")) {
            totalRecord = stepExecution.getExecutionContext().getLong("total.record") + 1;
        }
        stepExecution.getExecutionContext().putLong("total.record", ++totalRecord);
        LOGGER.info("Step1MaturityRolloverWriter afterwrite completed");
    }

    @Override
    public void onWriteError(Exception exception, List<? extends MaturityRolloverBO> items) {
        LOGGER.error("Error write Step1MaturityRolloverWriter");
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        failedDetails = new ArrayList<>();
        failedDetailKeys = new ArrayList<>();
        jobParams = stepExecution.getJobParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!failedDetails.isEmpty()) {
            LOGGER.error("Step1MaturityRolloverWriter, beforeStep failed details size : {}", failedDetails.size());
            return ExitStatus.FAILED;
        }
        LOGGER.info("[Step1MaturityRolloverWriter] readCount = {}, writeCount = {}",
                stepExecution.getReadCount(), stepExecution.getWriteCount());
        return null;
    }

    @Override
    public void write(final List<? extends MaturityRolloverBO> items) throws Exception {
        try {
            LOGGER.info("[Step1MaturityRolloverWriter] items.get(0).....");
        } catch (Exception e) {
            LOGGER.error("Step1MaturityRolloverWriter error on write");
            throw e;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setDelegates(new ArrayList<ItemWriter<? super MaturityRolloverBO>>());
    }

    @Override
    public void close() throws ItemStreamException {
        LOGGER.info("closing Step1MaturityRolloverWriter......");
        super.close();
    }

    public JdbcBatchItemWriter<MaturityRolloverBO> getInsertWriter() {
        return insertWriter;
    }

    public void setInsertWriter(JdbcBatchItemWriter<MaturityRolloverBO> insertWriter) {
        this.insertWriter = insertWriter;
    }

    public JdbcBatchItemWriter<MaturityRolloverBO> getDeleteWriter() {
        return deleteWriter;
    }

    public void setDeleteWriter(JdbcBatchItemWriter<MaturityRolloverBO> deleteWriter) {
        this.deleteWriter = deleteWriter;
    }
}
