package com.practice.modulebatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration(MsPlatSvcDSConfig.NAME)
public class MsPlatSvcDSConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsPlatSvcDSConfig.class);
    public static final String NAME = "MsPlatSvcDS";

    @Value("${msplatsvc.datasource.url}")
    String url;
    @Value("${msplatsvc.datasource.username}")
    String username;
    @Value("${msplatsvc.datasource.password}")
    String password;
    @Value("${msplatsvc.datasource.datasource.minIdle:1}")
    int minIdle;
    @Value("${msplatsvc.datasource.datasource.maxPoolSize:2}")
    int maxPool;

//    @Bean("MsPlatsvcDS")
    public DataSource dataSource() {
        LOGGER.info("MsPlatSvcDSConfig Initiated...........");
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setJdbcUrl(url);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setMinimumIdle(minIdle);
        dataSourceConfig.setMaximumPoolSize(maxPool);
        return new HikariDataSource(dataSourceConfig);
    }
}
