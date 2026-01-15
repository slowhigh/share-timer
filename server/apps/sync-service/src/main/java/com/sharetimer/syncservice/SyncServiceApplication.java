package com.sharetimer.syncservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.sharetimer.storage.redis.config.RedisProps;
import com.sharetimer.storage.redis.config.TimerRedisProps;

@ConfigurationPropertiesScan
@EnableConfigurationProperties({RedisProps.class, TimerRedisProps.class})
@SpringBootApplication(scanBasePackages = "com.sharetimer")
public class SyncServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SyncServiceApplication.class, args);
  }

}
