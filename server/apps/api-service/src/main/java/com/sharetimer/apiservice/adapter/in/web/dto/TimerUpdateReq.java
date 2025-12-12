package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TimerUpdateReq(

    /** 요청 시각(UTC) */
    @JsonProperty("requestTime") @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC") @NotNull(
            message = "'requestTime'은 필수 값입니다.") @Schema(name = "requestTime",
                description = "요청 시각(UTC)", example = "2025-07-26T15:00:00Z") Instant requestTime,

    /** 타이머의 기준 시각(UTC) */
    @JsonProperty("targetTime") @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC") @NotNull(message = "'targetTime'은 필수 값입니다.") @Schema(name = "targetTime",
            description = "타이머의 기준 시각(UTC)", example = "2025-07-26T15:00:00Z") Instant targetTime

) {
}
