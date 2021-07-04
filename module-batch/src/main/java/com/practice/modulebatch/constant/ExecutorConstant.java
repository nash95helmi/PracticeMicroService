package com.practice.modulebatch.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExecutorConstant {
    public static final String KEY_BATCH_HISTORY_ID = "batch.history.id";

    public static final class Input {
        public static final String JOB_NAME = "input.job.name";
        public static final String TRIGGERED_BY = "input.triggered.by";
        public static final String WORKING_DIR = "input.working.dir";
        public static final String STARTER_TYPE = "input.starter.type";
        public static final String PROCESS_DATE = "input.process.date";

    }
}
