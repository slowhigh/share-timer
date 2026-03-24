package com.sharetimer.apiservice.application.port.output

import com.sharetimer.apiservice.domain.model.Timer
import java.util.UUID

interface LoadTimerPort {
    fun loadTimer(timerId: UUID): Timer?
}
