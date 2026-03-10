package com.sharetimer.syncservice.adapter.out.web.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data format for 'targetTimeUpdate' event")
public record TargetTimeUpdateEvent(
    @Schema(description = "Updated timestamp", example = "2025-07-08T15:00:00Z") Instant updatedAt,

    @Schema(description = "Server time", example = "2025-07-08T15:00:00Z") Instant serverTime,

    @Schema(description = "New target time", example = "2025-07-08T15:00:00Z") Instant newTargetTime) {
}
