package com.sharetimer.apiservice.adapter.input.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.sharetimer.apiservice.domain.model.Timer
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Timer creation response
 */
@Schema(name = "Timer Create Response", description = "Timer creation response")
data class TimerCreateRes(
    /** Created Timer ID */
    @field:JsonProperty("timerId")
    @field:Schema(
        name = "timerId",
        description = "Timer ID (UUID)",
        example = "c75862bc-0070-4e34-8a93-9380619bd310",
    )
    val timerId: String,
    /** Timer's owner token */
    @field:JsonProperty("ownerToken")
    @field:Schema(
        name = "ownerToken",
        description = "Owner Token (UUID)",
        example = "3abfa8e7-bb9c-418a-af4b-0158cbf5baec",
    )
    val ownerToken: String,
) {
    companion object {
        fun from(timer: Timer): TimerCreateRes =
            TimerCreateRes(
                timerId = timer.id.toString(),
                ownerToken = timer.ownerToken.toString(),
            )
    }
}
