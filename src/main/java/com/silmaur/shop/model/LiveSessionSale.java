package com.silmaur.shop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("live_session_sales")
public class LiveSessionSale {

  @Id
  private Long id;

  @Column("live_session_id")
  private Long liveSessionId;

  @Column("product_id")
  private Long productId;

  @Column("customer_id")
  private Long customerId;

  private Integer quantity;
  @Column("order_id")
  private Long orderId; // opcional

  @Column("unit_price")
  private BigDecimal unitPrice;

  @Column("total_amount")
  @ReadOnlyProperty
  private BigDecimal totalAmount;

  @Column("created_at")
  private LocalDateTime createdAt;
}
