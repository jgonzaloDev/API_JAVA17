package com.silmaur.shop.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ClienteDeuda {
  private Long customerId;
  private String nombre;
  private BigDecimal deuda;
}
