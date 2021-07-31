package com.practice.modulebatch.job.maturityrollover.config;

import com.practice.modulebase.constant.BatchConstant;
import com.practice.modulebatch.config.AbstractSpringBatchConfiguration;
import com.practice.modulebatch.config.MsPlatSvcDSConfig;
import com.practice.modulebatch.job.maturityrollover.bo.MaturityRolloverBO;
import com.practice.modulebatch.service.BatchManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.practice.modulebatch.job.maturityrollover", "com.practice.modulebase"})
@Import({MsPlatSvcDSConfig.class})
public class MaturityRolloverConfiguration extends AbstractSpringBatchConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaturityRolloverConfiguration.class);

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private BatchManagerService batchManagerService;
    @Autowired
    @Qualifier(MsPlatSvcDSConfig.NAME)
    private MsPlatSvcDSConfig msPlatSvcDSConfig;
    private EntityManager em;

    @Value("${maturityrollover.incoming.folder.location}")
    String incomingFolderLocation;
    @Value("${maturityrollover.name.with.extention}")
    String flatFileName;
    @Value("file:${maturityrollover.incoming.folder.location}/${maturityrollover.name.with.extention}")
    private Resource[] inputResource;

    @Override
    public DataSource dataSource() {
        LOGGER.info("=========>Datasource MaturityRollover Initiate<========");
        return this.dataSource();
    }

    /**
     * name method as dataSourcePlatformSvc instead of dataSource
     * because it will throws error bean creation
     * Requested bean is currently in creation: Is there an unresolvable circular reference?
     */
    @Bean
    public DataSource dataSourcePlatformSvc(MsPlatSvcDSConfig msPlatSvcDSConfig) {
        return msPlatSvcDSConfig.dataSource();
    }

    @Bean(name = "MaturityRolloverJob")
    @Qualifier("jobMaturityRollover")
    public Job maturityRolloverJob() throws Exception {
        LOGGER.info("=========>MaturityRollover Job Initiate<========");
        return jobBuilders().get(BatchConstant.JobName.MATURITYROLLOVER_JOB)
                .repository(jobRepository())
                .start(step1MaturityRollover(stepBuilderFactory))
                .build();
    }

    @Bean
    public Step step1MaturityRollover(StepBuilderFactory stepBuilderFactory) throws Exception {
        DefaultTransactionAttribute txAttribute = new DefaultTransactionAttribute();
        TaskletStep step1 = stepBuilderFactory.get("step1MaturityRollover")
                .repository(jobRepository()).<MaturityRolloverBO, MaturityRolloverBO>chunk(1)
                .faultTolerant().skip(Exception.class).skipLimit(1)
                .reader(step1MaturityRolloverReader())
//                .processor()
                .transactionAttribute(txAttribute)
                .build();
        step1.setTransactionAttribute(new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        return step1;
    }

    @Bean
    public ItemReader<? extends MaturityRolloverBO> step1MaturityRolloverReader() {
        return null;
    }
}
