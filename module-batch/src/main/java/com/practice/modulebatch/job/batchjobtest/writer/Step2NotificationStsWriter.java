package com.practice.modulebatch.job.batchjobtest.writer;

import com.practice.modulebatch.exception.BatchProcessException;
import com.practice.modulebatch.job.batchjobtest.bo.NotificationStsBO;
import com.practice.modulebatch.service.BatchManagerService;
import org.springframework.batch.core.*;
import org.springframework.batch.item.support.CompositeItemWriter;

import javax.persistence.EntityManager;
import java.util.List;

public class Step2NotificationStsWriter extends CompositeItemWriter<NotificationStsBO>
        implements StepExecutionListener, ItemWriteListener<NotificationStsBO> {

    private JobParameters jobParams;
    private StepExecution stepExecution;
    private List<BatchProcessException> failedDetails;
    private EntityManager em;

    public Step2NotificationStsWriter(BatchManagerService batchManagerService, int records, int terminateLimit) {
        super();
//        this.
    }

    @Override
    public void beforeWrite(List<? extends NotificationStsBO> items) {

    }

    @Override
    public void afterWrite(List<? extends NotificationStsBO> items) {

    }

    @Override
    public void onWriteError(Exception exception, List<? extends NotificationStsBO> items) {

    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
