package com.silmaur.shop.dto;

import com.silmaur.shop.dto.response.LiveSessionResponseDTO;
import com.silmaur.shop.model.LiveSession;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveSessionRequestDTO {
    private String title;
    private String platform;
    private LocalDateTime startTime;
    private LocalDateTime endTime;




}
