package com.practice.modulebatch.job.batchjobtest.config;

import com.practice.modulebatch.config.AbstractSpringBatchConfiguration;
import com.practice.modulebatch.config.MsNotificationDSConfig;
import com.practice.modulebatch.job.batchjobtest.bo.NotificationStsBO;
import com.practice.modulebatch.job.batchjobtest.processor.Step2NotificationStsProcessor;
import com.practice.modulebatch.job.batchjobtest.reader.Step2NotificationStsReader;
import com.practice.modulebatch.job.batchjobtest.tasklet.TaskletNotificationStsReader;
import com.practice.modulebatch.job.batchjobtest.writer.Step2NotificationStsWriter;
import com.practice.modulebatch.service.BatchManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.practice.modulebatch.job.batchjobtest"})
@Import({MsNotificationDSConfig.class})
@EnableBatchProcessing
public class BatchJobTestConfig extends AbstractSpringBatchConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchJobTestConfig.class);

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private BatchManagerService batchManagerService;
    @Value("${notificationSts.job.record}")
    private int records;
    @Value("${notificationSts.job.sleep}")
    private int sleepInterval;
    @Value("${notificationSts.job.terminate}")
    private int terminateLimit;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public DataSource dataSource() {
        LOGGER.info("=========>Datasource Initiate<========");
        return this.dataSource();
    }
    @Autowired
    @Qualifier(MsNotificationDSConfig.NAME)
    private MsNotificationDSConfig msNotificationDSConfig;

//    @PersistenceContext(unitName = BatchCon)
    private EntityManager em;

    @Bean
    public DataSource dataSource(MsNotificationDSConfig msNotificationDSConfig) {
        return msNotificationDSConfig.dataSource();
    }

    @Bean
    @Autowired
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        LOGGER.info("=========>JobLauncher Initiate<========");
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Bean(name = "BatchJobTest")
    @Qualifier("jobBatchTest")
    public Job batchJobTest() throws Exception {
        LOGGER.info("=========>notificationStsJob Initiate with profile "+activeProfile+"<========");
        return jobBuilders().get("BatchJobTest").repository(jobRepository())
                .start(step1NotificationSts(stepBuilderFactory))
                .next(step2NotificationSts(stepBuilderFactory)).build();

    }

    @Bean
    public Step step2NotificationSts(StepBuilderFactory stepBuilderFactory) throws Exception {
        TaskletStep step2 = stepBuilderFactory.get("step2NotificationSts")
                .repository(jobRepository()).<NotificationStsBO, NotificationStsBO>chunk(1)
                .faultTolerant().skip(Exception.class).skipLimit(1)
                .reader(step2NotificationReader())
                .processor(step2NotificationStsProcessor())
                .writer(step2NotificationWriter())
                .listener(notificationStsBosListener())
                .build();
        step2.setTransactionAttribute(new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW));
        return step2;
    }

    @Bean
    public Object notificationStsBosListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[] { "notificationStsBos" });
        return listener;
    }

    @Bean
    public Step2NotificationStsWriter step2NotificationWriter() {
        Step2NotificationStsWriter writer = new Step2NotificationStsWriter(batchManagerService, msNotificationDSConfig.dataSource());
        writer.setBatchEntityManager(em);
        return writer;
    }

    @Bean
    public Step2NotificationStsProcessor step2NotificationStsProcessor() {
        Step2NotificationStsProcessor step2Processor = new Step2NotificationStsProcessor(batchManagerService, records, terminateLimit);
        step2Processor.setBatchEntityManager(em);
        return step2Processor;
    }

    @Bean
    public Step2NotificationStsReader step2NotificationReader() {
        return new Step2NotificationStsReader();
    }

    @Bean
    public Step step1NotificationSts(StepBuilderFactory stepBuilderFactory) throws Exception {
        TaskletNotificationStsReader tasklet = new TaskletNotificationStsReader(batchManagerService, em,msNotificationDSConfig.dataSource(), records,activeProfile);
        TaskletStep step1 = stepBuilderFactory.get("step1NotificationSts").repository(jobRepository()).tasklet(tasklet).build();
        step1.setTransactionAttribute(new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW));
        return step1;
    }
}
