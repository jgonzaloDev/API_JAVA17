package com.silmaur.shop.service;

import com.silmaur.shop.dto.LiveSessionSaleRequestDTO;
import com.silmaur.shop.dto.response.LiveSessionSaleResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LiveSessionSaleService {
  Mono<LiveSessionSaleResponseDTO> create(LiveSessionSaleRequestDTO dto);
  Flux<LiveSessionSaleResponseDTO> findBySession(Long liveSessionId);
}

