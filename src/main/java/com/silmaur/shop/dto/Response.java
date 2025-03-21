package com.silmaur.shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class Response<T> {
  private HttpStatus status;
  private String message;
  private T data;

  public static <T> Response<T> success(T data) {
    return Response.<T>builder()
        .status(HttpStatus.OK)
        .data(data)
        .build();
  }

  public static <T> Response<T> success(String message, T data) {
    return Response.<T>builder()
        .status(HttpStatus.OK)
        .message(message)
        .data(data)
        .build();
  }

  public static <T> Response<T> error(HttpStatus status, String message, T data) {
    return Response.<T>builder()
        .status(status)
        .message(message)
        .data(data)
        .build();
  }

  public static Response<ErrorResponse> error(HttpStatus status, String message, String errorCode, String field) {
    return Response.<ErrorResponse>builder()
        .status(status)
        .message(message)
        .data(ErrorResponse.builder().errorCode(errorCode).errorMessage(message).field(field).build())
        .build();
  }
}
