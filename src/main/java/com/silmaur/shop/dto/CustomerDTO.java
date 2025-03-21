package com.silmaur.shop.dto;

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
public class CustomerDTO {

  private Long id;
  private String documentId;
  private String name;
  private String phone;
  private String email;
  private LocalDateTime createdAt;
  private String nickTiktok;
  private BigDecimal initialDeposit;




}