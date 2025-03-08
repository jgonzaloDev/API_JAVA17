package com.silmaur.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("transaction_summary")
public class TransactionSummary {
  @Id
  @Column("customer_id")
  private Long customerId;

  @Column("customer_name")
  private String customerName;

  @Column("total_apertura")
  private BigDecimal totalApertura;

  @Column("total_pagado")
  private BigDecimal totalPagado;

  @Column("saldo_pendiente")
  private BigDecimal saldoPendiente;

  @Column("esta_pagado")
  private boolean estaPagado;

  @Column("last_payment_date")
  private LocalDateTime lastPaymentDate;
}
