package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 타이머 조회 응답
 */
@Schema(name = "Timer Info Response", description = "타이머 조회 응답")
public record TimerInfoRes(

    @JsonProperty("updatedAt") @Schema(name = "updatedAt", description = "타이머 갱신 시각(UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant updatedAt,

    @JsonProperty("targetTime") @Schema(name = "targetTime", description = "타이머의 기준 시각(UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant targetTime,

    @JsonProperty("serverTime") @Schema(name = "serverTime", description = "서버 시각(UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant serverTime,

    @JsonProperty("timestamps") @Schema(name = "timestamps",
        description = "타이머 시간 목록") List<TimestampInfoRes> timestamps,

    @JsonProperty("isOwner") @Schema(name = "isOwner", description = "소유자 여부",
        example = "true") boolean isOwner

) {
}
