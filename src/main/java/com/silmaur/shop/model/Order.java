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
@Table("orders")
public class Order {
  @Id
  private Long id;

  // Se almacena el ID del cliente
  @Column("customer_id")
  private Long customerId;

  // Se almacena el ID de la campaña (opcional)
  @Column("campaign_id")
  private Long campaignId;

  // Se almacena el ID de la sesión en vivo (opcional)
  @Column("live_session_id")
  private Long liveSessionId;

  @Column("apertura")
  private BigDecimal apertura;

  @Column("total_amount")
  private BigDecimal totalAmount;

  // El status se almacena como VARCHAR (valores 'PAGADO' o 'NO_PAGADO')
  @Column("status")
  private String status;

  @Column("acumulando")
  private boolean acumulando;

  @Column("payment_due_date")
  private LocalDateTime paymentDueDate;

  @Column("created_at")
  private LocalDateTime createdAt;

  @Column("updated_at")
  private LocalDateTime updatedAt;
}
