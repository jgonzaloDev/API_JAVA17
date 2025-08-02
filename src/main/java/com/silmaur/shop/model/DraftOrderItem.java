package com.silmaur.shop.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("draft_order_items")
public class DraftOrderItem {

  @Id
  private Long id;

  @Column("draft_order_id")
  private Long draftOrderId;

  @Column("product_id")
  private Long productId;

  @Column("quantity")
  private int quantity;

  @Column("unit_price")
  private BigDecimal unitPrice;
  @Transient // ✅ No se mapea a la base de datos
  private String productName;

}
