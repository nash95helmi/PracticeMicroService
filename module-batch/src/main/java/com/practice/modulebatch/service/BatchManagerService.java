package com.practice.modulebatch.service;

import com.practice.modulebatch.exception.BatchProcessException;
import com.practice.modulebatch.vo.BatchHistoryVO;
import com.practice.modulebatch.vo.BatchJobVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BatchManagerService {

    public BatchJobVO getBatchJobByName(String jobName, boolean eagerFetch) {
        BatchJobVO job = new BatchJobVO();
        job.setId("868ea42a08924ff087184bddc5b1a5f2");
        job.setName("BatchJobTest");
        job.setActive(true);
        job.setStatus("IDLE");
        job.setIntervalTerm("0 */5 * ? * *");
        return job;
    }

    public BatchJobVO getMaturityRolloverJob(String jobName, boolean eagerFetch) {
        BatchJobVO job = new BatchJobVO();
        job.setId("868ea42a08924ff087184bddc5b1a5f3");
        job.setName("MaturityRolloverJob");
        job.setActive(true);
        job.setStatus("IDLE");
        job.setIntervalTerm("0 */5 * ? * *");
        return job;
    }

    public void createFailedDetail(BatchHistoryVO history, List<BatchProcessException> failedDetails) {
    }
}
