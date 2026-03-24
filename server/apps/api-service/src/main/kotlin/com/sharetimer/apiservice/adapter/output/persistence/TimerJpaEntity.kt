package com.sharetimer.apiservice.adapter.output.persistence

import com.sharetimer.db.jpa.domain.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

/**
 * JPA entity for timers table
 */
@Entity
@Table(
    name = "timers",
    indexes = [Index(name = "idx_timers_id_owner_token", columnList = "id, owner_token")],
)
class TimerJpaEntity(
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(name = "id", length = 36)
    var id: UUID? = null,
    @Column(name = "target_time", nullable = false)
    var targetTime: Instant? = null,
    @Column(name = "owner_token", length = 36, unique = true, nullable = false)
    var ownerToken: UUID? = null,
    @OneToMany(
        mappedBy = "timer",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE],
    )
    @OrderBy("capturedAt ASC")
    var timestamps: MutableList<TimestampJpaEntity> = mutableListOf(),
) : BaseEntity() {
    override fun toString(): String = "TimerJpaEntity(id=$id, targetTime=$targetTime, ownerToken=$ownerToken)"
}
