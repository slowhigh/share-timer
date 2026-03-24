package com.sharetimer.syncservice.adapter.input.listener

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
class TimerExpirationListener(
    private val timerUseCase: TimerUseCase,
    private val timerRedisProps: TimerRedisProps,
    private val factory: RedisMessageListenerContainerFactory,
) : MessageListener {
    @PostConstruct
    fun init() {
        factory.container.addMessageListener(this, PatternTopic("__keyevent@0__:expired"))
    }

    override fun onMessage(
        message: Message,
        pattern: ByteArray?,
    ) {
        val body = String(message.body)
        log.info("Expired key: {}", body)

        body
            .removePrefix(timerRedisProps.expiration.keyPrefix)
            .takeIf { it != body }
            ?.let { timerUseCase.processTimerExpiration(it) }
    }

    companion object {
        private val log = LoggerFactory.getLogger(TimerExpirationListener::class.java)
    }
}
