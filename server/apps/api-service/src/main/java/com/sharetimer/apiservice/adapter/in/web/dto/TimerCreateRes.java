package com.sharetimer.apiservice.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 타이머 생성 응답
 */
@Schema(name = "Timer Create Response", description = "타이머 생성 응답")
public record TimerCreateRes(

    /** 생성된 타이머 ID */
    @JsonProperty("timerId") @Schema(name = "timerId", description = "타이머 ID (UUID)",
        example = "c75862bc-0070-4e34-8a93-9380619bd310") String timerId,

    /** 타이머의 소유자 토큰 */
    @JsonProperty("ownerToken") @Schema(name = "ownerToken", description = "소유자 토큰 (UUID)",
        example = "3abfa8e7-bb9c-418a-af4b-0158cbf5baec") String ownerToken

) {
}
