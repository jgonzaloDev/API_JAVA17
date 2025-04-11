package com.silmaur.shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
  private Long id;

  @NotBlank(message = "El nombre no puede estar vacío")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  private String name;

  @NotNull(message = "La categoría es obligatoria")
  private Long categoryId;

  @NotNull(message = "El precio de compra es obligatorio")
  @Positive(message = "El precio de compra debe ser mayor a 0")
  private BigDecimal purchasePrice;

  @NotNull(message = "El precio de venta es obligatorio")
  @Positive(message = "El precio de venta debe ser mayor a 0")
  @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor que el precio de compra")
  private BigDecimal salePrice;

  @NotNull(message = "El stock es obligatorio")
  @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;

  @NotNull(message = "El stock mínimo es obligatorio")
  @Min(value = 0, message = "El stock mínimo no puede ser negativo")
  private Integer minStock;

  private String description;
  private String imageUrl;
  private String brand;
  private String model;
  private SpecificationsDTO specifications;
  private String status;
  private String unit;
  private String barcode;
  private String sku;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private BigDecimal discount;
}
