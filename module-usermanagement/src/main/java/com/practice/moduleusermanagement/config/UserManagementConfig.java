package com.practice.moduleusermanagement.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class UserManagementConfig {

    @Value("${user.datasource.url}")
    String url;
    @Value("${user.datasource.username}")
    String username;
    @Value("${user.datasource.password}")
    String password;
    @Value("${user.datasource.dialect}")
    String dialect;
    @Value("${user.datasource.minIdle:1}")
    int minIdle;
    @Value("${user.datasource.maxPoolSize:10}")
    int maxPool;
    @Value("${user.datasource.connectionTestQuery}")
    String testQuery;

    @Bean(name = "userManagementDB")
    public DataSource restDataSource() {
        HikariConfig dataSource = new HikariConfig();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinimumIdle(minIdle);
        dataSource.setMaximumPoolSize(maxPool);
        dataSource.setConnectionTestQuery(testQuery);

        log.info("Initiate userManagementDB........");
        return new HikariDataSource(dataSource);
    }
}
