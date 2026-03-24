package com.sharetimer.apiservice.adapter.output.redis

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.sharetimer.apiservice.application.port.output.TimerEventPort
import com.sharetimer.common.config.InfoProps
import com.sharetimer.common.message.TimerAddTimestampMessage
import com.sharetimer.common.message.TimerUpdateTargetTimeMessage
import com.sharetimer.storage.redis.config.TimerRedisProps
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class TimerRedisPublisher(
    private val infoProps: InfoProps,
    private val timerRedisProps: TimerRedisProps,
    private val objectMapper: ObjectMapper,
    @Qualifier("timerExpirationRedisTemplate")
    private val timerExpirationRedisTemplate: StringRedisTemplate,
    @Qualifier("timerPubSubRedisTemplate")
    private val timerPubSubRedisTemplate: StringRedisTemplate,
) : TimerEventPort {
    companion object {
        private val log = LoggerFactory.getLogger(TimerRedisPublisher::class.java)
    }

    override fun scheduleExpiration(
        timerId: String,
        targetTime: Instant,
    ) {
        val expireSecond = targetTime.epochSecond - Instant.now().epochSecond
        if (expireSecond > 0) {
            val keyPrefix = timerRedisProps.expiration.keyPrefix
            val key = "$keyPrefix$timerId"
            timerExpirationRedisTemplate.opsForValue().set(key, "", expireSecond, TimeUnit.SECONDS)
            log.debug("Timer({}) is set to expire in {} seconds.", timerId, expireSecond)
        }
    }

    override fun publishUpdateTimerTargetTime(
        timerId: String,
        updatedAt: Instant,
        targetTime: Instant,
    ) {
        val channel = timerRedisProps.pubSub.targetTimeUpdatedChannel

        val message =
            TimerUpdateTargetTimeMessage(
                timerId = timerId,
                type = timerRedisProps.pubSub.targetTimeUpdatedMessageType,
                payload =
                    TimerUpdateTargetTimeMessage.Payload(
                        updatedAt = updatedAt,
                        serverTime = Instant.now(),
                        newTargetTime = targetTime,
                    ),
            )

        publishEvent(channel, message)
    }

    override fun publishAddTimestamp(
        timerId: String,
        targetTime: Instant,
        capturedAt: Instant,
    ) {
        val channel = timerRedisProps.pubSub.timestampAddedChannel

        val message =
            TimerAddTimestampMessage(
                timerId = timerId,
                type = timerRedisProps.pubSub.timestampAddedMessageType,
                payload =
                    TimerAddTimestampMessage.Payload(
                        targetTime = targetTime,
                        capturedAt = capturedAt,
                    ),
            )

        publishEvent(channel, message)
    }

    private fun publishEvent(
        channel: String,
        message: Any,
    ) {
        try {
            val jsonMessage = objectMapper.writeValueAsString(message)
            val fullChannel = "${infoProps.environment}:$channel"

            timerPubSubRedisTemplate.convertAndSend(fullChannel, jsonMessage)
            log.debug("Redis publish success: channel={}, message={}", fullChannel, jsonMessage)
        } catch (e: JsonProcessingException) {
            log.error("Message serialization failed: {}", e.message)
        } catch (e: Exception) {
            log.error("Redis publish failed: channel={}, error={}", channel, e.message)
        }
    }
}
