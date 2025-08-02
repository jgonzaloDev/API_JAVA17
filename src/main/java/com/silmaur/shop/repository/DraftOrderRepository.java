package com.silmaur.shop.repository;

import com.silmaur.shop.dto.DraftOrderDTO;
import com.silmaur.shop.model.DraftOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DraftOrderRepository extends ReactiveCrudRepository<DraftOrder, Long> {
  Flux<DraftOrderDTO> findByLiveSessionId(Long sessionId);

}
