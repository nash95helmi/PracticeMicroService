package com.practice.appusermanagement;

import com.practice.modulebase.constant.MsConstant;
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
import java.util.TimeZone;

@SpringBootApplication
@ComponentScan({"com.practice.appusermanagement",
				"com.practice.moduleusermanagement",
				"com.practice.modulebase"})
public class AppUsermanagementApplication extends SpringBootServletInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUsermanagementApplication.class);

	@Value("${user.timezone}")
	private String timeZone;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppUsermanagementApplication.class);
	}

	@PostConstruct
	public void init() { TimeZone.setDefault(TimeZone.getTimeZone(timeZone)); }

	public static void main(String[] args) {
		LOGGER.info("running Application....");
		ConfigurableApplicationContext ctx = SpringApplication.run(AppUsermanagementApplication.class, args);
	}

	@Bean
	public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
		return factory -> factory.setContextPath(MsConstant.appContextPathUserMgmgt);
	}

}
