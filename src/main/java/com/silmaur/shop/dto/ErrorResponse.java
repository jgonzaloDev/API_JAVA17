package com.silmaur.shop.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ErrorResponse {

  private String errorCode;
  private String errorMessage;
  private List<FieldError> errors; // NUEVO


  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FieldError {

    private String field;
    private String message;

  }
}
