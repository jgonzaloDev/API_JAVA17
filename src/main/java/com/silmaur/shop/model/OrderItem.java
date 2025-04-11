package com.silmaur.shop.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * Representa un ítem individual dentro de un pedido.
 *
 * <p>Cada ítem está vinculado a un pedido y a un producto específico,
 * e incluye información sobre el precio aplicado, cantidad, y cualquier
 * descuento aplicado al momento de la venta.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("order_items")
public class OrderItem {

  /**
   * Identificador único del ítem.
   */
  @Id
  private Long id;

  /**
   * Identificador del pedido al que pertenece este ítem.
   */
  private Long orderId;

  /**
   * Identificador del producto vendido.
   */
  private Long productId;

  /**
   * Nombre del producto en el momento de la venta (puede diferir si el nombre del producto cambia).
   */
  private String productName;

  /**
   * Precio unitario aplicado al producto (puede diferir del precio actual del producto).
   */
  private BigDecimal price;

  /**
   * Cantidad de unidades vendidas.
   */
  private Integer quantity;

  /**
   * Descuento aplicado al ítem. Por defecto, 0.00 si no aplica descuento.
   */
  private BigDecimal discount;
}
