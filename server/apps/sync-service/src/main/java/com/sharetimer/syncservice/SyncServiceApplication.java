package com.sharetimer.syncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import com.sharetimer.core.common.config.RedisProps;
import com.sharetimer.core.common.config.TimerProps;
import com.sharetimer.core.common.config.WebClientProps;

@ConfigurationPropertiesScan
@EnableConfigurationProperties({TimerProps.class, RedisProps.class, WebClientProps.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "com.sharetimer",
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "com.sharetimer.web.GlobalExceptionHandler"))
public class SyncServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SyncServiceApplication.class, args);
  }

}
