package com.practice.modulebatch.job.maturityrollover.config;

import com.practice.modulebase.constant.BatchConstant;
import com.practice.modulebatch.config.AbstractSpringBatchConfiguration;
import com.practice.modulebatch.config.MsPlatSvcDSConfig;
import com.practice.modulebatch.job.maturityrollover.bo.MaturityRolloverBO;
import com.practice.modulebatch.job.maturityrollover.mapper.MaturityRolloverBOMapper;
import com.practice.modulebatch.job.maturityrollover.processor.Step1MaturityRolloverProcessor;
import com.practice.modulebatch.job.maturityrollover.reader.Step1MaturityRolloverReader;
import com.practice.modulebatch.job.maturityrollover.writer.Step1MaturityRolloverWriter;
import com.practice.modulebatch.service.BatchManagerService;
import com.practice.modulebatch.utils.BlankLineRecordSeparatorPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

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
                .processor(new Step1MaturityRolloverProcessor())
                .writer(step1MaturityWriter())
                .transactionAttribute(txAttribute)
                .build();
        step1.setTransactionAttribute(new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        return step1;
    }

    @Bean
    public Step1MaturityRolloverWriter step1MaturityWriter() {
//        Step1MaturityRolloverWriter writer = new Step1MaturityRolloverWriter(batchManagerService);
        Step1MaturityRolloverWriter writer = new Step1MaturityRolloverWriter(batchManagerService, em, msPlatSvcDSConfig.dataSource());
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        jdbcTemplate.setDataSource(dataSource());
//        writer.setJdbcTemplate(jdbcTemplate);
//        writer.setBatchEntityManager(em);
//        writer.setIncomingFolder(incomingFolderLocation);
//        writer.setIncomingFile(flatFileName);
//        writer.setInsertWriter(insertWriterMRO());
//        writer.setDeleteWriter(deleteWriterMRO());
        return writer;
    }

//    @Bean
//    @StepScope
//    public JdbcBatchItemWriter<MaturityRolloverBO> deleteWriterMRO() {
//    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<MaturityRolloverBO> insertWriterMRO() {
        LOGGER.info("==========>MaturityRollOver T_MRO_FEEDFILE<=========");
        return new JdbcBatchItemWriterBuilder<MaturityRolloverBO>()
                .dataSource(msPlatSvcDSConfig.dataSource())
                .assertUpdates(true)
                .sql("INSERT INTO T_MRO_FEEDFILE \n" +
                        "(ID, POLICY_NUMBER, TRANSACTION_TYPE, TRXN_REF_NO, DT_COMPLETION, STATUS, DT_CREATE, CREATE_BY) \n" +
                        "VALUES \n" +
                        "(:id, :policyNumber, :transactionType, :transactionRefNo, :completionDate, :status, :dateCreated, :createdBy)")
                .beanMapped().build();
    }

    @Bean
    public Step1MaturityRolloverReader step1MaturityRolloverReader() {
        File tmpDir = new File(incomingFolderLocation);
        boolean exists = tmpDir.exists();
        String[] pathnames;

        LOGGER.info("[Step1MaturityRolloverReader] {} isexist : {}", incomingFolderLocation, exists);
        LOGGER.info("[Step1MaturityRolloverReader] {} isDirectory : {}", incomingFolderLocation, tmpDir.isDirectory());
        LOGGER.info("[Step1MaturityRolloverReader] {} canExecute : {}", incomingFolderLocation, tmpDir.canExecute());
        LOGGER.info("[Step1MaturityRolloverReader] {} canRead : {}", incomingFolderLocation, tmpDir.canRead());
        LOGGER.info("[Step1MaturityRolloverReader] {} canWrite : {}", incomingFolderLocation, tmpDir.canWrite());

        pathnames = tmpDir.list();
        for (String pathname : pathnames) {
            LOGGER.info("[Step1MaturityRolloverReader] files inside this folder {}", pathname);
        }

        for (Resource x : inputResource) {
            LOGGER.info("[Step1MaturityRolloverReader] Reading FLAT file from resources {}", x.getFilename());
            try {
                LOGGER.info("[Step1MaturityRolloverReader] step into flatFile");
            } catch (Exception e) {
                LOGGER.error("[Step1MaturityRolloverReader] Unable to read Flat file from location {0}", e);
            }
        }

        Step1MaturityRolloverReader resourceItemReader = new Step1MaturityRolloverReader();
        resourceItemReader.setResources(inputResource);
        resourceItemReader.setDelegate(readerMRO());
        resourceItemReader.setComparator(new Comparator<Resource>() {
            @Override
            public int compare(Resource f1, Resource f2) {
                int compare = 0;
                try {
                    compare = Long.compare(f1.lastModified(), f2.lastModified());
                } catch (Exception e) {
                    LOGGER.error("Step1MaturityRolloverReader failed to compare file", e);
                }
                return compare;
            }
        });
        resourceItemReader.setBatchEntityManager(em);
        return resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<MaturityRolloverBO> readerMRO() {
        FlatFileItemReader<MaturityRolloverBO> reader = new FlatFileItemReader<MaturityRolloverBO>();
        reader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());
        DefaultLineMapper<MaturityRolloverBO> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(new DelimitedLineTokenizer("|"));
        lineMapper.setFieldSetMapper(new MaturityRolloverBOMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }
}
