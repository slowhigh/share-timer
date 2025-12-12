package com.sharetimer.apiservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "eureka.client.enabled=false")
class ApiServiceApplicationTests {

  @Test
  void contextLoads() {}

}
