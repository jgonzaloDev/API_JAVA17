package com.silmaur.shop.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("draft_orders")
public class DraftOrder {

  @Id
  private Long id;

  @Column("live_session_id")
  private Long liveSessionId;

  @Column("customer_id")
  private Long customerId;

  @Column("nickname")
  private String nickname;

  @Column("total_amount")
  private BigDecimal totalAmount;

  @Column("created_at")
  private LocalDateTime createdAt;

  @Column("status")
  private String status;

  @Transient
  private List<DraftOrderItem> items; // 🟢 NECESARIO para mapear con MapStruct
}
