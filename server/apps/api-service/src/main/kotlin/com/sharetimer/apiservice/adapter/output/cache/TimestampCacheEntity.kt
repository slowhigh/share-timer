package com.sharetimer.apiservice.adapter.output.cache

import com.sharetimer.apiservice.domain.model.Timestamp
import java.time.Instant

/** Cache entity for Timestamp domain */
data class TimestampCacheEntity(
    val id: Long? = null,
    val targetTime: Instant? = null,
    val capturedAt: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
) {
    fun toDomain(): Timestamp =
        Timestamp(
            id = id,
            targetTime = checkNotNull(targetTime) { "TimestampCacheEntity.targetTime must not be null" },
            capturedAt = checkNotNull(capturedAt) { "TimestampCacheEntity.capturedAt must not be null" },
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun from(timestamp: Timestamp): TimestampCacheEntity =
            TimestampCacheEntity(
                id = timestamp.id,
                targetTime = timestamp.targetTime,
                capturedAt = timestamp.capturedAt,
                createdAt = timestamp.createdAt,
                updatedAt = timestamp.updatedAt,
            )
    }
}
