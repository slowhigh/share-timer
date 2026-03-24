package com.sharetimer.apiservice.adapter.input.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class TimerUpdateReq(
    /** Request time (UTC) */
    @field:JsonProperty("requestTime")
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC",
    )
    @field:NotNull(message = "'requestTime' is required.")
    @field:Schema(
        name = "requestTime",
        description = "Request time (UTC)",
        example = "2025-07-26T15:00:00Z",
    )
    val requestTime: Instant? = null,
    /** Timer's target time (UTC) */
    @field:JsonProperty("targetTime")
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC",
    )
    @field:NotNull(message = "'targetTime' is required.")
    @field:Schema(
        name = "targetTime",
        description = "Timer's target time (UTC)",
        example = "2025-07-26T15:00:00Z",
    )
    val targetTime: Instant? = null,
)
