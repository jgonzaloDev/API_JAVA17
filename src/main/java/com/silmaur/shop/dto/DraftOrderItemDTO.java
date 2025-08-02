package com.silmaur.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftOrderItemDTO {

  private Long id;

  private Long draftOrderId; // ✅ necesario para el builder

  @NotNull(message = "El producto es obligatorio")
  private Long productId;

  private String productName; // ✅ opcional, para mostrarlo en UI si deseas

  @Min(value = 1, message = "La cantidad debe ser mayor a 0")
  private int quantity;

  @NotNull(message = "El precio unitario es obligatorio")
  private BigDecimal unitPrice;
}
