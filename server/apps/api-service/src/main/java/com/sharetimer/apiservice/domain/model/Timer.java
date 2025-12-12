package com.sharetimer.apiservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.sharetimer.core.common.domain.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 타이머 정보를 저장하는 엔티티 - Timer : Timestamp = 1 : N
 */
@Entity
@Table(name = "timers",
    indexes = {@Index(name = "idx_timers_id_owner_token", columnList = "id, owner_token")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(exclude = "timestamps")
public class Timer extends BaseTimeEntity {

  /** PK, UUID */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", length = 36)
  private UUID id;

  /** 타이머의 기준 시각(UTC) */
  @Column(name = "target_time", nullable = false)
  private Instant targetTime;

  /** 타이머의 소유자 토큰 */
  @Column(name = "owner_token", length = 36, unique = true, nullable = false)
  private UUID ownerToken;

  /** 타이머에 속한 타임스탬프 목록 */
  @Builder.Default
  @OneToMany(mappedBy = "timer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Timestamp> timestamps = new ArrayList<>();

  public void updateTargetTime(Instant targetTime) {
    this.targetTime = targetTime;
  }

}
