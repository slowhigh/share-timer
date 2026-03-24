package com.sharetimer.web.support.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Error Response", description = "Error Response")
data class ErrorRes(
    @field:JsonProperty("statusCode")
    @field:Schema(name = "statusCode", description = "Status Code", example = "400")
    val statusCode: String,
    @field:JsonProperty("statusName")
    @field:Schema(name = "statusName", description = "Status Name", example = "ArgumentNotValid")
    val statusName: String,
    @field:JsonProperty("message")
    @field:Schema(name = "message", description = "Error Message", example = "Invalid Request.")
    val message: String,
)
