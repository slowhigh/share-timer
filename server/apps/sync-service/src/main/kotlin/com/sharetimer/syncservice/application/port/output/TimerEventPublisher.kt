package com.sharetimer.syncservice.application.port.output

import kotlinx.coroutines.flow.Flow
import org.springframework.http.codec.ServerSentEvent
import java.time.Instant

interface TimerEventPublisher {
    fun subscribe(timerId: String): Flow<ServerSentEvent<Any>>

    fun sendTimerEndEvent(timerId: String)

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
