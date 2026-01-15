package com.sharetimer.syncservice.adapter.out.web.dto;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data format for 'timestampAdded' event")
public class TimestampAddedEvent {

  @Schema(description = "New target time", example = "2025-07-08T15:00:00Z")
  private Instant targetTime;

  @Schema(description = "Captured time", example = "2025-07-08T15:00:00Z")
  private Instant capturedAt;

}
