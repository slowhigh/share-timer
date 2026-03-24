package com.sharetimer.apiservice.config

import com.sharetimer.storage.redis.config.RedisTemplateFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Clock

@Configuration
class TimerConfig {
    @Bean
    fun clock(): Clock = Clock.systemUTC()

    @Bean("timerExpirationRedisTemplate")
    fun timerExpirationRedisTemplate(redisTemplateFactory: RedisTemplateFactory): StringRedisTemplate =
        redisTemplateFactory.template

    @Bean("timerPubSubRedisTemplate")
    fun timerPubSubRedisTemplate(redisTemplateFactory: RedisTemplateFactory): StringRedisTemplate =
        redisTemplateFactory.template
}
