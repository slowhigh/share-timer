package com.sharetimer.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.sharetimer.core.common.config.RedisProps;
import com.sharetimer.core.common.config.TimerProps;

@EnableJpaAuditing
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@EnableConfigurationProperties({TimerProps.class, RedisProps.class})
@SpringBootApplication(scanBasePackages = "com.sharetimer")
public class ApiServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiServiceApplication.class, args);
  }

}
