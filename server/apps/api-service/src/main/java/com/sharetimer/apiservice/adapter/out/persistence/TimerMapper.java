package com.sharetimer.apiservice.adapter.out.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sharetimer.apiservice.domain.model.Timer;
import com.sharetimer.apiservice.domain.model.Timestamp;

@Component
public class TimerMapper {

  public Timer mapToDomainEntity(TimerJpaEntity timerJpaEntity) {
    if (timerJpaEntity == null) {
      return null;
    }

    return Timer.builder()
        .id(timerJpaEntity.getId())
        .targetTime(timerJpaEntity.getTargetTime())
        .ownerToken(timerJpaEntity.getOwnerToken())
        .timestamps(mapToDomainEntity(timerJpaEntity.getTimestamps()))
        .createdAt(timerJpaEntity.getCreatedAt())
        .updatedAt(timerJpaEntity.getUpdatedAt())
        .build();
  }

  public Timestamp mapToDomainEntity(TimestampJpaEntity timestampJpaEntity) {
    if (timestampJpaEntity == null) {
      return null;
    }
    return Timestamp.builder()
        .id(timestampJpaEntity.getId())
        .targetTime(timestampJpaEntity.getTargetTime())
        .capturedAt(timestampJpaEntity.getCapturedAt())
        .createdAt(timestampJpaEntity.getCreatedAt())
        .updatedAt(timestampJpaEntity.getUpdatedAt())
        .build();
  }

  public List<Timestamp> mapToDomainEntity(List<TimestampJpaEntity> timestamps) {
    if (timestamps == null) {
      return List.of();
    }
    return timestamps.stream()
        .map(this::mapToDomainEntity)
        .collect(Collectors.toList());
  }

  public TimerJpaEntity mapToJpaEntity(Timer timer) {
    if (timer == null) {
      return null;
    }

    TimerJpaEntity timerJpaEntity = TimerJpaEntity.builder()
        .id(timer.getId())
        .targetTime(timer.getTargetTime())
        .ownerToken(timer.getOwnerToken())
        .build();

    if (timer.getTimestamps() != null) {
      List<TimestampJpaEntity> timestampEntities = timer.getTimestamps().stream()
          .map(t -> mapToJpaEntity(t, timerJpaEntity))
          .collect(Collectors.toList());
      timerJpaEntity.getTimestamps().addAll(timestampEntities);
    }

    if (timer.getUpdatedAt() != null) {
      timerJpaEntity.updateUpdatedAt(timer.getUpdatedAt());
    }

    return timerJpaEntity;
  }

  public TimestampJpaEntity mapToJpaEntity(Timestamp timestamp, TimerJpaEntity timerJpaEntity) {
    if (timestamp == null) {
      return null;
    }

    TimestampJpaEntity entity = TimestampJpaEntity.builder()
        .id(timestamp.getId())
        .targetTime(timestamp.getTargetTime())
        .capturedAt(timestamp.getCapturedAt())
        .timer(timerJpaEntity)
        .build();

    if (timestamp.getUpdatedAt() != null) {
      entity.updateUpdatedAt(timestamp.getUpdatedAt());
    }
    return entity;
  }
}
