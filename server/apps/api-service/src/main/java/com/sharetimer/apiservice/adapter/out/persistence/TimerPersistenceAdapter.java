package com.sharetimer.apiservice.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.sharetimer.apiservice.application.port.out.LoadTimerPort;
import com.sharetimer.apiservice.application.port.out.SaveTimerPort;
import com.sharetimer.apiservice.domain.model.Timer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TimerPersistenceAdapter implements LoadTimerPort, SaveTimerPort {

  private final TimerRepository timerRepository;
  private final TimerMapper timerMapper;

  @Override
  @Cacheable(value = "timers", key = "#timerId", unless = "#result == null")
  public Optional<Timer> loadTimer(UUID timerId) {
    return timerRepository.findWithTimestampsById(timerId)
        .map(timerMapper::mapToDomainEntity);
  }

  @Override
  @CacheEvict(value = "timers", key = "#timer.id", condition = "#timer.id != null")
  public Timer saveTimer(Timer timer) {
    TimerJpaEntity jpaEntity = timerMapper.mapToJpaEntity(timer);
    TimerJpaEntity savedEntity = timerRepository.saveAndFlush(jpaEntity);
    return timerMapper.mapToDomainEntity(savedEntity);
  }

  @Override
  @CacheEvict(value = "timers", key = "#timerId")
  public void deleteTimer(UUID timerId) {
    timerRepository.deleteById(timerId);
  }
}
