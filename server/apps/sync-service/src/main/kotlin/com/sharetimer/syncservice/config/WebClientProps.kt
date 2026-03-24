package com.sharetimer.syncservice.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "webclient.timer-api")
data class WebClientProps(
    val url: String,
)
