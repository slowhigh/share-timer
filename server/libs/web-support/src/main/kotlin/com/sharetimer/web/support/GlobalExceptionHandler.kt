package com.sharetimer.web.support

import com.sharetimer.web.support.exception.ApplicationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.net.URI

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(ex: ApplicationException): ResponseEntity<ProblemDetail> =
        createProblemDetail(ex.status, ex.statusName, ex.message)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ProblemDetail> =
        createProblemDetail(HttpStatus.BAD_REQUEST, "InvalidArgument", ex.message)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> {
        val errorMessage = ex.bindingResult.fieldError?.defaultMessage ?: "Invalid Request"
        return createProblemDetail(HttpStatus.BAD_REQUEST, "ArgumentNotValid", errorMessage)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception): ResponseEntity<ProblemDetail> =
        createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServerError", ex.message)

    private fun createProblemDetail(
        status: HttpStatus,
        title: String,
        detail: String?,
    ): ResponseEntity<ProblemDetail> =
        ResponseEntity.status(status).body(
            ProblemDetail.forStatusAndDetail(status, detail).apply {
                this.title = title
                type = URI.create("https://api.sharetimer.com/errors/${title.lowercase()}")
            },
        )
}
