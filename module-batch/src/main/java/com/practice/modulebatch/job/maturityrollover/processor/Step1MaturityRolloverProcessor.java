package com.practice.modulebatch.job.maturityrollover.processor;

import com.practice.modulebatch.job.maturityrollover.bo.MaturityRolloverBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class Step1MaturityRolloverProcessor implements ItemProcessor<MaturityRolloverBO, MaturityRolloverBO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Step1MaturityRolloverProcessor.class);

    @Override
    public MaturityRolloverBO process(MaturityRolloverBO item) throws Exception {
        LOGGER.info("Step1MaturityRolloverProcessor process item : {}", item.toString());
        return item;
    }
}
