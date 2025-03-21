package com.silmaur.shop.repository;

import com.silmaur.shop.model.Product;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
  Mono<Product> findByName(String name);
  @Modifying
  @Query("DELETE FROM products WHERE id = :id")
  Mono<Void> deleteByIdCustom(Long id);
}
