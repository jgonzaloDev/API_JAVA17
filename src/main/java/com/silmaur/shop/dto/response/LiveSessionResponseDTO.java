package com.silmaur.shop.dto.response;

import com.silmaur.shop.model.LiveSession;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LiveSessionResponseDTO {
  private Long id;
  private String title;
  private String platform;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private LocalDateTime createdAt;

  public static LiveSessionResponseDTO fromEntity(LiveSession entity) {
    return LiveSessionResponseDTO.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .platform(entity.getPlatform())
        .startTime(entity.getStartTime())
        .endTime(entity.getEndTime())
        .createdAt(entity.getCreatedAt())
        .build();
  }

}
