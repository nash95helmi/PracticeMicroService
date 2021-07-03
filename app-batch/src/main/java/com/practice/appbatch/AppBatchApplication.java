package com.practice.appbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

//witout exclude, hit error on JPA becoz by default it init at the beginning
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, FlywayAutoConfiguration.class})
@ComponentScan(basePackages = {"com.practice.appbatch", "com.practice.modulebatch"})
@EnableBatchProcessing
public class AppBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppBatchApplication.class, args);
	}

	@Value("${user.timezone}")
	private String timeZone;

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	}
}
