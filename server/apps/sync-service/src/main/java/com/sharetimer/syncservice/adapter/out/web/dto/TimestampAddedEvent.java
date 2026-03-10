package com.sharetimer.syncservice.adapter.out.web.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data format for 'timestampAdded' event")
public record TimestampAddedEvent(
    @Schema(description = "New target time", example = "2025-07-08T15:00:00Z") Instant targetTime,

    @Schema(description = "Captured time", example = "2025-07-08T15:00:00Z") Instant capturedAt) {
}
