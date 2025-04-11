package com.silmaur.shop.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una orden (pedido) dentro del sistema Silmaur Shop.
 * Almacena información del cliente, campaña, sesión en vivo, estado del pedido,
 * montos, fechas, y días sin pagar para control interno.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("orders")
public class Order {

  /**
   * ID único del pedido generado automáticamente por la base de datos.
   */
  @Id
  private Long id;

  /**
   * ID del cliente que realiza el pedido.
   */
  private Long customerId;

  /**
   * ID opcional de la campaña promocional asociada al pedido.
   */
  private Long campaignId;

  /**
   * ID opcional de la sesión en vivo asociada al pedido.
   */
  private Long liveSessionId;

  /**
   * Monto inicial del pedido (apertura o adelanto inicial).
   */
  private BigDecimal aperture;

  /**
   * Monto total del pedido (calculado internamente en backend).
   */
  private BigDecimal totalAmount;

  /**
   * Estado actual del pedido (PAGADO o NO_PAGADO).
   */
  private String status;

  /**
   * Indica si el pedido acumula productos o pagos adicionales pendientes.
   */
  private Boolean accumulation;

  /**
   * Fecha límite para completar el pago total del pedido.
   */
  private LocalDateTime paymentDueDate;

  /**
   * Días transcurridos sin recibir un pago completo (campo interno).
   */
  private Integer diasSinPagar;

  /**
   * Fecha y hora de creación del pedido.
   */
  private LocalDateTime createdAt;

  /**
   * Fecha y hora de la última actualización del pedido.
   */
  private LocalDateTime updatedAt;
  /**
   * Monto real que debe pagar el cliente
   */
  private BigDecimal realAmountToPay;
}
