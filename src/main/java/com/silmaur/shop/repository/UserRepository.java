package com.silmaur.shop.repository;

import com.silmaur.shop.model.User;
import java.util.Optional;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
  Mono<User> findByUsername(String username);
}
