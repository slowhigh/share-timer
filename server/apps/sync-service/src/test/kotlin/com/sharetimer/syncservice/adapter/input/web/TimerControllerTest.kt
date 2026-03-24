package com.sharetimer.syncservice.adapter.input.web

import com.sharetimer.syncservice.application.port.input.TimerUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("local")
class TimerControllerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockitoBean
    private lateinit var timerUseCase: TimerUseCase

    @Test
    fun subscribeEvents_ShouldReturnSseStream() {
        val timerId = "test-timer"
        val eventStream: Flow<ServerSentEvent<Any>> =
            flowOf(
                ServerSentEvent
                    .builder<Any>()
                    .event("connect")
                    .data("connected")
                    .build(),
                ServerSentEvent.builder<Any>().event("heartbeat").build(),
            )

        given(timerUseCase.subscribe(timerId)).willReturn(eventStream)

        webTestClient
            .get()
            .uri("/timers/{timerId}", timerId)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus()
            .isOk
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)

        verify(timerUseCase).subscribe(timerId)
    }
}
