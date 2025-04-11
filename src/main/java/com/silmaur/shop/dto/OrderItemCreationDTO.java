package com.silmaur.shop.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO que representa un item individual dentro de un pedido en creación.
 * Incluye validaciones estrictas para garantizar consistencia.
 */
@Data
public class OrderItemCreationDTO {

  /**
   * ID del producto asociado al item del pedido.
   * Obligatorio.
   */
  @NotNull(message = "El ID del producto es obligatorio.")
  private Long productId;



  /**
   * Cantidad solicitada del producto.
   * Obligatorio, mínimo 1 unidad.
   */
  @NotNull(message = "La cantidad es obligatoria.")
  @Min(value = 1, message = "La cantidad mínima es 1 unidad.")
  private Integer quantity;

  /**
   * Descuento aplicado al producto (opcional, por defecto cero).
   * Debe ser un valor positivo o cero.
   */
  @DecimalMin(value = "0.00", inclusive = true, message = "El descuento no puede ser negativo.")
  private BigDecimal discount = BigDecimal.ZERO;
}
