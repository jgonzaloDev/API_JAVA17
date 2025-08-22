package com.silmaur.shop.service;

import com.silmaur.shop.dto.LiveSessionDTO;
import com.silmaur.shop.dto.LiveSessionRequestDTO;
import com.silmaur.shop.dto.LiveSessionSummaryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LiveSessionService {

  Flux<LiveSessionDTO> findAll();

  Mono<LiveSessionDTO> create(LiveSessionRequestDTO dto);

  Mono<LiveSessionDTO> findById(Long id);

  Mono<Void> deleteById(Long id);

  Mono<LiveSessionSummaryDTO> getSessionSummary(Long sessionId);
  Mono<Void> finalizeSession(Long id);

}
