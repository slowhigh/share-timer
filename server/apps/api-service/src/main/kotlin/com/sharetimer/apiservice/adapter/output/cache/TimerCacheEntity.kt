package com.sharetimer.apiservice.adapter.output.cache

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.sharetimer.apiservice.domain.model.Timer
import java.time.Instant
import java.util.UUID

/** Cache entity for Timer domain */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
data class TimerCacheEntity(
    val id: UUID? = null,
    val targetTime: Instant? = null,
    val ownerToken: UUID? = null,
    val timestamps: List<TimestampCacheEntity> = emptyList(),
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
) {
    fun toDomain(): Timer =
        Timer(
            id = id,
            targetTime =
                checkNotNull(targetTime) {
                    "TimerCacheEntity.targetTime must not be null"
                },
            ownerToken = ownerToken,
            timestamps = timestamps.map { it.toDomain() }.toMutableList(),
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun from(timer: Timer): TimerCacheEntity =
            TimerCacheEntity(
                id = timer.id,
                targetTime = timer.targetTime,
                ownerToken = timer.ownerToken,
                timestamps = timer.timestamps.map { TimestampCacheEntity.from(it) },
                createdAt = timer.createdAt,
                updatedAt = timer.updatedAt,
            )
    }
}
