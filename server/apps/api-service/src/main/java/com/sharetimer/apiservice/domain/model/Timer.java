package com.sharetimer.apiservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import com.sharetimer.db.jpa.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Entity storing timer information - Timer : Timestamp = 1 : N
 */
@Entity
@Table(name = "timers",
    indexes = {@Index(name = "idx_timers_id_owner_token", columnList = "id, owner_token")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(exclude = "timestamps")
public class Timer extends BaseEntity {

  /** PK, UUID */
  @Id
  @UuidGenerator(style = UuidGenerator.Style.TIME)
  @Column(name = "id", length = 36)
  private UUID id;

  /** Timer's target time (UTC) */
  @Column(name = "target_time", nullable = false)
  private Instant targetTime;

  /** Timer's owner token */
  @Column(name = "owner_token", length = 36, unique = true, nullable = false)
  private UUID ownerToken;

  /** List of timestamps belonging to the timer */
  @Builder.Default
  @OneToMany(mappedBy = "timer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Timestamp> timestamps = new ArrayList<>();

  public void updateTargetTime(Instant targetTime) {
    this.targetTime = targetTime;
  }

}
