package com.silmaur.shop.repository;

import com.silmaur.shop.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
  Mono<Product> findByName(String name);
}
