package com.slowhigh.chronos.common.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

  private String statusName;

  public ForbiddenException(String statusName, String message) {
    super(message);
    this.statusName = statusName;
  }
}
