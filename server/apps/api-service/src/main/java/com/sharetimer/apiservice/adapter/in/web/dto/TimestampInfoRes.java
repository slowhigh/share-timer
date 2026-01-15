package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Timestamp info response
 */
@Schema(name = "Timestamp Info Response", description = "Timestamp info response")
public record TimestampInfoRes(

    @JsonProperty("targetTime") @Schema(name = "targetTime",
        description = "Reference time (UTC) when adding the timestamp",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant targetTime,

    @JsonProperty("capturedAt") @Schema(name = "capturedAt",
        description = "Time (UTC) when the timestamp was recorded",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant capturedAt

) {
}
