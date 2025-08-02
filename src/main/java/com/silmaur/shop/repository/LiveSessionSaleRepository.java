package com.silmaur.shop.repository;

import com.silmaur.shop.model.LiveSessionSale;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LiveSessionSaleRepository extends ReactiveCrudRepository<LiveSessionSale, Long> {
  Flux<LiveSessionSale> findByLiveSessionId(Long liveSessionId);
}