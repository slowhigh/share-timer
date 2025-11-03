package com.slowhigh.kairos.timer.handler;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slowhigh.kairos.timer.handler.message.TimerAddTimestampMessage;
import com.slowhigh.kairos.timer.service.TimerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerTimestampAddListener implements MessageListener {

  private final TimerService timerService;
  private final ObjectMapper objectMapper;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String body = new String(message.getBody());

    TimerAddTimestampMessage timerAddTimestampMessage = null;

    try {
      timerAddTimestampMessage = objectMapper.readValue(body, TimerAddTimestampMessage.class);
    } catch (Exception e) {
      log.error("메시지 파싱 실패 body: {}", body);
      return;
    }

    timerService.addTimestamp(timerAddTimestampMessage.timerId(),
        timerAddTimestampMessage.payload().targetTime(),
        timerAddTimestampMessage.payload().capturedAt());
  }
}
