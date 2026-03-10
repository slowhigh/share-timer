package com.sharetimer.apiservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

/**
 * Timer domain model
 */
@Getter
@Builder
public class Timer {

  private UUID id;
  private Instant targetTime;
  private UUID ownerToken;
  @Builder.Default
  private List<Timestamp> timestamps = new ArrayList<>();
  private Instant createdAt;
  private Instant updatedAt;

  public void updateTargetTime(Instant targetTime) {
    this.targetTime = targetTime;
  }

  public void updateUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

}
