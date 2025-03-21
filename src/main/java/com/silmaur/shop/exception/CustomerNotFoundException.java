package com.silmaur.shop.exception;

public class CustomerNotFoundException extends RuntimeException{
  public CustomerNotFoundException(String message) {
    super(message);
  }

}
