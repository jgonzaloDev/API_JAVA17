package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.LiveSessionSaleRequestDTO;
import com.silmaur.shop.dto.response.LiveSessionSaleResponseDTO;
import com.silmaur.shop.model.Customer;
import com.silmaur.shop.model.LiveSessionSale;
import com.silmaur.shop.model.enums.ShippingPreferences;
import com.silmaur.shop.repository.CustomerRepository;
import com.silmaur.shop.repository.LiveSessionRepository;
import com.silmaur.shop.repository.LiveSessionSaleRepository;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.LiveSessionSaleService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiveSessionSaleServiceImpl implements LiveSessionSaleService {

  private final CustomerRepository customerRepository;
  private final LiveSessionRepository liveSessionRepository;
  private final ProductRepository productRepository;
  private final LiveSessionSaleRepository repository;

  @Override
  public Mono<LiveSessionSaleResponseDTO> create(LiveSessionSaleRequestDTO dto) {
    log.info("📦 Venta recibida → sessionId: {}, productId: {}, customerId: {}, quantity: {}, unitPrice: {}, nickname: {}",
        dto.getLiveSessionId(),
        dto.getProductId(),
        dto.getCustomerId(),
        dto.getQuantity(),
        dto.getUnitPrice(),
        dto.getNickname());

    Mono<Customer> customerMono;

    if (dto.getCustomerId() != null) {
      customerMono = customerRepository.findById(dto.getCustomerId());
    } else if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
      customerMono = liveSessionRepository.findById(dto.getLiveSessionId())
          .flatMap(session ->
              customerRepository.findByNicknameAndPlatform(dto.getNickname(), session.getPlatform())
                  .switchIfEmpty(
                      customerRepository.save(
                          Customer.builder()
                              .nickname(dto.getNickname())
                              .platform(session.getPlatform())
                              .initialDeposit(BigDecimal.TEN)
                              .shippingPreference(ShippingPreferences.ACCUMULATE)
                              .remainingDeposit(BigDecimal.ZERO)
                              .createdAt(LocalDateTime.now())
                              .build()
                      )
                  )
          );
    } else {
      return Mono.error(new IllegalArgumentException("Debe seleccionarse un cliente o ingresar un nickname."));
    }

    return customerMono.flatMap(customer -> {
      Long customerId = customer.getId();

      return productRepository.findById(dto.getProductId())
          .flatMap(product -> {
            int stockActual = product.getStock();
            int cantidadVendida = dto.getQuantity();

            if (stockActual < cantidadVendida) {
              return Mono.error(new IllegalArgumentException("Stock insuficiente para realizar la venta."));
            }

            product.setStock(stockActual - cantidadVendida);

            return productRepository.save(product)
                .flatMap(ignore -> {
                  LiveSessionSale sale = new LiveSessionSale();
                  sale.setLiveSessionId(dto.getLiveSessionId());
                  sale.setProductId(dto.getProductId());
                  sale.setCustomerId(customerId);
                  sale.setQuantity(cantidadVendida);
                  sale.setUnitPrice(dto.getUnitPrice());

                  return repository.save(sale)
                      .flatMap(saved -> repository.findById(saved.getId()))
                      .map(loaded -> {
                        LiveSessionSaleResponseDTO response = new LiveSessionSaleResponseDTO();
                        response.setId(loaded.getId());
                        response.setQuantity(loaded.getQuantity());
                        response.setUnitPrice(loaded.getUnitPrice());
                        response.setTotalAmount(loaded.getTotalAmount());
                        response.setCreatedAt(loaded.getCreatedAt());
                        response.setProductName("Producto #" + loaded.getProductId());
                        response.setProductId(loaded.getProductId());
                        response.setCustomerId(loaded.getCustomerId());
                        return response;
                      });
                });
          });
    });
  }

  @Override
  public Flux<LiveSessionSaleResponseDTO> findBySession(Long liveSessionId) {
    return repository.findByLiveSessionId(liveSessionId)
        .flatMap(sale -> {
          Mono<String> customerNameMono;

          if (sale.getCustomerId() != null) {
            customerNameMono = customerRepository.findById(sale.getCustomerId())
                .map(Customer::getNickname)
                .defaultIfEmpty("N/A");
          } else {
            customerNameMono = Mono.just("N/A");
          }

          return customerNameMono.map(customerName -> {
            LiveSessionSaleResponseDTO dto = new LiveSessionSaleResponseDTO();
            dto.setId(sale.getId());
            dto.setProductId(sale.getProductId());
            dto.setQuantity(sale.getQuantity());
            dto.setUnitPrice(sale.getUnitPrice());
            dto.setTotalAmount(sale.getTotalAmount());
            dto.setCreatedAt(sale.getCreatedAt());
            dto.setProductName("Producto #" + sale.getProductId());
            dto.setCustomerId(sale.getCustomerId());
            dto.setCustomerName(customerName);
            return dto;
          });
        });
  }
}
