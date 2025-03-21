package com.silmaur.shop.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
  private String errorCode;
  private String errorMessage;
  private String field;
}


