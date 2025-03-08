package com.silmaur.shop.model;

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

  @Column("name")
  private String name;

  @Column("phone")
  private String phone;

  @Column("email")
  private String email;

  @Column("created_at")
  private LocalDateTime createdAt;
}
