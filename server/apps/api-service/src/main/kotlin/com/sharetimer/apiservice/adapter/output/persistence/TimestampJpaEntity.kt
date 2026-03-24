package com.sharetimer.apiservice.adapter.output.persistence

import com.sharetimer.db.jpa.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

/**
 * JPA entity for timestamps table
 */
@Entity
@Table(name = "timestamps")
class TimestampJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "target_time", nullable = false)
    var targetTime: Instant? = null,
    @Column(name = "captured_at", nullable = false)
    var capturedAt: Instant? = null,
    @ManyToOne
    @JoinColumn(name = "timer_id", nullable = false)
    var timer: TimerJpaEntity? = null,
) : BaseEntity() {
    override fun toString(): String = "TimestampJpaEntity(id=$id, targetTime=$targetTime, capturedAt=$capturedAt)"
}
