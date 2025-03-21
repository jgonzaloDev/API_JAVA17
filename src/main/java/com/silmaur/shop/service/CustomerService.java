package com.silmaur.shop.service;

import com.silmaur.shop.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
  Mono<CustomerDTO> createCustomer(CustomerDTO customerDto);
  Flux<CustomerDTO> getAllCustomers();
  Mono<CustomerDTO> getCustomerById(Long id);
  Mono<CustomerDTO> updateCustomer(Long id, CustomerDTO customerDto);
  Mono<Void> deleteCustomer(Long id);
}