package com.silmaur.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("order_items")
public class OrderItem {
  @Id
  private Long id;

  @Column("order_id")
  private Long orderId;

  @Column("product_id")
  private String productId;

  @Column("product_name")
  private String productName;

  @Column("price")
  private BigDecimal price;

  @Column("quantity")
  private int quantity;

  @Column("discount")
  private BigDecimal discount;
}
