package com.silmaur.shop.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LiveSessionSaleRequestDTO {
  private Long liveSessionId;
  private Long productId;
  private Long customerId; // opcional
  private Integer quantity;
  private BigDecimal unitPrice;
  private String nickname; // ✅ nuevo campo
  private BigDecimal initialDeposit; // ✅ nuevo c


}
