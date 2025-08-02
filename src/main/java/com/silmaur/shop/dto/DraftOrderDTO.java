package com.silmaur.shop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftOrderDTO {

  private Long id;

  @NotNull(message = "La sesión es obligatoria")
  private Long liveSessionId;

  private Long customerId;

  @NotNull(message = "El nickname es obligatorio")
  private String nickname;

  @NotNull(message = "El total es obligatorio")
  private BigDecimal totalAmount;

  private LocalDateTime createdAt;

  private String status;

  private List<DraftOrderItemDTO> items;
}
