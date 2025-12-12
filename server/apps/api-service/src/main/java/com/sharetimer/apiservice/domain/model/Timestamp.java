package com.sharetimer.apiservice.domain.model;

import java.time.Instant;
import com.sharetimer.core.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 타임스탬프 정보를 저장하는 엔티티 - Timestamp: Timer = N : 1
 */
@Entity
@Table(name = "timestamps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(exclude = "timer")
public class Timestamp extends BaseTimeEntity {

  /** PK, 자동 증가 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 타임스탬프를 추가할 때 기준 시각(UTC) */
  @Column(name = "target_time", nullable = false)
  private Instant targetTime;

  /** 타임스탬프가 기록된 시각(UTC) */
  @Column(name = "captured_at", nullable = false)
  private Instant capturedAt;

  /** 타임스탬프가 속한 타이머 */
  @ManyToOne()
  @JoinColumn(name = "timer_id", nullable = false)
  private Timer timer;

}
