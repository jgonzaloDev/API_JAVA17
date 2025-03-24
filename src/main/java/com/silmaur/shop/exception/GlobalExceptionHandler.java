package com.silmaur.shop.exception;

import com.silmaur.shop.dto.AuthResponse;
import com.silmaur.shop.dto.ErrorResponse;
import com.silmaur.shop.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UsernameNotFoundException.class)
  public Mono<ResponseEntity<AuthResponse>> handleUserNotFoundException(UsernameNotFoundException e) {
    log.warn("Usuario no encontrado: {}", e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new AuthResponse("Usuario no encontrado")));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public Mono<ResponseEntity<AuthResponse>> handleBadCredentialsException(BadCredentialsException e) {
    log.warn("Contraseña incorrecta: {}", e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new AuthResponse("Contraseña incorrecta")));
  }

  @ExceptionHandler(LockedException.class)
  public Mono<ResponseEntity<AuthResponse>> handleLockedException(LockedException e) {
    log.warn("Cuenta bloqueada: {}", e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new AuthResponse("Cuenta bloqueada")));
  }

  @ExceptionHandler(DisabledException.class)
  public Mono<ResponseEntity<AuthResponse>> handleDisabledException(DisabledException e) {
    log.warn("Cuenta deshabilitada: {}", e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new AuthResponse("Cuenta deshabilitada")));
  }

  @ExceptionHandler(CredentialsExpiredException.class)
  public Mono<ResponseEntity<AuthResponse>> handleCredentialsExpiredException(CredentialsExpiredException e) {
    log.warn("Credenciales expiradas: {}", e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new AuthResponse("Credenciales expiradas")));
  }

/*  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<AuthResponse>> handleGenericException(Exception e) {
    log.error("Error inesperado en autenticación: {}", e.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new AuthResponse("Error de autenticación desconocido")));
  }*/

  @ExceptionHandler(SalePriceLessThanPurchasePriceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<ErrorResponse> handleSalePriceLessThanPurchasePriceException(SalePriceLessThanPurchasePriceException ex) {
    return Response.<ErrorResponse>builder()
        .status(HttpStatus.BAD_REQUEST)
        .message(ex.getMessage())
        .data(ErrorResponse.builder()
            .errorCode("SALE_PRICE_INVALID")
            .errorMessage(ex.getMessage())
            .field("salePrice")
            .build())
        .build();
  }

  @ExceptionHandler(DocumentIdAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Response<ErrorResponse> handleDocumentExistsException(DocumentIdAlreadyExistsException ex) {
    return Response.error(
        HttpStatus.CONFLICT,
        ex.getMessage(),
        "PRODUCT_EXISTS",
        "name"
    );
  }

  // Opcional: Manejo genérico
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response<ErrorResponse> handleGeneralException(Exception ex) {
    return Response.error(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Ha ocurrido un error inesperado",
        "INTERNAL_ERROR",
        null
    );
  }

}
