package com.silmaur.shop.exception;

import com.silmaur.shop.dto.ErrorResponse;
import com.silmaur.shop.dto.ErrorResponse.FieldError;
import com.silmaur.shop.dto.Response;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(SalePriceLessThanPurchasePriceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<ErrorResponse> handleSalePriceException(SalePriceLessThanPurchasePriceException ex) {
    log.warn("Validación fallida: {}", ex.getMessage());

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message(ex.getMessage())
        .data(ErrorResponse.builder()
            .errorCode("SALE_PRICE_INVALID")
            .errorMessage(ex.getMessage())
            .errors(List.of(new FieldError("salePrice", ex.getMessage())))
            .build())
        .build();
  }

  @ExceptionHandler(DocumentIdAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Response<ErrorResponse> handleDocumentExistsException(DocumentIdAlreadyExistsException ex) {
    log.warn("Documento duplicado: {}", ex.getMessage());

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.CONFLICT.value())
        .message(ex.getMessage())
        .data(ErrorResponse.builder()
            .errorCode("PRODUCT_EXISTS")
            .errorMessage(ex.getMessage())
            .errors(List.of(new FieldError("name", ex.getMessage())))
            .build())
        .build();
  }

  @ExceptionHandler(ProductAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Response<ErrorResponse> handleProductAlreadyExists(ProductAlreadyExistsException ex) {
    log.warn("Producto duplicado: {}", ex.getMessage());

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.CONFLICT.value())
        .message(ex.getMessage())
        .data(ErrorResponse.builder()
            .errorCode("PRODUCT_EXISTS")
            .errorMessage(ex.getMessage())
            .errors(List.of(new FieldError("name", ex.getMessage())))
            .build())
        .build();
  }

  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<ErrorResponse> handleValidationErrors(WebExchangeBindException ex) {
    var fieldErrors = ex.getFieldErrors().stream()
        .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
        .toList();

    log.warn("Errores de validación: {}", fieldErrors);

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message("Error de validación en uno o más campos")
        .data(ErrorResponse.builder()
            .errorCode("VALIDATION_ERROR")
            .errorMessage("Campos inválidos")
            .errors(fieldErrors)
            .build())
        .build();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    var fieldErrors = ex.getConstraintViolations().stream()
        .map(v -> new FieldError(
            v.getPropertyPath().toString(), v.getMessage()
        ))
        .toList();

    log.warn("Violación de restricciones: {}", fieldErrors);

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message("Error de validación en uno o más campos")
        .data(ErrorResponse.builder()
            .errorCode("VALIDATION_ERROR")
            .errorMessage("Campos inválidos")
            .errors(fieldErrors)
            .build())
        .build();
  }

  @ExceptionHandler(ServerWebInputException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<ErrorResponse> handleWebInput(ServerWebInputException ex) {
    var error = new FieldError("body", ex.getReason());

    log.warn("Error en el cuerpo de la solicitud: {}", ex.getReason());

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message("El cuerpo de la solicitud es inválido o está mal formado")
        .data(ErrorResponse.builder()
            .errorCode("REQUEST_BODY_INVALID")
            .errorMessage("No se pudo procesar el cuerpo del request")
            .errors(List.of(error))
            .build())
        .build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Response<ErrorResponse> handleGeneralException(Exception ex) {
    log.error("Error interno inesperado", ex);

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message("Ha ocurrido un error inesperado")
        .data(ErrorResponse.builder()
            .errorCode("INTERNAL_ERROR")
            .errorMessage("Ha ocurrido un error inesperado")
            .errors(null)
            .build())
        .build();
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Response<ErrorResponse> handleNotFound(NotFoundException ex) {
    log.warn("Recurso no encontrado: {}", ex.getMessage());

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.NOT_FOUND.value())
        .message(ex.getMessage())
        .data(ErrorResponse.builder()
            .errorCode("RESOURCE_NOT_FOUND")
            .errorMessage(ex.getMessage())
            .errors(null)
            .build())
        .build();
  }

  @ExceptionHandler(ProductNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Response<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
    log.warn("Producto no encontrado: {}", ex.getMessage());

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.NOT_FOUND.value())
        .message(ex.getMessage())
        .data(ErrorResponse.builder()
            .errorCode("PRODUCT_NOT_FOUND")
            .errorMessage(ex.getMessage())
            .errors(null)
            .build())
        .build();
  }

  @ExceptionHandler(InsufficientStockException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Response<ErrorResponse> handleInsufficientStock(InsufficientStockException ex) {
    log.warn("Stock insuficiente: {}", ex.getMessage());

    return Response.<ErrorResponse>builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .message("No hay stock suficiente para uno o más productos")
        .data(ErrorResponse.builder()
            .errorCode("INSUFFICIENT_STOCK")
            .errorMessage(ex.getMessage())
            .errors(List.of(new FieldError("stock", ex.getMessage())))
            .build())
        .build();
  }


}
