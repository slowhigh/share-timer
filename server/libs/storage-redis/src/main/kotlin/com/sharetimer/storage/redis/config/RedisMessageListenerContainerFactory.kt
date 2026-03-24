package com.sharetimer.storage.redis.config

import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class RedisMessageListenerContainerFactory(
    private val redisProps: RedisProps,
) {
    val container: RedisMessageListenerContainer by lazy { createContainer() }

    private fun createContainer(): RedisMessageListenerContainer {
        val redisConfig = RedisStandaloneConfiguration(redisProps.host, redisProps.port)

        val factory =
            LettuceConnectionFactory(redisConfig).apply {
                afterPropertiesSet()
            }
        factory.connection.serverCommands().setConfig("notify-keyspace-events", "Ex")

        return RedisMessageListenerContainer().apply {
            setConnectionFactory(factory)
            afterPropertiesSet()
            start()
        }
    }
}
