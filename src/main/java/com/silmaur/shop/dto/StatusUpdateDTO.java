package com.silmaur.shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateDTO {
  @NotNull
  private String status; // "PAGADO" o "NO_PAGADO"
}
