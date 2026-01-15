package com.sharetimer.apiservice.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Timer creation response
 */
@Schema(name = "Timer Create Response", description = "Timer creation response")
public record TimerCreateRes(

    /** Created Timer ID */
    @JsonProperty("timerId") @Schema(name = "timerId", description = "Timer ID (UUID)",
        example = "c75862bc-0070-4e34-8a93-9380619bd310") String timerId,

    /** Timer's owner token */
    @JsonProperty("ownerToken") @Schema(name = "ownerToken", description = "Owner Token (UUID)",
        example = "3abfa8e7-bb9c-418a-af4b-0158cbf5baec") String ownerToken

) {
}
