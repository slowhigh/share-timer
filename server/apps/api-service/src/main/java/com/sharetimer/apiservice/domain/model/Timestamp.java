package com.sharetimer.apiservice.domain.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

/**
 * Timestamp domain model
 */
@Getter
@Builder
public class Timestamp {

  private Long id;
  private Instant targetTime;
  private Instant capturedAt;
  private Instant createdAt;
  private Instant updatedAt;

}
