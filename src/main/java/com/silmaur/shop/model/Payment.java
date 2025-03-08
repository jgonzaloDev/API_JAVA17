package com.silmaur.shop.model;

import com.silmaur.shop.model.enums.PaymentMethod;
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
@Table("payments")
public class Payment {

  @Id
  private Long id;

  @Column("order_id")
  private Long orderId;

  @Column("amount")
  private BigDecimal amount;

  @Column("payment_date")
  private LocalDateTime paymentDate;

  // El campo se almacena como String en la columna "payment_method"
  @Column("payment_method")
  private PaymentMethod paymentMethod;
}
