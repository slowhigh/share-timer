package com.sharetimer.storage.redis.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "timer.redis")
data class TimerRedisProps(
    val expiration: Expiration,
    val pubSub: PubSub,
) {
    data class Expiration(
        val keyPrefix: String,
    )

    data class PubSub(
        val targetTimeUpdatedChannel: String,
        val targetTimeUpdatedMessageType: String,
        val timestampAddedChannel: String,
        val timestampAddedMessageType: String,
    )
}
