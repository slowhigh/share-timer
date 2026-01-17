package com.sharetimer.apiservice.application.port.out;

import java.util.Optional;
import java.util.UUID;
import com.sharetimer.apiservice.domain.model.Timer;

public interface LoadTimerPort {

  Optional<Timer> loadTimer(UUID timerId);
}
