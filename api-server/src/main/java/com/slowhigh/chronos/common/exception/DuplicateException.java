package com.slowhigh.chronos.common.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {

  private String statusName;

  public DuplicateException(String statusName, String message) {
    super(message);
    this.statusName = statusName;
  }
}
