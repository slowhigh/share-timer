package com.sharetimer.apiservice.adapter.input.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.Instant

@Schema(name = "Timer Add Timestamp Request", description = "Request to add timestamp to timer")
data class TimerAddTimestampReq(
    /** Timestamp time (UTC) */
    @field:JsonProperty("capturedAt")
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC",
    )
    @field:NotNull(message = "'capturedAt' is required.")
    @field:Schema(
        name = "capturedAt",
        description = "Timestamp time (UTC)",
        example = "2025-07-26T15:00:00Z",
    )
    val capturedAt: Instant? = null,
)
