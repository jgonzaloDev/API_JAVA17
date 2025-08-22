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
  private String title;
  private String platform;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private LocalDateTime createdAt;
  private String status;
}
