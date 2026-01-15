package com.sharetimer.apiservice.domain.model;

import java.time.Instant;
import com.sharetimer.db.jpa.domain.BaseEntity;
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
 * Entity storing timestamp information - Timestamp: Timer = N : 1
 */
@Entity
@Table(name = "timestamps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(exclude = "timer")
public class Timestamp extends BaseEntity {

  /** PK, Auto-increment */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Base time when timestamp is added (UTC) */
  @Column(name = "target_time", nullable = false)
  private Instant targetTime;

  /** Time when timestamp was recorded (UTC) */
  @Column(name = "captured_at", nullable = false)
  private Instant capturedAt;

  /** Timer that owns the timestamp */
  @ManyToOne()
  @JoinColumn(name = "timer_id", nullable = false)
  private Timer timer;

}
