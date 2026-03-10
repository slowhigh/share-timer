package com.sharetimer.apiservice.adapter.out.persistence;

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
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * JPA entity for timers table
 */
@Entity
@Table(name = "timers", indexes = { @Index(name = "idx_timers_id_owner_token", columnList = "id, owner_token") })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString(exclude = "timestamps")
public class TimerJpaEntity extends BaseEntity {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.TIME)
  @Column(name = "id", length = 36)
  private UUID id;

  @Column(name = "target_time", nullable = false)
  private Instant targetTime;

  @Column(name = "owner_token", length = 36, unique = true, nullable = false)
  private UUID ownerToken;

  @Builder.Default
  @OneToMany(mappedBy = "timer", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
  @OrderBy("capturedAt ASC")
  private List<TimestampJpaEntity> timestamps = new ArrayList<>();

}
