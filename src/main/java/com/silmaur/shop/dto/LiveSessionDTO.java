package com.silmaur.shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiveSessionDTO {
  private Long id;
  private String title;
  private String platform;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime startTime;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime endTime;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  public String getStatus() {
    LocalDateTime now = LocalDateTime.now();
    if (startTime != null && endTime != null) {
      if (now.isBefore(startTime)) return "PROGRAMADA";
      if (now.isAfter(endTime)) return "FINALIZADA";
      return "ACTIVA";
    }
    return "DESCONOCIDO";
  }
}