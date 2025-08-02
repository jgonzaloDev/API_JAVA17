package com.silmaur.shop.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LiveSessionSaleResponseDTO {
  private Long id;
  private String productName;
  private Long productId;               // ✅ AGREGA ESTA LÍNEA
  private Integer quantity;
  private BigDecimal unitPrice;   // <-- NUEVO: precio unitario
  private BigDecimal totalAmount;
  private String customerName;
  private LocalDateTime createdAt;
  private Long orderId; // si la venta fue enlazada a un pedido
  private Long customerId;

}
