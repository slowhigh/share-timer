package com.sharetimer.syncservice.application.port.input

import kotlinx.coroutines.flow.Flow
import org.springframework.http.codec.ServerSentEvent
import java.time.Instant

interface TimerUseCase {
    fun subscribe(timerId: String): Flow<ServerSentEvent<Any>>

    fun processTimerExpiration(timerId: String)

    fun updateTargetTime(
        timerId: String,
        updatedAt: Instant,
        serverTime: Instant,
        newTargetTime: Instant,
    )

    fun addTimestamp(
        timerId: String,
        targetTime: Instant,
        capturedAt: Instant,
    )
}
