package com.sharetimer.syncservice.adapter.output.web

import com.sharetimer.syncservice.adapter.output.web.dto.TargetTimeUpdateEvent
import com.sharetimer.syncservice.adapter.output.web.dto.TimestampAddedEvent
import com.sharetimer.syncservice.application.port.output.TimerEventPublisher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class ConnectionManager : TimerEventPublisher {
    private val flows = ConcurrentHashMap<String, MutableSharedFlow<ServerSentEvent<Any>>>()

    override fun subscribe(timerId: String): Flow<ServerSentEvent<Any>> {
        val eventFlow =
            flows.computeIfAbsent(timerId) {
                log.debug("Flow created: {}", it)
                MutableSharedFlow(extraBufferCapacity = 64)
            }

        val heartbeatFlow =
            flow {
                while (true) {
                    emit(ServerSentEvent.builder<Any>().comment("heartbeat").build())
                    delay(30_000)
                }
            }

        return merge(eventFlow, heartbeatFlow)
    }

    override fun sendTimerEndEvent(timerId: String) {
        val flow =
            flows.remove(timerId) ?: run {
                log.warn("No target for TimerEnd event, timerId: {}", timerId)
                return
            }

        val sent =
            flow.tryEmit(
                ServerSentEvent
                    .builder<Any>()
                    .event("timerEnd")
                    .data(emptyMap<String, Any>())
                    .build(),
            )
        if (sent) {
            log.debug("TimerEnd event sent and Flow removed, timerId: {}", timerId)
        } else {
            log.warn("TimerEnd event dropped (buffer full), timerId: {}", timerId)
        }
    }

    override fun updateTargetTime(
        timerId: String,
        updatedAt: Instant,
        serverTime: Instant,
        newTargetTime: Instant,
    ) {
        emitEvent(timerId, "targetTimeUpdate", TargetTimeUpdateEvent(updatedAt, serverTime, newTargetTime))
    }

    override fun addTimestamp(
        timerId: String,
        targetTime: Instant,
        capturedAt: Instant,
    ) {
        emitEvent(timerId, "timestampAdd", TimestampAddedEvent(targetTime, capturedAt))
    }

    private fun emitEvent(
        timerId: String,
        eventName: String,
        data: Any,
    ) {
        val flow =
            flows[timerId] ?: run {
                log.warn("No target for {} event, timerId: {}", eventName, timerId)
                return
            }

        val sent =
            flow.tryEmit(
                ServerSentEvent
                    .builder<Any>()
                    .event(eventName)
                    .data(data)
                    .build(),
            )
        if (sent) {
            log.debug("{} event sent successfully, timerId: {}", eventName, timerId)
        } else {
            log.warn("{} event dropped (buffer full), timerId: {}", eventName, timerId)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ConnectionManager::class.java)
    }
}
