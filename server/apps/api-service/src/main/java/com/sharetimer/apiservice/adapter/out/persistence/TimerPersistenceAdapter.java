package com.sharetimer.apiservice.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import com.sharetimer.apiservice.application.port.out.LoadTimerPort;
import com.sharetimer.apiservice.application.port.out.SaveTimerPort;
import com.sharetimer.apiservice.domain.model.Timer;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TimerPersistenceAdapter implements LoadTimerPort, SaveTimerPort {

  private final TimerRepository timerRepository;

  @Override
  public Optional<Timer> loadTimer(UUID timerId) {
    return timerRepository.findWithTimestampsById(timerId);
  }

  @Override
  public Timer saveTimer(Timer timer) {
    return timerRepository.saveAndFlush(timer);
  }

  @Override
  public void deleteTimer(UUID timerId) {
    timerRepository.deleteById(timerId);
  }
}
