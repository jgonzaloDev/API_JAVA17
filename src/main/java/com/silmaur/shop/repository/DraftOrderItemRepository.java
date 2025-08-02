package com.silmaur.shop.repository;

import com.silmaur.shop.model.DraftOrderItem;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DraftOrderItemRepository extends ReactiveCrudRepository<DraftOrderItem, Long> {
  Flux<DraftOrderItem> findByDraftOrderId(Long draftOrderId);
  @Modifying
  @Query("DELETE FROM draft_order_items WHERE draft_order_id = :draftOrderId")
  Mono<Void> deleteByDraftOrderId(Long draftOrderId);

}
