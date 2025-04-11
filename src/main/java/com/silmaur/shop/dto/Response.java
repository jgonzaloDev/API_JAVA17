package com.silmaur.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
  private int status;
  private String message;
  private T data;

  // Éxito sin mensaje
  public static <T> Response<T> success(T data) {
    return Response.<T>builder()
        .status(HttpStatus.OK.value())
        .data(data)
        .build();
  }

  // Éxito con mensaje
  public static <T> Response<T> success(String message, T data) {
    return Response.<T>builder()
        .status(HttpStatus.OK.value())
        .message(message)
        .data(data)
        .build();
  }

  // Error genérico con data
  public static <T> Response<T> error(HttpStatus status, String message, T data) {
    return Response.<T>builder()
        .status(status.value())
        .message(message)
        .data(data)
        .build();
  }

  // Error estructurado con campo y mensaje
  public static Response<ErrorResponse> error(HttpStatus status, String message, String errorCode, String field) {
    return Response.<ErrorResponse>builder()
        .status(status.value())
        .message(message)
        .data(ErrorResponse.builder()
            .errorCode(errorCode)
            .errorMessage(message)
            .errors(List.of(new ErrorResponse.FieldError(field, message)))
            .build())
        .build();
  }
}
