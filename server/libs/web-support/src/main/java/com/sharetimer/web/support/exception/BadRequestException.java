package com.sharetimer.web.support.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

  private String statusName;

  public BadRequestException(String statusName, String message) {
    super(message);
    this.statusName = statusName;
  }
}
