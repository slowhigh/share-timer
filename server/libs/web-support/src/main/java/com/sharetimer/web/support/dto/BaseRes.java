package com.sharetimer.web.support.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Base class for all response classes
 */
@Getter
public class BaseRes<T> {

  /** Response status code */
  @JsonProperty("statusCode")
  @Schema(name = "statusCode", description = "Status Code", example = "201")
  private String statusCode;

  /** Response status name */
  @JsonProperty("statusName")
  @Schema(name = "statusName", description = "Status Name", example = "Success")
  private String statusName;

  /** Response data */
  @JsonProperty("data")
  @Schema(name = "data", description = "Response Data", nullable = true)
  private final T data;

  private BaseRes(HttpStatus status, T data) {
    this.statusCode = String.valueOf(status.value());
    this.statusName = status.name();
    this.data = data;
  }

  public static <T> ResponseEntity<BaseRes<T>> of(HttpStatus status, T data) {
    return ResponseEntity.status(status).body(new BaseRes<>(status, data));
  }
}
