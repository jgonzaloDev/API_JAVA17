package com.silmaur.shop.repository;

import com.silmaur.shop.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
  /**
   * Obtiene todos los pedidos realizados por un cliente específico.
   *
   * @param customerId ID del cliente.
   * @return lista reactiva de pedidos.
   */
  Flux<Order> findByCustomerId(Long customerId);
  Mono<Long> countByCustomerId(Long customerId);
}
