package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 타임스탬프 조회 응답
 */
@Schema(name = "Timestamp Info Response", description = "타임스탬프 조회 응답")
public record TimestampInfoRes(

    @JsonProperty("targetTime") @Schema(name = "targetTime",
        description = "타임스탬프를 추가할 때 기준 시각(UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant targetTime,

    @JsonProperty("capturedAt") @Schema(name = "capturedAt", description = "타임스탬프가 기록된 시각(UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant capturedAt

) {
}
