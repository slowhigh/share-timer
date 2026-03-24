package com.sharetimer.apiservice.application.port.output

import com.sharetimer.apiservice.domain.model.Timer
import java.util.UUID

interface SaveTimerPort {
    fun saveTimer(timer: Timer): Timer

    fun deleteTimer(timerId: UUID)
}
