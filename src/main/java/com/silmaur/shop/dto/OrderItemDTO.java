package com.silmaur.shop.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO que representa un ítem ya creado en un pedido.
 */
@Data
public class OrderItemDTO {

  private Long id;

  // 🔧 Campo que estaba faltando
  private Long orderId;

  private Long productId;
  private String productName;
  private BigDecimal price;
  private Integer quantity;
  private BigDecimal discount;
}
