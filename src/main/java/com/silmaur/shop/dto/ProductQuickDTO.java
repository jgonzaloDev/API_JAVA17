package com.silmaur.shop.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductQuickDTO {
  private String name;
  private BigDecimal salePrice;
  private Integer stock;

}
