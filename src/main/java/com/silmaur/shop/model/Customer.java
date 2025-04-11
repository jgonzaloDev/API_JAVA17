package com.silmaur.shop.model;

import com.silmaur.shop.model.enums.ShippingPreferences;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Representa un cliente en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("customers")
public class Customer {

  /**
   * Identificador único del cliente.
   */
  @Id
  private Long id;

  /**
   * Documento de identidad del cliente.
   */
  @Column("document_id")
  private String documentId;

  /**
   * Nombre completo del cliente.
   */
  @Column("name")
  private String name;

  /**
   * Número de teléfono del cliente.
   */
  @Column("phone")
  private String phone;

  /**
   * Dirección de correo electrónico del cliente.
   */
  @Column("email")
  private String email;

  /**
   * Fecha y hora de creación del cliente.
   */
  @Column("created_at")
  private LocalDateTime createdAt;

  /**
   * Nickname del cliente en TikTok.
   */
  @Column("nick_tiktok")
  private String nickTiktok;

  /**
   * Depósito inicial del cliente.
   */
  @Column("initial_deposit")
  private BigDecimal initialDeposit;

  /**
   * Preferencia de envío del cliente.
   */
  @Column("shipping_preference")
  private ShippingPreferences shippingPreference = ShippingPreferences.ACCUMULATE;
  /**
   * Saldo a favor del cliente.
   */
  @Column("remaining_deposit")
  private BigDecimal remainingDeposit;

}