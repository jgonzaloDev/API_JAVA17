package com.silmaur.shop.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ClienteDeuda {
  private Long customerId;
  private String nombre;
  private BigDecimal initialDeposit;     // siempre mostrar
  private BigDecimal remainingDeposit;   // para pagos adicionales o saldo real
  private BigDecimal deuda;
  private BigDecimal saldoAFavor;
  private List<ProductoResumen> productos;
}


