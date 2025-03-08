package com.silmaur.shop.repository;

import com.silmaur.shop.model.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {
  Mono<Role> findByName(String name);
}
