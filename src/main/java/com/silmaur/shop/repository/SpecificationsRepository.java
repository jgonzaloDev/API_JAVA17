/*
package com.silmaur.shop.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SpecificationsRepository extends ReactiveCrudRepository<Specifications, Long> {

  // Eliminar especificaciones por ID de producto
  @Modifying
  @Query("DELETE FROM product_specifications WHERE product_id = :productId")
  Mono<Void> deleteByProductId(Long productId);
}
*/
