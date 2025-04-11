package com.silmaur.shop.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para recibir datos necesarios para crear un nuevo pedido.
 * Incluye validaciones para asegurar la integridad y calidad de los datos recibidos.
 */
@Data
public class OrderCreationDTO {

  /**
   * ID del cliente que realiza el pedido.
   * Obligatorio.
   */
  @NotNull(message = "El ID del cliente es obligatorio.")
  private Long customerId;

  /**
   * ID opcional de campaña promocional asociada al pedido.
   */
  private Long campaignId;

  /**
   * ID opcional de la sesión en vivo asociada al pedido.
   */
  private Long liveSessionId;


  /**
   * Lista de items incluidos en el pedido.
   * Debe contener al menos un item válido.
   */
  @NotEmpty(message = "La lista de items no puede estar vacía.")
  @Valid
  private List<OrderItemCreationDTO> items;

  /**
   * Fecha límite opcional para completar el pago total del pedido.
   */
  @Future(message = "La fecha límite de pago debe ser futura.")
  private LocalDateTime paymentDueDate;

  /**
   * Indica si el pedido está acumulando productos o pagos adicionales pendientes.
   * Obligatorio.
   */
  @NotNull(message = "Debe indicar claramente si acumula productos o no.")
  private Boolean accumulation = false;
}
