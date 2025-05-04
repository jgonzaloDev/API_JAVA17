package com.silmaur.shop.repository;

import com.silmaur.shop.model.Category;
import com.silmaur.shop.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
  Mono<Category> findByName(String name);
}
