package com.silmaur.shop.service;

import com.silmaur.shop.dto.DraftOrderDTO;
import com.silmaur.shop.dto.OrderDTO;
import com.silmaur.shop.model.DraftOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DraftOrderService {
  Mono<DraftOrder> createDraftOrder(DraftOrderDTO dto);
  Flux<DraftOrderDTO> findByLiveSessionId(Long sessionId);
  Mono<Void> deleteById(Long id);
  Mono<Void> updateStatus(Long id, String status);
  Mono<OrderDTO> confirmDraftOrder(Long draftOrderId);
  Mono<DraftOrder> updateDraftOrder(Long id, DraftOrderDTO draftOrderDTO);


}
