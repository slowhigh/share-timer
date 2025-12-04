package com.sharetimer.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
public class ApiServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiServiceApplication.class, args);
  }

}
