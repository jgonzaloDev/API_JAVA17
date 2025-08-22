package com.silmaur.shop.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDebtDTO {
  private Long customerId;
  private String nickname;
  private String name;
  private BigDecimal deuda;        // monto pendiente
  private BigDecimal saldoAFavor;  // si tiene depósitos disponibles
}
