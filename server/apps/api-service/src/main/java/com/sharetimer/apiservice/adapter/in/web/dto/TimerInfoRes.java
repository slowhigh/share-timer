package com.sharetimer.apiservice.adapter.in.web.dto;

import java.time.Instant;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Timer info response
 */
@Schema(name = "Timer Info Response", description = "Timer info response")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public record TimerInfoRes(

    @JsonProperty("updatedAt") @Schema(name = "updatedAt", description = "Timer update time (UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant updatedAt,

    @JsonProperty("targetTime") @Schema(name = "targetTime",
        description = "Timer's target time (UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant targetTime,

    @JsonProperty("serverTime") @Schema(name = "serverTime", description = "Server time (UTC)",
        example = "2025-07-26T15:00:00Z") @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", timezone = "UTC") Instant serverTime,

    @JsonProperty("timestamps") @Schema(name = "timestamps",
        description = "List of timer timestamps") List<TimestampInfoRes> timestamps,

    @JsonProperty("isOwner") @Schema(name = "isOwner", description = "Is Owner",
        example = "true") boolean isOwner

) {
}
