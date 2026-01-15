package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Timer Add Timestamp Request", description = "Request to add timestamp to timer")
public record TimerAddTimestampReq(

    /** Timestamp time (UTC) */
    @JsonProperty("capturedAt") @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC") @NotNull(message = "'capturedAt' is required.") @Schema(
            name = "capturedAt", description = "Timestamp time (UTC)",
            example = "2025-07-26T15:00:00Z") Instant capturedAt

) {
}
