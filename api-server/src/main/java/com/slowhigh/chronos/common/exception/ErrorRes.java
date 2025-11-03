package com.slowhigh.chronos.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(name = "Error Response", description = "에러 응답")
public class ErrorRes {

  @JsonProperty("statusCode")
  @Schema(name = "statusCode", description = "상태 코드", example = "400")
  private final String statusCode;

  @JsonProperty("statusName")
  @Schema(name = "statusName", description = "상태 이름", example = "ArgumentNotValid")
  private final String statusName;

  @JsonProperty("message")
  @Schema(name = "message", description = "에러 메시지", example = "잘못된 요청입니다.")
  private final String message;
}
