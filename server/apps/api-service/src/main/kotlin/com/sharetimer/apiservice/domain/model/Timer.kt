package com.sharetimer.apiservice.domain.model

import java.time.Instant
import java.util.UUID

/**
 * Timer domain model
 */
class Timer(
    val id: UUID? = null,
    var targetTime: Instant,
    val ownerToken: UUID? = null,
    val timestamps: MutableList<Timestamp> = mutableListOf(),
    val createdAt: Instant? = null,
    var updatedAt: Instant? = null,
) {
    fun updateTargetTime(
        newTargetTime: Instant,
        requestTime: Instant,
    ) {
        require(requestTime.isAfter(updatedAt ?: Instant.MIN)) { "Stale update request" }
        targetTime = newTargetTime
        updatedAt = requestTime
    }

    fun addTimestamp(capturedAt: Instant) {
        require(!capturedAt.isAfter(targetTime)) {
            "Timestamp must be before target time. capturedAt=$capturedAt"
        }
        timestamps.add(Timestamp(targetTime = targetTime, capturedAt = capturedAt))
    }

    override fun toString(): String =
        "Timer(id=$id, targetTime=$targetTime, ownerToken=$ownerToken, timestamps=${timestamps.size})"
}
