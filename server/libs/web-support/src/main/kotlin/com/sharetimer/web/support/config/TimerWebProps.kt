package com.sharetimer.web.support.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "timer.web")
data class TimerWebProps(
    val ownerTokenHeader: String,
)
