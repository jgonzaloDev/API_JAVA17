package com.silmaur.shop.exception;

public class LiveSessionNotFoundException extends RuntimeException {
  public LiveSessionNotFoundException(String message) {
    super(message);
  }
}
