package com.sharetimer.storage.redis.config

import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component

@Component
class ReactiveRedisTemplateFactory(
    private val redisProps: RedisProps,
) {
    val template: ReactiveRedisTemplate<String, String> by lazy { createTemplate() }

    private fun createTemplate(): ReactiveRedisTemplate<String, String> {
        val redisConfig = RedisStandaloneConfiguration(redisProps.host, redisProps.port)

        val factory =
            LettuceConnectionFactory(redisConfig).apply {
                afterPropertiesSet()
            }

        val serializer = StringRedisSerializer()
        val serializationContext =
            RedisSerializationContext
                .newSerializationContext<String, String>(serializer)
                .key(serializer)
                .value(serializer)
                .hashKey(serializer)
                .hashValue(serializer)
                .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}
