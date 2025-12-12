package com.sharetimer.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.sharetimer.core.common.exception.BadRequestException;
import com.sharetimer.core.common.exception.DuplicateException;
import com.sharetimer.core.common.exception.ErrorRes;
import com.sharetimer.core.common.exception.ForbiddenException;
import com.sharetimer.core.common.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorRes> handleBadRequestException(BadRequestException ex,
      HttpServletRequest request) {
    return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getStatusName(), ex.getMessage());
  }

  /** @Valid 어노테이션 사용 시 발생하는 예외 처리 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorRes> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    FieldError fieldError = ex.getBindingResult().getFieldError();
    String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Invalid Request";
    return createErrorResponse(HttpStatus.BAD_REQUEST, "ArgumentNotValid", errorMessage);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorRes> handleNotFoundException(NotFoundException ex,
      HttpServletRequest request) {
    return createErrorResponse(HttpStatus.NOT_FOUND, ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<ErrorRes> handleDuplicateException(DuplicateException ex,
      HttpServletRequest request) {
    return createErrorResponse(HttpStatus.CONFLICT, ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorRes> handleForbiddenException(ForbiddenException ex,
      HttpServletRequest request) {
    return createErrorResponse(HttpStatus.FORBIDDEN, ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorRes> handleGlobalException(Exception ex, HttpServletRequest request) {
    return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServerError",
        ex.getMessage());
  }

  private ResponseEntity<ErrorRes> createErrorResponse(HttpStatus status, String statusName,
      String message) {
    ErrorRes errorResponse = new ErrorRes(String.valueOf(status.value()), statusName, message);
    return new ResponseEntity<>(errorResponse, status);
  }

}
