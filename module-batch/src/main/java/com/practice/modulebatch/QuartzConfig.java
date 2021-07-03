package com.practice.modulebatch;

import com.practice.modulebatch.constant.BatchConstant;
import com.practice.modulebatch.service.BatchManagerService;
import com.practice.modulebatch.vo.BatchJobVO;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Configuration
public class QuartzConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzConfig.class);

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobLocator jobLocator;
    @Autowired
    private JobExplorer jobExplorer;
    @Autowired
    private BatchManagerService batchService;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(jobBatchTestTrigger());
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setJobDetails(jobBatchTest());
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setOverwriteExistingJobs(true);
        return scheduler;
    }

    @Bean
    public JobDetail jobBatchTest() {
        //Set Job Data Map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", BatchConstant.JobName.BATCH_JOB_TEST);
        jobDataMap.put("input.job.name", BatchConstant.JobName.BATCH_JOB_TEST);
        jobDataMap.put("jobLauncher", jobLauncher);
        jobDataMap.put("jobLocator", jobLocator);
        jobDataMap.put("triggerBy", "5ebaa7818723494d8962e9f69777d43c");
        jobDataMap.put("workingDir", "-");
        jobDataMap.put("starterType", "-");
        jobDataMap.put("workingDay", "-");
        jobDataMap.put("processDate", new Date());
        jobDataMap.put("batchService", batchService);
        jobDataMap.put("jobExplore", jobExplorer);

        return JobBuilder.newJob(CustomQuartzJob.class).withIdentity("BatchJobTest").setJobData(jobDataMap)
                .storeDurably().build();
    }

    @Bean
    public Trigger jobBatchTestTrigger() {
        BatchJobVO job = batchService.getBatchJobByName("BatchJobTest", false);
        return buildTrigger(job, "jobBatchTest", jobBatchTest());
    }

    public Trigger buildTrigger(BatchJobVO job, String jobName, JobDetail jobDetail) {
        CronScheduleBuilder cronScheduleBuilder = getCronScheduleBuilder(job.getIntervalTerm());

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail).withIdentity(jobName)
                .withSchedule(cronScheduleBuilder).build();

    }

    public Trigger buildNewTrigger(TriggerKey triggerKey, Integer interval, String intervalTerm) {
        CronScheduleBuilder cronScheduleBuilder = getCronScheduleBuilder(intervalTerm);

        return TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder).build();
    }

    private CronScheduleBuilder getCronScheduleBuilder(String intervalTerm) {
        return CronScheduleBuilder.cronSchedule(intervalTerm);
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
