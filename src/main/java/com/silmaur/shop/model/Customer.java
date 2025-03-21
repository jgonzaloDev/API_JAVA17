package com.silmaur.shop.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("customers")
public class Customer {
  @Id
  private Long id;

  @Column("document_id")
  private String documentId;

  @Column("name")
  private String name;

  @Column("phone")
  private String phone;

  @Column("email")
  private String email;

  @Column("created_at")
  private LocalDateTime createdAt;

  @Column("nick_tiktok")
  private String nickTiktok;

  @Column("initial_deposit")
  private BigDecimal initialDeposit;


}
