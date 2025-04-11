package com.silmaur.shop.exception;

/**
 * Excepción personalizada para recursos no encontrados (HTTP 404).
 */
public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}