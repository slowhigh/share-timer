package com.sharetimer.apiservice.application.port.input.command

import java.time.Instant

data class AddTimestampCommand(
    val timerId: String,
    val capturedAt: Instant,
)
