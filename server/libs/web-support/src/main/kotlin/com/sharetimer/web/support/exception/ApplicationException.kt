package com.sharetimer.web.support.exception

import org.springframework.http.HttpStatus

sealed class ApplicationException(
    val status: HttpStatus,
    val statusName: String,
    message: String,
) : RuntimeException(message)

class BadRequestException(
    statusName: String,
    message: String,
) : ApplicationException(HttpStatus.BAD_REQUEST, statusName, message)

class NotFoundException(
    statusName: String,
    message: String,
) : ApplicationException(HttpStatus.NOT_FOUND, statusName, message)

class ForbiddenException(
    statusName: String,
    message: String,
) : ApplicationException(HttpStatus.FORBIDDEN, statusName, message)

class DuplicateException(
    statusName: String,
    message: String,
) : ApplicationException(HttpStatus.CONFLICT, statusName, message)
