package com.sharetimer.syncservice.adapter.output.external

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class TimerApiClient(
    @Qualifier("timerWebClient")
    private val webClient: WebClient,
) {
    fun deleteTimer(timerId: String): Mono<Void> =
        webClient
            .delete()
            .uri("/timers/$timerId")
            .retrieve()
            .bodyToMono(Void::class.java)
}
