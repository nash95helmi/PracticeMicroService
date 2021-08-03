package com.practice.module.config;

//import static com.practice.module.config.RestConfig.PERSISTENCE_UNIT_NAME;
//import static com.practice.module.config.RestConfig.REST_TRX_MANAGER;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Slf4j
//@EnableAspectJAutoProxy
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "com.practice.module",
//                        entityManagerFactoryRef = PERSISTENCE_UNIT_NAME, transactionManagerRef = REST_TRX_MANAGER)
public class RestConfig {

    @Value("${rest.datasource.url}")
    String url;
    @Value("${rest.datasource.username}")
    String username;
    @Value("${rest.datasource.password}")
    String password;

//    @Value("${rest.datasource.url}")
//    String url;
//    @Value("${rest.datasource.username}")
//    String username;
//    @Value("${rest.datasource.password}")
//    String password;
//    @Value("${rest.datasource.dialect}")
//    String dialect;
//    @Value("${rest.datasource.minIdle:1}")
//    int minIdle;
//    @Value("${rest.datasource.maxPoolSize:10}")
//    int maxPool;
//    @Value("${rest.datasource.connectionTestQuery}")
//    String testQuery;

    @Bean(name = "restDB")
    public DataSource restDataSource() {
        HikariConfig dataSource = new HikariConfig();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(2);
//        dataSource.setConnectionTestQuery(testQuery);

        log.info("Initiate restDB........");
        return new HikariDataSource(dataSource);
    }

//    public static final String PERSISTENCE_UNIT_NAME = "RESTMODULE.DB";
//    public static final String REST_TRX_MANAGER = "restTransactionManager";
//
//    @Value("${rest.db.dialect}")
//    public String hibernateDialect;
//
//    @Value("${rest.db.ddl-auto:none}")
//    public String hibernateHbm2ddlAuto;
//
//    @Primary
//    @Bean("restDataSource")
//    public DataSource dataSource(@Value("${rest.datasource.url}") final String url,
//                                 @Value("${rest.datasource.username}") final String username,
//                                 @Value("${rest.datasource.password}") final String password,
//                                 @Value("${rest.db.datasource.minIdle}") final int minIdle,
//                                 @Value("${rest.db.datasource.maxPoolSize}") final int maxPoolSize,
//                                 @Value("${rest.db.datasource.connectionTestQuery}") final String connectionTestQuery,
//                                 @Value("${rest.db.datasource.validationTimeout}") final int validationTimeout,
//                                 @Value("${rest.db.datasource.idleTimeout:300000}") final long idleTimeout) {
//
//        HikariConfig dataSourceConfig = new HikariConfig();
//        dataSourceConfig.setJdbcUrl(url);
//        dataSourceConfig.setUsername(username);
//        dataSourceConfig.setPassword(password);
//        dataSourceConfig.setMinimumIdle(minIdle);
//        dataSourceConfig.setMaximumPoolSize(maxPoolSize);
//        dataSourceConfig.setConnectionTestQuery(connectionTestQuery);
//        dataSourceConfig.setValidationTimeout(validationTimeout);
//        dataSourceConfig.setIdleTimeout(idleTimeout);
//        return new HikariDataSource(dataSourceConfig);
//    }
//
//    @Primary
//    @Bean(PERSISTENCE_UNIT_NAME)
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
//            @Qualifier("restDataSource") final DataSource dataSource) {
//
//        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
//        em.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        em.setJpaDialect(new HibernateJpaDialect());
////        em.setPackagesToScan("com.practice.module.vo");
//        em.setDataSource(dataSource);
//        em.setJpaProperties(additionalProperties());
//        return em;
//    }
//
//    private Properties additionalProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.dialect", hibernateDialect);
//        properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
//        return properties;
//    }
//
//    @Primary
//    @Bean(REST_TRX_MANAGER)
//    public PlatformTransactionManager transactionManager(
//            @Qualifier(PERSISTENCE_UNIT_NAME) LocalContainerEntityManagerFactoryBean entityManagerFactory) {
//
//        JpaTransactionManager txnManager = new JpaTransactionManager();
//        txnManager.setEntityManagerFactory(entityManagerFactory.getObject());
//        return txnManager;
//    }
}
