package com.silmaur.shop.exception;

public class DocumentIdAlreadyExistsException extends RuntimeException {
  public DocumentIdAlreadyExistsException(String message) {
    super(message);
  }
}