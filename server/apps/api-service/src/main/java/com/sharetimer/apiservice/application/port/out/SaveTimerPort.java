package com.sharetimer.apiservice.application.port.out;

import java.util.UUID;
import com.sharetimer.apiservice.domain.model.Timer;

public interface SaveTimerPort {

  Timer saveTimer(Timer timer);

  void deleteTimer(UUID timerId);
}
