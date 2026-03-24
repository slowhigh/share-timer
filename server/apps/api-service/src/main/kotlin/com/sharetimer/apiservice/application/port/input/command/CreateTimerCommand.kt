package com.sharetimer.apiservice.application.port.input.command

import java.time.Instant
import java.time.temporal.ChronoUnit

data class CreateTimerCommand(
    val targetTime: Instant,
) {
    init {
        require(targetTime.isAfter(Instant.now().plus(1, ChronoUnit.MINUTES))) {
            "'targetTime' must be at least 1 minute after current time."
        }
    }
}
