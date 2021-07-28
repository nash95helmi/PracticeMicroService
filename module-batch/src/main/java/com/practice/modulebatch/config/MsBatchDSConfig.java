package com.practice.modulebatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration(MsBatchDSConfig.NAME)
public class MsBatchDSConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsBatchDSConfig.class);
    public static final String NAME = "MsBatchDS";

    @Value("${msbatch.datasource.url}")
    String url;
    @Value("${msbatch.datasource.username}")
    String username;
    @Value("${msbatch.datasource.password}")
    String password;
    @Value("${msbatch.datasource.datasource.minIdle:1}")
    int minIdle;
    @Value("${msbatch.datasource.datasource.maxPoolSize:2}")
    int maxPool;

//    @Bean("MsBatchDS")
    public DataSource batchDataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setJdbcUrl(url);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setMinimumIdle(minIdle);
        dataSourceConfig.setMaximumPoolSize(maxPool);
        return new HikariDataSource(dataSourceConfig);
    }
}
