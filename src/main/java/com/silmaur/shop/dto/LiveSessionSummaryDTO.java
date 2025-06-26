package com.silmaur.shop.dto;

import com.silmaur.shop.model.ClienteDeuda;
import com.silmaur.shop.model.ProductoResumen;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class LiveSessionSummaryDTO {
  private Long sessionId;
  private String sessionTitle;
  private BigDecimal totalVendido;
  private int totalOrdenes;
  private int productosDistintos;
  private List<ProductoResumen> topProductos;
  private List<ClienteDeuda> clientesConDeuda;
}
