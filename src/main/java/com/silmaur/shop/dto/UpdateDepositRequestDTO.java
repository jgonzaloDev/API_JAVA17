package com.silmaur.shop.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateDepositRequestDTO {
  private BigDecimal amount; // positivo = recarga, negativo = descuento/ajuste
}