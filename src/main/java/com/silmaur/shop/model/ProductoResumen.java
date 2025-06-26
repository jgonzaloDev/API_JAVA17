package com.silmaur.shop.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductoResumen {
  private Long productId;
  private String nombre;
  private int cantidadVendida;
  private BigDecimal totalGenerado;
}
