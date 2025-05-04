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
@Table("users")
public class User {
  @Id
  private Long id;

  @Column("username")
  private String username;

  @Column("password")
  private String password;

  @Column("role_id")
  private Long roleId; // Cambiado a Long

  @Column("created_at")
  private LocalDateTime createdAt;


}