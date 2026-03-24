package com.sharetimer.storage.redis.config

import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisTemplateFactory(
    private val redisProps: RedisProps,
) {
    val template: StringRedisTemplate by lazy { createTemplate() }

    private fun createTemplate(): StringRedisTemplate {
        val redisConfig = RedisStandaloneConfiguration(redisProps.host, redisProps.port)

        val factory =
            LettuceConnectionFactory(redisConfig).apply {
                afterPropertiesSet()
            }

        return StringRedisTemplate(factory).apply {
            afterPropertiesSet()
        }
    }
}
