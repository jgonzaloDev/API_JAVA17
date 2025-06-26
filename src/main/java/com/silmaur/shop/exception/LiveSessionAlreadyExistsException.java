package com.silmaur.shop.exception;

public class LiveSessionAlreadyExistsException extends RuntimeException {
  public LiveSessionAlreadyExistsException(String message) {
    super(message);
  }
}
