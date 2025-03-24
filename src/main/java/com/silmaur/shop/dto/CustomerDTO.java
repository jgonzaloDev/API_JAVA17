package com.silmaur.shop.dto;

import com.silmaur.shop.model.enums.ShippingPreferences;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

  private Long id;
  private String documentId;
  private String name;
  private String phone;
  private String email;
  private LocalDateTime createdAt;
  private String nickTiktok;
  private BigDecimal initialDeposit;
  private ShippingPreferences shippingPreference ;




}