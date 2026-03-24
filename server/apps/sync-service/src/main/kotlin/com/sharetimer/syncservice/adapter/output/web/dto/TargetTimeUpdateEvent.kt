package com.sharetimer.syncservice.adapter.output.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Data format for 'targetTimeUpdate' event")
data class TargetTimeUpdateEvent(
    @field:Schema(description = "Updated timestamp", example = "2025-07-08T15:00:00Z")
    val updatedAt: Instant,
    @field:Schema(description = "Server time", example = "2025-07-08T15:00:00Z")
    val serverTime: Instant,
    @field:Schema(description = "New target time", example = "2025-07-08T15:00:00Z")
    val newTargetTime: Instant,
)
