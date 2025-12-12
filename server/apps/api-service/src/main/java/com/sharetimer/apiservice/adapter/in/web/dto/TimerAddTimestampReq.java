package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Timer Add Timestamp Request", description = "타이머에 타임스탬프 추가 요청")
public record TimerAddTimestampReq(

    /** 타임스탬프 시각(UTC) */
    @JsonProperty("capturedAt") @JsonFormat(shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
        timezone = "UTC") @NotNull(message = "'capturedAt'은 필수 값입니다.") @Schema(name = "capturedAt",
            description = "타임스탬프 시각(UTC)", example = "2025-07-26T15:00:00Z") Instant capturedAt

) {
}
