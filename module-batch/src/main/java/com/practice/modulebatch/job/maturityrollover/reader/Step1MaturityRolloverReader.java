package com.practice.modulebatch.job.maturityrollover.reader;

import com.practice.modulebase.exception.BatchProcessException;
import com.practice.modulebatch.job.maturityrollover.bo.MaturityRolloverBO;
import com.practice.modulebatch.service.BatchManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class Step1MaturityRolloverReader extends MultiResourceItemReader<MaturityRolloverBO>
        implements StepExecutionListener, ItemReadListener<MaturityRolloverBO>, ItemReader<MaturityRolloverBO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step1MaturityRolloverReader.class);

    private StepExecution stepExecution;
    private List<String> failedDetailKeys;
    private List<BatchProcessException> failedDetails;
    private JobParameters jobParams;
    private EntityManager em;

    @Autowired
    private BatchManagerService batchManagerService;

    public void setBatchEntityManager(EntityManager em) {
        this.em = em;
    }

    public EntityManager getBatchPrimaryEM() {
        return em;
    }

    @Override
    public void beforeRead() { }

    @Override
    public void afterRead(MaturityRolloverBO item) {
        if (item != null) {
//            item.set
        }
        LOGGER.info("[Step1MaturityRolloverReader] ***** COMMIT COUNT :{} \n"+ "***** FILTER COUNT : {} \n" +
                        "***** PROCESS SKIP COUNT : {} \n" + "***** READ COUNT : {} \n" +
                        "***** READ SKIP COUNT : {} \n" + "***** ROLLBACK COUNT : {} \n" + "***** SKIP COUNT : {} \n" +
                        "***** WRITE COUNT : {} \n" + "***** WRITE SKIP COUNT : {}",
                stepExecution.getCommitCount(), stepExecution.getFilterCount(), stepExecution.getProcessSkipCount(),
                stepExecution.getReadCount(), stepExecution.getReadSkipCount(), stepExecution.getRollbackCount(),
                stepExecution.getSkipCount(), stepExecution.getWriteCount(), stepExecution.getWriteSkipCount());
    }

    @Override
    public void onReadError(Exception ex) { }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        failedDetails = new ArrayList<>();
        failedDetailKeys = new ArrayList<>();
        jobParams = stepExecution.getJobParameters();
        LOGGER.info("failedDetails Reader : {}", failedDetails);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("Step1MaturityRolloverReader afterStep, failedDetails empty : {}",failedDetails.isEmpty());
        if (!failedDetails.isEmpty()) {
            return ExitStatus.FAILED;
        }
        LOGGER.info("Step1MaturityRolloverReader completed : {}",failedDetails.isEmpty());
        return null;
    }

    @Override
    public MaturityRolloverBO read() throws Exception {
        try {
            LOGGER.info("Reading FeedFile for Maturity Rollover........");
            return super.read();
        } catch (Exception e) {
            LOGGER.error("ERROR on Reading FeedFile for Maturity Rollover........");
            throw e;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            LOGGER.info("Opening FeedFile for Maturity Rollover........");
            super.open(executionContext);
        } catch (Exception e) {
            LOGGER.error("ERROR on Opening FeedFile for Maturity Rollover........");
            throw e;
        }
    }
}
