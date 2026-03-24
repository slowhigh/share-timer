package com.sharetimer.apiservice.application.port.output

import java.time.Instant

interface TimerEventPort {
    fun scheduleExpiration(
        timerId: String,
        targetTime: Instant,
    )

    fun publishUpdateTimerTargetTime(
        timerId: String,
        updatedAt: Instant,
        targetTime: Instant,
    )

    fun publishAddTimestamp(
        timerId: String,
        targetTime: Instant,
        capturedAt: Instant,
    )
}
