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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    log.info("📦 Venta recibida → sessionId: {}, productId: {}, customerId: {}, quantity: {}, unitPrice: {}, nickname: {}, initialDeposit: {}",
        dto.getLiveSessionId(),
        dto.getProductId(),
        dto.getCustomerId(),
        dto.getQuantity(),
        dto.getUnitPrice(),
        dto.getNickname(),
        dto.getInitialDeposit());

    Mono<Customer> customerMono;

    if (dto.getCustomerId() != null) {
      // 🔹 Cliente ya existe → lo buscamos
      customerMono = customerRepository.findById(dto.getCustomerId());

    } else if (dto.getNickname() != null && !dto.getNickname().isBlank()) {
      // 🔹 Crear cliente rápido si no existe
      customerMono = liveSessionRepository.findById(dto.getLiveSessionId())
          .flatMap(session ->
              customerRepository.findByNicknameAndPlatform(dto.getNickname(), session.getPlatform())
                  .switchIfEmpty(
                      Mono.defer(() -> {
                        BigDecimal depositoInicial = dto.getInitialDeposit() != null
                            ? dto.getInitialDeposit()
                            : BigDecimal.TEN;

                        log.info("🆕 Creando cliente rápido '{}' en plataforma {} con depósito inicial {}",
                            dto.getNickname(), session.getPlatform(), depositoInicial);

                        return customerRepository.save(
                            Customer.builder()
                                .nickname(dto.getNickname())
                                .platform(session.getPlatform())
                                .initialDeposit(depositoInicial)
                                .remainingDeposit(depositoInicial) // ✅ saldo = depósito inicial
                                .shippingPreference(ShippingPreferences.ACCUMULATE)
                                .createdAt(LocalDateTime.now())
                                .build()
                        );
                      })
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
                  // 🔹 Calcular el total de la venta
                  BigDecimal totalVenta = dto.getUnitPrice().multiply(BigDecimal.valueOf(cantidadVendida));

                  // 🔹 Aplicar saldo del cliente
                  BigDecimal saldoDisponible = customer.getRemainingDeposit() != null
                      ? customer.getRemainingDeposit()
                      : BigDecimal.ZERO;
                  BigDecimal saldoAplicado = saldoDisponible.min(totalVenta); // solo hasta donde alcance
                  BigDecimal montoAPagar = totalVenta.subtract(saldoAplicado);
                  BigDecimal nuevoSaldo = saldoDisponible.subtract(saldoAplicado);

                  log.info("💰 Cliente {} → Total venta: {}, Saldo disponible: {}, Saldo aplicado: {}, A pagar: {}, Nuevo saldo: {}",
                      customer.getNickname(), totalVenta, saldoDisponible, saldoAplicado, montoAPagar, nuevoSaldo);

                  // 🔹 Crear la venta
                  LiveSessionSale sale = new LiveSessionSale();
                  sale.setLiveSessionId(dto.getLiveSessionId());
                  sale.setProductId(dto.getProductId());
                  sale.setCustomerId(customerId);
                  sale.setQuantity(cantidadVendida);
                  sale.setUnitPrice(dto.getUnitPrice());
                  sale.setArchived(false);
                  sale.setTotalAmount(totalVenta);       // Total bruto
                  sale.setRealAmountToPay(montoAPagar);  // ✅ lo que realmente debe pagar

                  return repository.save(sale)
                      .flatMap(saved -> {
                        // 🔹 Actualizar saldo del cliente
                        customer.setRemainingDeposit(nuevoSaldo);

                        return customerRepository.save(customer)
                            .then(repository.findById(saved.getId()))
                            .flatMap(loaded ->
                                // 🔹 Enriquecer con datos reales de producto y cliente
                                Mono.zip(
                                    productRepository.findById(loaded.getProductId())
                                        .map(p -> p.getName())
                                        .defaultIfEmpty("Producto N/A"),
                                    customerRepository.findById(loaded.getCustomerId())
                                        .map(c -> c.getNickname())
                                        .defaultIfEmpty("Cliente N/A")
                                ).map(tuple -> {
                                  LiveSessionSaleResponseDTO response = new LiveSessionSaleResponseDTO();
                                  response.setId(loaded.getId());
                                  response.setQuantity(loaded.getQuantity());
                                  response.setUnitPrice(loaded.getUnitPrice());
                                  response.setTotalAmount(loaded.getTotalAmount());
                                  response.setRealAmountToPay(loaded.getRealAmountToPay());
                                  response.setCreatedAt(loaded.getCreatedAt());
                                  response.setProductName(tuple.getT1()); // ✅ nombre real producto
                                  response.setProductId(loaded.getProductId());
                                  response.setCustomerId(loaded.getCustomerId());
                                  response.setCustomerName(tuple.getT2()); // ✅ nickname real cliente
                                  return response;
                                })
                            );
                      });
                });
          });
    });
  }




  @Override
  public Flux<LiveSessionSaleResponseDTO> findBySession(Long liveSessionId) {
    // 🔹 Ventas visibles (no archivadas)
    return repository.findByLiveSessionIdAndArchivedFalse(liveSessionId)
        .flatMap(this::mapToResponse);
  }

  @Override
  public Flux<LiveSessionSaleResponseDTO> findAllBySession(Long liveSessionId) {
    return repository.findByLiveSessionId(liveSessionId)
        .flatMap(sale -> {
          log.info("🔍 Histórico → ID: {}, ProdID: {}, ClienteID: {}",
              sale.getId(), sale.getProductId(), sale.getCustomerId()); // si tienes getter
          return mapToResponse(sale);
        });
  }


  @Override
  public Mono<Void> archiveSalesByCustomer(Long liveSessionId, Long customerId) {
    log.info("📦 Archivando ventas del cliente {} en la sesión {}", customerId, liveSessionId);

    return repository.findByLiveSessionIdAndCustomerId(liveSessionId, customerId)
        .collectList()
        .flatMapMany(sales -> {
          if (sales.isEmpty()) {
            log.warn("⚠️ No se encontraron ventas para cliente {} en sesión {}", customerId, liveSessionId);
          } else {
            log.info("📝 Ventas encontradas para archivar ({}): {}",
                sales.size(),
                sales.stream().map(s -> s.getId()).toList());
          }

          return Flux.fromIterable(sales)
              .flatMap(sale -> {
                sale.setArchived(true);
                log.debug("✅ Marcando venta {} como archivada", sale.getId());
                return repository.save(sale)
                    .doOnSuccess(saved -> log.debug("💾 Venta {} guardada como archivada", saved.getId()));
              });
        })
        .then()
        .doOnSuccess(v -> log.info("🎉 Ventas archivadas correctamente para cliente {} en sesión {}", customerId, liveSessionId))
        .doOnError(err -> log.error("❌ Error al archivar ventas del cliente {} en sesión {}", customerId, liveSessionId, err));
  }

  @Override
  public Mono<Void> deleteSale(Long id) {
    System.out.println("🛠️ [SERVICE-IMPL] Intentando eliminar venta id=" + id);
    return repository.findById(id)
        .doOnNext(sale -> System.out.println("🔍 [DB] Venta encontrada: " + sale))
        .switchIfEmpty(Mono.defer(() -> {
          System.err.println("⚠️ [DB] Venta no encontrada con id=" + id);
          return Mono.error(new RuntimeException("Venta no encontrada con id=" + id));
        }))
        .flatMap(sale -> repository.delete(sale)
            .doOnSuccess(v -> System.out.println("🗑️ [DB] Venta eliminada con id=" + id))
        );
  }




  private Mono<LiveSessionSaleResponseDTO> mapToResponse(LiveSessionSale sale) {
    Mono<String> customerNameMono = Mono.just("N/A");
    if (sale.getCustomerId() != null) {
      customerNameMono = customerRepository.findById(sale.getCustomerId())
          .map(Customer::getNickname)
          .defaultIfEmpty("N/A");
    }

    // 🔹 Cargar nombre real del producto
    Mono<String> productNameMono = Mono.just("Producto N/A");
    if (sale.getProductId() != null) {
      productNameMono = productRepository.findById(sale.getProductId())
          .map(p -> p.getName() != null ? p.getName() : "Producto N/A")
          .defaultIfEmpty("Producto N/A");
    }

    return Mono.zip(customerNameMono, productNameMono)
        .map(tuple -> {
          LiveSessionSaleResponseDTO dto = new LiveSessionSaleResponseDTO();
          dto.setId(sale.getId());
          dto.setProductId(sale.getProductId());
          dto.setQuantity(sale.getQuantity());
          dto.setUnitPrice(sale.getUnitPrice());
          dto.setTotalAmount(sale.getTotalAmount());
          dto.setCreatedAt(sale.getCreatedAt());
          dto.setCustomerId(sale.getCustomerId());
          dto.setCustomerName(tuple.getT1()); // nombre cliente
          dto.setProductName(tuple.getT2());  // nombre producto
          return dto;
        });
  }

}
