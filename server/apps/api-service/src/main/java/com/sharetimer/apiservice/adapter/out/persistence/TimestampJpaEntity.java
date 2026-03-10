package com.sharetimer.apiservice.adapter.out.persistence;

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
 * JPA entity for timestamps table
 */
@Entity
@Table(name = "timestamps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(exclude = "timer")
public class TimestampJpaEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "target_time", nullable = false)
  private Instant targetTime;

  @Column(name = "captured_at", nullable = false)
  private Instant capturedAt;

  @ManyToOne()
  @JoinColumn(name = "timer_id", nullable = false)
  private TimerJpaEntity timer;

}
