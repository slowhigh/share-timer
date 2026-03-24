package com.sharetimer.apiservice.adapter.input.web.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.sharetimer.apiservice.domain.model.Timestamp
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * Timestamp info response
 */
@Schema(name = "Timestamp Info Response", description = "Timestamp info response")
data class TimestampInfoRes(
    @field:JsonProperty("targetTime")
    @field:Schema(
        name = "targetTime",
        description = "Reference time (UTC) when adding the timestamp",
        example = "2025-07-26T15:00:00Z",
    )
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        timezone = "UTC",
    )
    val targetTime: Instant,
    @field:JsonProperty("capturedAt")
    @field:Schema(
        name = "capturedAt",
        description = "Time (UTC) when the timestamp was recorded",
        example = "2025-07-26T15:00:00Z",
    )
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        timezone = "UTC",
    )
    val capturedAt: Instant,
) {
    companion object {
        fun from(timestamp: Timestamp): TimestampInfoRes =
            TimestampInfoRes(
                targetTime = timestamp.targetTime,
                capturedAt = timestamp.capturedAt,
            )

        fun fromList(timestamps: List<Timestamp>): List<TimestampInfoRes> = timestamps.map { from(it) }
    }
}
