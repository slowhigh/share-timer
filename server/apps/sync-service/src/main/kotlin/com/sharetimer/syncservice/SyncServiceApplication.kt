package com.sharetimer.syncservice

import com.sharetimer.common.config.InfoProps
import com.sharetimer.storage.redis.config.RedisProps
import com.sharetimer.storage.redis.config.TimerRedisProps
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@EnableConfigurationProperties(InfoProps::class, RedisProps::class, TimerRedisProps::class)
@SpringBootApplication(scanBasePackages = ["com.sharetimer"])
class SyncServiceApplication

fun main(args: Array<String>) {
    runApplication<SyncServiceApplication>(*args)
}
