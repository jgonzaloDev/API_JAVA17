package com.silmaur.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("products")
public class Product {

  @Id
  private String id;

  @Column("name")
  private String name;

  @Column("category_id")
  private Long categoryId;

  @Column("purchase_price")
  private BigDecimal purchasePrice;

  @Column("sale_price")
  private BigDecimal salePrice;

  @Column("stock")
  private int stock;

  @Column("min_stock")
  private int minStock;

  @Column("created_at")
  private LocalDateTime createdAt;

  @Column("updated_at")
  private LocalDateTime updatedAt;

  @Transient  // Usamos la anotación de Spring Data para indicar que este método no se persiste
  public boolean isValidPrice() {
    return salePrice.compareTo(purchasePrice) >= 0;
  }
}
