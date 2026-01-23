package com.sharetimer.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.sharetimer.common.config.InfoProps;
import com.sharetimer.storage.redis.config.RedisProps;
import com.sharetimer.storage.redis.config.TimerRedisProps;


@EnableJpaAuditing
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@EnableConfigurationProperties({InfoProps.class, RedisProps.class, TimerRedisProps.class})
@SpringBootApplication(scanBasePackages = "com.sharetimer")
// TODO: @EnableCaching를 적용 테스트, 현재는 안되고 있어서 CacheConfig Class를 사용하고 있음
// @EnableCaching
public class ApiServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiServiceApplication.class, args);
  }

}
