package com.sharetimer.web.support;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.sharetimer.web.support.exception.BadRequestException;
import com.sharetimer.web.support.exception.DuplicateException;
import com.sharetimer.web.support.exception.ForbiddenException;
import com.sharetimer.web.support.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException ex) {
    return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getStatusName(), ex.getMessage());
  }

  /** Exception handling for @Valid annotation usage */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    FieldError fieldError = ex.getBindingResult().getFieldError();
    String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Invalid Request";
    return createProblemDetail(HttpStatus.BAD_REQUEST, "ArgumentNotValid", errorMessage);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFoundException(NotFoundException ex) {
    return createProblemDetail(HttpStatus.NOT_FOUND, ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<ProblemDetail> handleDuplicateException(DuplicateException ex) {
    return createProblemDetail(HttpStatus.CONFLICT, ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ProblemDetail> handleForbiddenException(ForbiddenException ex) {
    return createProblemDetail(HttpStatus.FORBIDDEN, ex.getStatusName(), ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleGlobalException(Exception ex) {
    return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServerError",
        ex.getMessage());
  }

  private ResponseEntity<ProblemDetail> createProblemDetail(HttpStatus status, String title,
      String detail) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setTitle(title);
    problemDetail.setType(URI.create("https://api.sharetimer.com/errors/" + title.toLowerCase()));
    return ResponseEntity.status(status).body(problemDetail);
  }

}
