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
@Table("live_sessions")
public class LiveSession {
  @Id
  private Long id;

  @Column("title")
  private String title;

  @Column("platform")
  private String platform;

  @Column("start_time")
  private LocalDateTime startTime;

  @Column("end_time")
  private LocalDateTime endTime;

  @Column("created_at")
  private LocalDateTime createdAt;
}
