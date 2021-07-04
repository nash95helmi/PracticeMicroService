package com.practice.modulebatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

public class DefaultBatchConfigurer implements BatchConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBatchConfigurer.class);

    private DataSource dataSource;
    private PlatformTransactionManager transactionManager;
    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;

    /**
     *  Sets the Datasource. If the datasource has been set once
     *  all future values are passed are ignored, to prevent @Autowired
     *  overwriting the value
     */
    @Autowired(required = false)
    public void setDataSource(DataSource dataSource) {
        if (this.dataSource == null)
            this.dataSource = dataSource;

        if (getTransactionManager() == null) {
            LOGGER.warn("No transaction manager was provided, using DataSourceTransactionManager");
            this.transactionManager = new DataSourceTransactionManager(this.dataSource);
        }
    }

    protected DefaultBatchConfigurer() {}

    public DefaultBatchConfigurer(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public JobRepository getJobRepository() throws Exception {
        return jobRepository;
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return jobLauncher;
    }

    @Override
    public JobExplorer getJobExplorer() throws Exception {
        return jobExplorer;
    }

    @PostConstruct
    public void initialize() {
        try{
            if (dataSource == null) {
                LOGGER.warn("No datasource provided...using Map based JobRepository");

                if (getTransactionManager() == null) {
                    LOGGER.warn("No transactionManager provided...using ResourcelessTransactionManager");
                    this.transactionManager = new ResourcelessTransactionManager();
                }

                MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(getTransactionManager());
                jobRepositoryFactory.afterPropertiesSet();
                this.jobRepository = jobRepositoryFactory.getObject();

                MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(jobRepositoryFactory);
                jobExplorerFactory.afterPropertiesSet();
                this.jobExplorer = jobExplorerFactory.getObject();

            } else {
                this.jobRepository = createJobRepository();
                this.jobExplorer = createJobExplorer();
            }

            this.jobLauncher = createJobLauncher();
        } catch (Exception e) {
            throw new BatchConfigurationException(e);
        }
    }

    private JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.afterPropertiesSet();
        return simpleJobLauncher;
    }

    private JobExplorer createJobExplorer() throws Exception {
        JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setDataSource(this.dataSource);
        jobExplorerFactoryBean.afterPropertiesSet();
        return jobExplorerFactoryBean.getObject();
    }

    private JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(getTransactionManager());
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
