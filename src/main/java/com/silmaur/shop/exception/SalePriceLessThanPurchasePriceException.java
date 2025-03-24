package com.silmaur.shop.exception;

public class SalePriceLessThanPurchasePriceException extends RuntimeException{
  public SalePriceLessThanPurchasePriceException(String message) {
    super(message);
  }

}
