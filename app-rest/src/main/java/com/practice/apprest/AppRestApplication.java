package com.practice.apprest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.time.Clock;
import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = {"com.practice.apprest", "com.practice.module"})
public class AppRestApplication extends SpringBootServletInitializer {

	private static final Logger APP_LOGGER = LoggerFactory.getLogger(AppRestApplication.class);

	@Value("${user.timezone}")
	private String timeZone;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppRestApplication.class);
	}

	@PostConstruct
	public void init() { TimeZone.setDefault(TimeZone.getTimeZone(timeZone)); }

	public static void main(String[] args) {
		APP_LOGGER.info("running Application....");
		ConfigurableApplicationContext ctx = SpringApplication.run(AppRestApplication.class, args);
	}

	/**
	 *	http://localhost:8081/app/appRest/isAppRunning
	 */
	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
		return factory -> factory.setContextPath("/app");
	}

	@Bean
	public Clock clock() { return Clock.systemDefaultZone(); }
}
