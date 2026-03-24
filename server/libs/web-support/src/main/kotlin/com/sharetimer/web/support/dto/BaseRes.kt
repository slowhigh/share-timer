package com.sharetimer.web.support.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Base class for all response classes
 */
data class BaseRes<T>(
    /** Response status code */
    @field:JsonProperty("statusCode")
    @field:JsonSerialize(using = ToStringSerializer::class)
    @field:Schema(name = "statusCode", description = "Status Code", example = "201")
    val statusCode: Int,
    /** Response status name */
    @field:JsonProperty("statusName")
    @field:Schema(name = "statusName", description = "Status Name", example = "Success")
    val statusName: String,
    /** Response data */
    @field:JsonProperty("data")
    @field:Schema(name = "data", description = "Response Data", nullable = true)
    val data: T,
) {
    companion object {
        fun <T> of(
            status: HttpStatus,
            data: T,
        ): ResponseEntity<BaseRes<T>> {
            val body =
                BaseRes(
                    statusCode = status.value(),
                    statusName = status.name,
                    data = data,
                )
            return ResponseEntity.status(status).body(body)
        }
    }
}
