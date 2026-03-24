package com.sharetimer.apiservice.domain.model

import java.time.Instant

/**
 * Timestamp domain model
 */
data class Timestamp(
    val id: Long? = null,
    val targetTime: Instant,
    val capturedAt: Instant,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)
