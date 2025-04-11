package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.CustomerDTO;
import com.silmaur.shop.exception.CustomerNotFoundException;
import com.silmaur.shop.exception.DocumentIdAlreadyExistsException;
import com.silmaur.shop.handler.mapper.CustomerMapper;
import com.silmaur.shop.model.Customer;
import com.silmaur.shop.repository.CustomerRepository;
import com.silmaur.shop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  @Override
  public Mono<CustomerDTO> createCustomer(CustomerDTO customerDto) {
    // ⚠️ Validar si documentId está vacío o null


    Customer createCustomer = customerMapper.toEntity(customerDto);
    createCustomer.setCreatedAt(LocalDateTime.now());

    return customerRepository.save(createCustomer)
        .onErrorResume(DataIntegrityViolationException.class, ex -> {
          if (ex.getMessage().contains("Unique index or primary key violation") ||
              ex.getMessage().contains("constraint violation")) {
            return Mono.error(new DocumentIdAlreadyExistsException(
                "El documento de identidad '" + customerDto.getDocumentId() + "' ya está en uso."));
          }
          return Mono.error(ex);
        })
        .map(customerMapper::toDto);
  }


  @Override
  public Flux<CustomerDTO> getAllCustomers() {
    return customerRepository.findAll()
        .map(customerMapper::toDto);
  }

  @Override
  public Mono<CustomerDTO> getCustomerById(Long id) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("Cliente no encontrado con ID: " + id)))
        .map(customerMapper::toDto); // Si tienes mapper, mejor
  }

  @Override
  public Mono<CustomerDTO> updateCustomer(Long id, CustomerDTO customerDto) {

    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomerNotFoundException("Cliente no encontrado con id " + id)))
        .flatMap(existingCustomer -> {
          Customer updatedCustomer = Customer.builder()
              .id(existingCustomer.getId())
              .name(customerDto.getName())
              .phone(customerDto.getPhone())
              .email(customerDto.getEmail())
              .documentId(customerDto.getDocumentId())
              .initialDeposit(customerDto.getInitialDeposit())
              .nickTiktok(customerDto.getNickTiktok())
              .shippingPreference(customerDto.getShippingPreference())
              .createdAt(existingCustomer.getCreatedAt())
              .build();
          return customerRepository.save(updatedCustomer);
        })
        .map(customerMapper::toDto);
  }

  @Override
  public Mono<Void> deleteCustomer(Long id) {
    log.debug("Intentando eliminar el cliente con ID: {}", id);

    return customerRepository.findById(id)
        .flatMap(customer -> {
          log.debug("Cliente encontrado con ID: {}, intentando eliminar.", id);
          return customerRepository.delete(customer)
              .doOnSuccess(v -> log.debug("Cliente con ID: {} eliminado correctamente.", id))
              .doOnError(e -> log.error("Error al eliminar el cliente con ID: {}", id, e));
        }).then();
  }
}
