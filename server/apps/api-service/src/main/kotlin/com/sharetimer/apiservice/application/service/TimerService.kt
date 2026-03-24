package com.sharetimer.apiservice.application.service

import com.sharetimer.apiservice.application.port.input.TimerUseCase
import com.sharetimer.apiservice.application.port.input.command.AddTimestampCommand
import com.sharetimer.apiservice.application.port.input.command.CreateTimerCommand
import com.sharetimer.apiservice.application.port.input.command.DeleteTimerCommand
import com.sharetimer.apiservice.application.port.input.command.GetTimerQuery
import com.sharetimer.apiservice.application.port.input.command.UpdateTimerCommand
import com.sharetimer.apiservice.application.port.output.LoadTimerPort
import com.sharetimer.apiservice.application.port.output.SaveTimerPort
import com.sharetimer.apiservice.application.port.output.TimerEventPort
import com.sharetimer.apiservice.domain.model.Timer
import com.sharetimer.web.support.exception.ForbiddenException
import com.sharetimer.web.support.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class TimerService(
    private val loadTimerPort: LoadTimerPort,
    private val saveTimerPort: SaveTimerPort,
    private val timerEventPort: TimerEventPort,
) : TimerUseCase {
    companion object {
        private val log = LoggerFactory.getLogger(TimerService::class.java)
    }

    @Transactional
    override fun createTimer(cmd: CreateTimerCommand): Timer {
        val newTimer =
            Timer(
                targetTime = cmd.targetTime,
                ownerToken = UUID.randomUUID(),
            )

        val savedTimer = saveTimerPort.saveTimer(newTimer)
        log.debug("New timer created: timerId={}, ownerToken={}", savedTimer.id, savedTimer.ownerToken)

        timerEventPort.scheduleExpiration(savedTimer.id.toString(), savedTimer.targetTime)

        return savedTimer
    }

    @Transactional(readOnly = true)
    override fun getTimerInfo(query: GetTimerQuery): Timer = findTimer(query.timerId)

    @Transactional
    override fun deleteTimer(cmd: DeleteTimerCommand) {
        saveTimerPort.deleteTimer(UUID.fromString(cmd.timerId))
    }

    @Transactional
    override fun updateTimer(cmd: UpdateTimerCommand) {
        val timer = findTimer(cmd.timerId)

        if (!isOwner(cmd.ownerToken, timer)) {
            throw ForbiddenException("OwnerTokenMismatch", "No permission to update timer.")
        }

        runCatching { timer.updateTargetTime(cmd.targetTime, cmd.requestTime) }
            .onFailure {
                log.warn(
                    "Old timer update request. timerId={}, requestTime={}, updatedAt={}",
                    cmd.timerId,
                    cmd.requestTime,
                    timer.updatedAt,
                )
                return
            }

        saveTimerPort.saveTimer(timer)

        timerEventPort.scheduleExpiration(timer.id.toString(), timer.targetTime)
        timerEventPort.publishUpdateTimerTargetTime(
            timer.id.toString(),
            cmd.requestTime,
            timer.targetTime,
        )
    }

    @Transactional
    override fun addTimestamp(cmd: AddTimestampCommand) {
        val timer = findTimer(cmd.timerId)

        timer.addTimestamp(cmd.capturedAt)
        saveTimerPort.saveTimer(timer)

        timerEventPort.publishAddTimestamp(
            timer.id.toString(),
            timer.targetTime,
            cmd.capturedAt,
        )
    }

    private fun findTimer(timerId: String): Timer =
        loadTimerPort.loadTimer(UUID.fromString(timerId))
            ?: throw NotFoundException("TimerNotFound", "Timer not found. timerId=$timerId")

    private fun isOwner(
        ownerToken: String?,
        timer: Timer,
    ): Boolean = ownerToken != null && UUID.fromString(ownerToken) == timer.ownerToken
}
