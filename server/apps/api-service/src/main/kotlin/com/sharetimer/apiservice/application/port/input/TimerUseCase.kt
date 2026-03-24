package com.sharetimer.apiservice.application.port.input

import com.sharetimer.apiservice.application.port.input.command.AddTimestampCommand
import com.sharetimer.apiservice.application.port.input.command.CreateTimerCommand
import com.sharetimer.apiservice.application.port.input.command.DeleteTimerCommand
import com.sharetimer.apiservice.application.port.input.command.GetTimerQuery
import com.sharetimer.apiservice.application.port.input.command.UpdateTimerCommand
import com.sharetimer.apiservice.domain.model.Timer

interface TimerUseCase {
    fun createTimer(cmd: CreateTimerCommand): Timer

    fun getTimerInfo(query: GetTimerQuery): Timer

    fun deleteTimer(cmd: DeleteTimerCommand)

    fun updateTimer(cmd: UpdateTimerCommand)

    fun addTimestamp(cmd: AddTimestampCommand)
}
