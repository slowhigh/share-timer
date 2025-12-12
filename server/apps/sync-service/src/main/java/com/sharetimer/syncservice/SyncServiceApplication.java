package com.sharetimer.syncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.sharetimer.core.common.config.RedisProps;
import com.sharetimer.core.common.config.TimerProps;

@EnableFeignClients
@ConfigurationPropertiesScan
@EnableConfigurationProperties({TimerProps.class, RedisProps.class})
@SpringBootApplication(scanBasePackages = "com.sharetimer",
    exclude = {DataSourceAutoConfiguration.class})
public class SyncServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SyncServiceApplication.class, args);
  }

}
