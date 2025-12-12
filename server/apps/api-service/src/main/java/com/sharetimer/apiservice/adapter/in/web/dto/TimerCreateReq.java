package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 타이머 생성 요청
 */
@Schema(name = "Timer Create Request", description = "타이머 생성 요청")
public record TimerCreateReq(

    /** 타이머의 기준 시각(UTC) */
    @JsonProperty("targetTime") @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC") @NotNull(message = "'targetTime'은 필수 값입니다.") @Schema(name = "targetTime",
            description = "타이머의 기준 시각(UTC)", example = "2025-07-26T15:00:00Z") Instant targetTime

) {
}

