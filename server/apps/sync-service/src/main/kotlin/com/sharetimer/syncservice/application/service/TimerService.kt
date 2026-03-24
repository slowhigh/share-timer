package com.sharetimer.syncservice.application.service

import com.sharetimer.storage.redis.config.ReactiveRedisTemplateFactory
import com.sharetimer.storage.redis.config.TimerRedisProps
import com.sharetimer.syncservice.adapter.output.external.TimerApiClient
import com.sharetimer.syncservice.application.port.input.TimerUseCase
import com.sharetimer.syncservice.application.port.output.TimerEventPublisher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TimerService(
    private val timerEventPublisher: TimerEventPublisher,
    private val redisTemplateFactory: ReactiveRedisTemplateFactory,
    private val timerRedisProps: TimerRedisProps,
    private val timerApiClient: TimerApiClient,
) : TimerUseCase {
    override fun subscribe(timerId: String): Flow<ServerSentEvent<Any>> {
        val key = timerRedisProps.expiration.keyPrefix + timerId
        return flow {
            val hasKey = redisTemplateFactory.template.hasKey(key).awaitSingle()
            if (!hasKey) {
                emit(ServerSentEvent.builder<Any>().event("timerNotFound").build())
            } else {
                emitAll(timerEventPublisher.subscribe(timerId))
            }
        }
    }

    override fun processTimerExpiration(timerId: String) {
        timerEventPublisher.sendTimerEndEvent(timerId)
        timerApiClient.deleteTimer(timerId).subscribe(
            { log.debug("Timer delete request success: {}", timerId) },
            { error -> log.error("Timer delete request failed: {}", timerId, error) },
        )
    }

    override fun updateTargetTime(
        timerId: String,
        updatedAt: Instant,
        serverTime: Instant,
        newTargetTime: Instant,
    ) {
        timerEventPublisher.updateTargetTime(timerId, updatedAt, serverTime, newTargetTime)
    }

    override fun addTimestamp(
        timerId: String,
        targetTime: Instant,
        capturedAt: Instant,
    ) {
        timerEventPublisher.addTimestamp(timerId, targetTime, capturedAt)
    }

    companion object {
        private val log = LoggerFactory.getLogger(TimerService::class.java)
    }
}
