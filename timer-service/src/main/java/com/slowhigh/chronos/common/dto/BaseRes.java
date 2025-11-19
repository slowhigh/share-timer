package com.slowhigh.chronos.common.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 모든 응답 클래스의 상위 클래스
 */
@Getter
public class BaseRes<T> {

  /** 응답 상태 코드 */
  @JsonProperty("statusCode")
  @Schema(name = "statusCode", description = "상태 코드", example = "201")
  private String statusCode;

  /** 응답 상태 이름 */
  @JsonProperty("statusName")
  @Schema(name = "statusName", description = "상태 이름", example = "Success")
  private String statusName;

  /** 응답 데이터 */
  @JsonProperty("data")
  @Schema(name = "data", description = "응답 데이터", nullable = true)
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
