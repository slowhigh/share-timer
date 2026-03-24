package com.sharetimer.common.message

import java.time.Instant

data class TimerUpdateTargetTimeMessage(
    val timerId: String,
    val type: String,
    val payload: Payload,
) {
    data class Payload(
        val updatedAt: Instant,
        val serverTime: Instant,
        val newTargetTime: Instant,
    )
}
