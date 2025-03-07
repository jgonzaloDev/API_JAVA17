package com.silmaur.shop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummary {
  @Id
  private Long customerId;

  @Column(nullable = false)
  private String customerName;

  @Column(nullable = false)
  private BigDecimal totalApertura;

  @Column(nullable = false)
  private BigDecimal totalPagado;

  @Column(nullable = false)
  private BigDecimal saldoPendiente;

  @Column(nullable = false)
  private boolean estaPagado;

  private LocalDateTime lastPaymentDate;
}