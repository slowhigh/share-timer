package com.sharetimer.syncservice.adapter.output.web.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Data format for 'timestampAdded' event")
data class TimestampAddedEvent(
    @field:Schema(description = "New target time", example = "2025-07-08T15:00:00Z")
    val targetTime: Instant,
    @field:Schema(description = "Captured time", example = "2025-07-08T15:00:00Z")
    val capturedAt: Instant,
)
