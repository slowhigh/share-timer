package com.sharetimer.syncservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean("timerWebClient")
    fun webClient(webClientProps: WebClientProps): WebClient =
        WebClient.builder().baseUrl(requireNotNull(webClientProps.url)).build()
}
