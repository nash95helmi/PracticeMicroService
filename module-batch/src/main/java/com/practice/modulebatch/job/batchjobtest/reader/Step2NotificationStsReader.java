package com.practice.modulebatch.job.batchjobtest.reader;

import com.practice.modulebatch.job.batchjobtest.bo.NotificationStsBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.IteratorItemReader;

import java.util.List;

public class Step2NotificationStsReader implements StepExecutionListener, ItemReadListener<NotificationStsBO>, ItemReader<NotificationStsBO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step2NotificationStsReader.class);

    private StepExecution stepExecution;
    private ItemReader<NotificationStsBO> delegate;
    private List<NotificationStsBO> notificationStsBOS;

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(NotificationStsBO item) {
        if (item != null) {
//            item.set
        }
        LOGGER.info("[Step2NotificationStsReader] ***** COMMIT COUNT :{} \n"+ " ***** FILTER COUNT : {} \n" +
                "***** PROCESS SKIP COUNT : {} \n" + "***** READ COUNT : {} \n" +
                "***** READ SKIP COUNT : {} \n" + "***** ROLLBACK COUNT : {} \n" + "***** SKIP COUNT : {}" +
                "***** WRITE COUNT : {} \n" + "***** WRITE SKIP COUNT : {}",
                stepExecution.getCommitCount(), stepExecution.getFilterCount(), stepExecution.getProcessSkipCount(),
                stepExecution.getReadCount(), stepExecution.getReadSkipCount(), stepExecution.getRollbackCount(),
                stepExecution.getSkipCount(), stepExecution.getWriteCount(), stepExecution.getWriteSkipCount());
    }

    @Override
    public void onReadError(Exception ex) {

    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        this.notificationStsBOS = (List<NotificationStsBO>) jobContext.get("notificationStsBOS");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        LOGGER.info("Step2NotificationStsReader completed");
        delegate = null;
        return ExitStatus.COMPLETED;
    }

    @Override
    public NotificationStsBO read() throws Exception {
        if (delegate == null) {
            LOGGER.info("Step2NotificationStsReader read delegate is null");
            delegate = new IteratorItemReader<NotificationStsBO>(notificationStsBOS);
        }
        LOGGER.info("Step2NotificationStsReader-read");
        return delegate.read();
    }
}
