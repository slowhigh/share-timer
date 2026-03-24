package com.sharetimer.syncservice.adapter.input.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.sharetimer.common.config.InfoProps
import com.sharetimer.common.message.TimerAddTimestampMessage
import com.sharetimer.storage.redis.config.RedisMessageListenerContainerFactory
import com.sharetimer.storage.redis.config.TimerRedisProps
import com.sharetimer.syncservice.application.port.input.TimerUseCase
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.stereotype.Component

@Component
class TimerTimestampAddListener(
    private val infoProps: InfoProps,
    private val timerUseCase: TimerUseCase,
    private val objectMapper: ObjectMapper,
    private val timerRedisProps: TimerRedisProps,
    private val factory: RedisMessageListenerContainerFactory,
) : MessageListener {
    @PostConstruct
    fun init() {
        val env = infoProps.environment
        val topic = "$env:${timerRedisProps.pubSub.timestampAddedChannel}"

        log.debug("subscribe topic: {}", topic)

        factory.container.addMessageListener(this, PatternTopic(topic))
    }

    override fun onMessage(
        message: Message,
        pattern: ByteArray?,
    ) {
        val body = String(message.body)

        val msg =
            runCatching { objectMapper.readValue(body, TimerAddTimestampMessage::class.java) }
                .onFailure { log.error("Message parsing failed body: {}", body) }
                .getOrNull() ?: return

        log.debug("onMessage: {}", msg)

        timerUseCase.addTimestamp(
            msg.timerId,
            msg.payload.targetTime,
            msg.payload.capturedAt,
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(TimerTimestampAddListener::class.java)
    }
}
