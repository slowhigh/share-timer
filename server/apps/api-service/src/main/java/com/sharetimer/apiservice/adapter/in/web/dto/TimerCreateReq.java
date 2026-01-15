package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Timer creation request
 */
@Schema(name = "Timer Create Request", description = "Timer creation request")
public record TimerCreateReq(

    /** Timer's target time (UTC) */
    @JsonProperty("targetTime") @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC") @NotNull(message = "'targetTime' is required.") @Schema(
            name = "targetTime", description = "Timer's target time (UTC)",
            example = "2025-07-26T15:00:00Z") Instant targetTime

) {
}

