package com.practice.modulebatch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration(MsNotificationDSConfig.NAME)
public class MsNotificationDSConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsNotificationDSConfig.class);
    public static final String NAME = " MsNotificationDS";

    @Value("${msnotification.datasource.url}")
    String url;
    @Value("${msnotification.datasource.username}")
    String username;
    @Value("${msnotification.datasource.password}")
    String password;
    @Value("${msnotification.datasource.datasource.minIdle:1}")
    int minIdle;
    @Value("${msnotification.datasource.datasource.maxPoolSize:2}")
    int maxPool;

//    @Bean("MsNotificationDS")
    public DataSource dataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setJdbcUrl(url);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        dataSourceConfig.setMinimumIdle(minIdle);
        dataSourceConfig.setMaximumPoolSize(maxPool);
        return new HikariDataSource(dataSourceConfig);
    }
}
