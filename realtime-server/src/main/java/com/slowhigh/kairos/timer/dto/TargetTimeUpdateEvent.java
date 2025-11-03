package com.slowhigh.kairos.timer.dto;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "'targetTimeUpdate' 이벤트의 데이터 형식")
public class TargetTimeUpdateEvent {

  @Schema(description = "갱신된 타임스탬프", example = "2025-07-08T15:00:00Z")
  private Instant updatedAt;

  @Schema(description = "서버 시간", example = "2025-07-08T15:00:00Z")
  private Instant serverTime;

  @Schema(description = "신규 기준 시간", example = "2025-07-08T15:00:00Z")
  private Instant newTargetTime;

}
