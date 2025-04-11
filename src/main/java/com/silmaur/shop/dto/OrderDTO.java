package com.silmaur.shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDTO {
  private Long id;
  private Long customerId;
  private Long campaignId;
  private Long liveSessionId;
  private BigDecimal apertura;
  private BigDecimal totalAmount;
  private String status;
  private Boolean accumulation;
  private LocalDateTime paymentDueDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<OrderItemDTO> items;
  private BigDecimal realAmountToPay;
}
