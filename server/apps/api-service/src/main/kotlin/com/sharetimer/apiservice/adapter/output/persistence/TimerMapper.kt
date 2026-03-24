package com.sharetimer.apiservice.adapter.output.persistence

import com.sharetimer.apiservice.domain.model.Timer
import com.sharetimer.apiservice.domain.model.Timestamp

object TimerMapper {
    fun TimerJpaEntity.toDomain(): Timer =
        Timer(
            id = id,
            targetTime = checkNotNull(targetTime) { "TimerJpaEntity.targetTime must not be null" },
            ownerToken = ownerToken,
            timestamps = timestamps.mapNotNull { it.toDomain() }.toMutableList(),
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    fun TimestampJpaEntity.toDomain(): Timestamp =
        Timestamp(
            id = id,
            targetTime = checkNotNull(targetTime) { "TimestampJpaEntity.targetTime must not be null" },
            capturedAt = checkNotNull(capturedAt) { "TimestampJpaEntity.capturedAt must not be null" },
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    fun Timer.toJpaEntity(): TimerJpaEntity {
        val timerJpaEntity =
            TimerJpaEntity(
                id = id,
                targetTime = targetTime,
                ownerToken = ownerToken,
            )

        timerJpaEntity.timestamps += timestamps.map { it.toJpaEntity(timerJpaEntity) }
        updatedAt?.let { timerJpaEntity.updateUpdatedAt(it) }

        return timerJpaEntity
    }

    fun Timestamp.toJpaEntity(timerJpaEntity: TimerJpaEntity): TimestampJpaEntity =
        TimestampJpaEntity(
            id = id,
            targetTime = targetTime,
            capturedAt = capturedAt,
            timer = timerJpaEntity,
        ).also { entity ->
            updatedAt?.let { entity.updateUpdatedAt(it) }
        }
}
