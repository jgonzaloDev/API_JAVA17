package com.silmaur.shop.repository;

import com.silmaur.shop.model.Customer;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
  Mono<Customer> findByEmail(String email);

}
