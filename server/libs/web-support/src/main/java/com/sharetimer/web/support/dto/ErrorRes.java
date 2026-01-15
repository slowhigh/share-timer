package com.sharetimer.web.support.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(name = "Error Response", description = "Error Response")
public class ErrorRes {

  @JsonProperty("statusCode")
  @Schema(name = "statusCode", description = "Status Code", example = "400")
  private final String statusCode;

  @JsonProperty("statusName")
  @Schema(name = "statusName", description = "Status Name", example = "ArgumentNotValid")
  private final String statusName;

  @JsonProperty("message")
  @Schema(name = "message", description = "Error Message", example = "Invalid Request.")
  private final String message;
}
