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
@Table("campaigns")
public class Campaign {
  @Id
  private Long id;

  @Column("name")
  private String name;

  @Column("description")
  private String description;

  @Column("start_date")
  private LocalDateTime startDate;

  @Column("end_date")
  private LocalDateTime endDate;
}
