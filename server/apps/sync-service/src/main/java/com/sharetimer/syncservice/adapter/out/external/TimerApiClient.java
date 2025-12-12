package com.sharetimer.syncservice.adapter.out.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "timer-api-client", url = "${feign.client.timer-api.url}")
public interface TimerApiClient {

  @DeleteMapping("/timers/{timerId}")
  void deleteTimer(@PathVariable("timerId") String timerId);

}
