package com.sharetimer.apiservice.application.port.input.command

import com.sharetimer.web.support.exception.BadRequestException
import java.time.Instant
import java.time.temporal.ChronoUnit

data class UpdateTimerCommand(
    val timerId: String,
    val ownerToken: String,
    val requestTime: Instant,
    val targetTime: Instant,
) {
    init {
        if (targetTime.isBefore(Instant.now().plus(1, ChronoUnit.MINUTES))) {
            throw BadRequestException(
                "InvalidTargetTime",
                "'targetTime' must be at least 1 minute after current time.",
            )
        }
    }
}
