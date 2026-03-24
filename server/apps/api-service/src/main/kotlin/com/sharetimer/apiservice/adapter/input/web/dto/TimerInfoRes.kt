package com.sharetimer.apiservice.adapter.input.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.sharetimer.apiservice.domain.model.Timer
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * Timer info response
 */
@Schema(name = "Timer Info Response", description = "Timer info response")
data class TimerInfoRes(
    @field:JsonProperty("updatedAt")
    @field:Schema(
        name = "updatedAt",
        description = "Timer update time (UTC)",
        example = "2025-07-26T15:00:00Z",
    )
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        timezone = "UTC",
    )
    val updatedAt: Instant?,
    @field:JsonProperty("targetTime")
    @field:Schema(
        name = "targetTime",
        description = "Timer's target time (UTC)",
        example = "2025-07-26T15:00:00Z",
    )
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        timezone = "UTC",
    )
    val targetTime: Instant?,
    @field:JsonProperty("serverTime")
    @field:Schema(
        name = "serverTime",
        description = "Server time (UTC)",
        example = "2025-07-26T15:00:00Z",
    )
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        timezone = "UTC",
    )
    val serverTime: Instant?,
    @field:JsonProperty("timestamps")
    @field:Schema(name = "timestamps", description = "List of timer timestamps")
    val timestamps: List<TimestampInfoRes>,
    @field:JsonProperty("isOwner")
    @field:Schema(name = "isOwner", description = "Is Owner", example = "true")
    val isOwner: Boolean,
) {
    companion object {
        fun from(
            serverTime: Instant,
            isOwner: Boolean,
            timer: Timer,
        ): TimerInfoRes =
            TimerInfoRes(
                updatedAt = timer.updatedAt,
                targetTime = timer.targetTime,
                serverTime = serverTime,
                timestamps = TimestampInfoRes.fromList(timer.timestamps),
                isOwner = isOwner,
            )
    }
}
