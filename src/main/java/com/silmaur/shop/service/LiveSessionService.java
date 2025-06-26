package com.silmaur.shop.service;

import com.silmaur.shop.dto.LiveSessionRequestDTO;
import com.silmaur.shop.dto.LiveSessionSummaryDTO;
import com.silmaur.shop.dto.response.LiveSessionResponseDTO;
import com.silmaur.shop.model.LiveSessionSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LiveSessionService {
  Flux<LiveSessionResponseDTO> findAll();
  Mono<LiveSessionResponseDTO> create(LiveSessionRequestDTO dto);
  Mono<LiveSessionResponseDTO> findById(Long id);
  Mono<Void> deleteById(Long id);
  Mono<LiveSessionSummaryDTO> getSessionSummary(Long sessionId);


}
