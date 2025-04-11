package com.silmaur.shop.repository;

import com.silmaur.shop.model.OrderItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {

  /**
   * Recupera todos los ítems asociados a un pedido específico.
   *
   * @param orderId ID del pedido.
   * @return Flux con los ítems del pedido.
   */
  Flux<OrderItem> findAllByOrderId(Long orderId);
}
