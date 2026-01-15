package com.sharetimer.web.support.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

  private String statusName;

  public NotFoundException(String statusName, String message) {
    super(message);
    this.statusName = statusName;
  }
}
