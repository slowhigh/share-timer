package com.sharetimer.syncservice.adapter.out.web.dto;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data format for 'targetTimeUpdate' event")
public class TargetTimeUpdateEvent {

  @Schema(description = "Updated timestamp", example = "2025-07-08T15:00:00Z")
  private Instant updatedAt;

  @Schema(description = "Server time", example = "2025-07-08T15:00:00Z")
  private Instant serverTime;

  @Schema(description = "New target time", example = "2025-07-08T15:00:00Z")
  private Instant newTargetTime;

}
