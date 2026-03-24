package com.sharetimer.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "info")
data class InfoProps(
    val environment: String,
)
