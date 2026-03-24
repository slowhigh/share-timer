package com.sharetimer.common.message

import java.time.Instant

data class TimerAddTimestampMessage(
    val timerId: String,
    val type: String,
    val payload: Payload,
) {
    data class Payload(
        val targetTime: Instant,
        val capturedAt: Instant,
    )
}
