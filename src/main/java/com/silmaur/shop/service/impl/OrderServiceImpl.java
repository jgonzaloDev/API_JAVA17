package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.*;
import com.silmaur.shop.exception.InsufficientStockException;
import com.silmaur.shop.exception.NotFoundException;
import com.silmaur.shop.exception.ProductNotFoundException;
import com.silmaur.shop.handler.mapper.OrderItemMapper;
import com.silmaur.shop.handler.mapper.OrderMapper;
import com.silmaur.shop.model.Order;
import com.silmaur.shop.model.OrderItem;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.*;
import com.silmaur.shop.service.OrderService;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final OrderItemRepository orderItemRepository;

  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;

  @Override
  @Transactional
  public Mono<OrderDTO> createOrder(OrderCreationDTO dto) {
    log.info("Iniciando creación de pedido para cliente ID: {}", dto.getCustomerId());

    return customerRepository.findById(dto.getCustomerId())
        .switchIfEmpty(Mono.error(new NotFoundException("Cliente no encontrado con ID: " + dto.getCustomerId())))
        .flatMap(customer -> orderRepository.countByCustomerId(customer.getId())
            .flatMap(orderCount -> {
              boolean isFirstOrder = orderCount == 0;

              // Si es el primer pedido, aplicamos la apertura como saldo disponible
              if (isFirstOrder) {
                customer.setRemainingDeposit(customer.getInitialDeposit());
              }

              BigDecimal saldoDisponible = Optional.ofNullable(customer.getRemainingDeposit()).orElse(BigDecimal.ZERO);

              List<Long> productIds = dto.getItems().stream()
                  .map(OrderItemCreationDTO::getProductId)
                  .distinct()
                  .toList();

              return productRepository.findAllById(productIds)
                  .collectList()
                  .flatMap(products -> {
                    if (products.size() != productIds.size()) {
                      return Mono.error(new IllegalArgumentException("Uno o más productos no existen."));
                    }

                    Map<Long, Product> productMap = products.stream()
                        .collect(Collectors.toMap(Product::getId, p -> p));

                    BigDecimal totalPedido = calcularTotal(dto.getItems(), productMap);

                    // ⚠️ Lo que realmente debe pagar el cliente (descontando saldo disponible)
                    BigDecimal realAmountToPay = totalPedido.subtract(saldoDisponible).max(BigDecimal.ZERO);

                    // 💰 Si sobra saldo, se actualiza como nuevo saldo a favor
                    BigDecimal nuevoSaldoAFavor = saldoDisponible.subtract(totalPedido);

                    String estado = realAmountToPay.compareTo(BigDecimal.ZERO) == 0 ? "PAGADO" : "NO_PAGADO";

                    LocalDateTime paymentDueDate = Optional.ofNullable(dto.getPaymentDueDate())
                        .orElse(LocalDateTime.now().plusDays(7));
                    int diasSinPagar = calcularDiasSinPagar(paymentDueDate);

                    Order order = orderMapper.toEntity(dto, totalPedido, estado, diasSinPagar);
                    order.setAperture(isFirstOrder ? customer.getInitialDeposit() : BigDecimal.ZERO);
                    order.setRealAmountToPay(realAmountToPay); // ✅ NUEVO CAMPO
                    order.setPaymentDueDate(paymentDueDate);
                    order.setCreatedAt(LocalDateTime.now());
                    order.setUpdatedAt(LocalDateTime.now());

                    return orderRepository.save(order).flatMap(savedOrder -> {
                      List<OrderItem> items = dto.getItems().stream().map(item -> {
                        Product product = productMap.get(item.getProductId());
                        validarStock(item, product);

                        OrderItem entity = orderItemMapper.toEntity(savedOrder.getId(), item);
                        entity.setProductName(product.getName());
                        entity.setPrice(product.getSalePrice());
                        return entity;
                      }).toList();

                      // ✅ Actualizamos el saldo del cliente
                      customer.setRemainingDeposit(nuevoSaldoAFavor.compareTo(BigDecimal.ZERO) > 0
                          ? nuevoSaldoAFavor
                          : BigDecimal.ZERO);

                      return customerRepository.save(customer)
                          .then(productRepository.saveAll(productMap.values()).then())
                          .then(orderItemRepository.saveAll(items).collectList())
                          .map(savedItems -> {
                            OrderDTO response = orderMapper.toDto(savedOrder);
                            response.setApertura(order.getAperture());
                            response.setItems(savedItems.stream().map(orderItemMapper::toDto).toList());
                            return response;
                          });
                    });
                  });
            }));
  }










  private BigDecimal calcularTotal(List<OrderItemCreationDTO> items, Map<Long, Product> productMap) {
    return items.stream()
        .map(item -> {
          Product product = productMap.get(item.getProductId());
          return product.getSalePrice()
              .multiply(BigDecimal.valueOf(item.getQuantity()))
              .subtract(Optional.ofNullable(item.getDiscount()).orElse(BigDecimal.ZERO));
        })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void validarStock(OrderItemCreationDTO item, Product product) {
    int newStock = product.getStock() - item.getQuantity();
    if (newStock < 0) {
      throw new InsufficientStockException("No hay suficiente stock para el producto: " + product.getName());
    }
    product.setStock(newStock);

    if (newStock <= product.getMinStock()) {
      log.warn("⚠️ Producto con stock bajo: {} (stock actual: {}, mínimo permitido: {})",
          product.getName(), newStock, product.getMinStock());
    }
  }

  private int calcularDiasSinPagar(LocalDateTime fechaLimite) {
    return fechaLimite == null ? 0 :
        (int) Math.max(ChronoUnit.DAYS.between(fechaLimite.toLocalDate(), LocalDate.now()), 0);
  }


  @Override
  public Mono<OrderDTO> getOrderById(Long id) {
    return orderRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Pedido no encontrado con ID: " + id)))
        .flatMap(order ->
            orderItemRepository.findAllByOrderId(order.getId())
                .collectList()
                .map(items -> {
                  OrderDTO dto = orderMapper.toDto(order);
                  dto.setItems(items.stream().map(orderItemMapper::toDto).toList());
                  return dto;
                }));
  }

  @Override
  public Flux<OrderDTO> getOrdersByCustomerId(Long customerId) {
    return orderRepository.findByCustomerId(customerId)
        .flatMap(order ->
            orderItemRepository.findAllByOrderId(order.getId())
                .collectList()
                .map(items -> {
                  OrderDTO dto = orderMapper.toDto(order);
                  dto.setItems(items.stream().map(orderItemMapper::toDto).toList());
                  return dto;
                }));
  }

  @Override
  public Mono<OrderDTO> updateOrderStatus(Long id, StatusUpdateDTO statusDto) {
    return orderRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Pedido no encontrado con ID: " + id)))
        .flatMap(order -> {
          order.setStatus(statusDto.getStatus());
          order.setUpdatedAt(LocalDateTime.now());

          if (order.getPaymentDueDate() != null) {
            long dias = ChronoUnit.DAYS.between(order.getPaymentDueDate().toLocalDate(), LocalDate.now());
            order.setDiasSinPagar((int) Math.max(dias, 0));
          }

          return orderRepository.save(order);
        })
        .flatMap(savedOrder ->
            orderItemRepository.findAllByOrderId(savedOrder.getId())
                .collectList()
                .map(items -> {
                  OrderDTO dto = orderMapper.toDto(savedOrder);
                  dto.setItems(items.stream().map(orderItemMapper::toDto).toList());
                  return dto;
                }));
  }

  @Override
  public Flux<OrderDTO> getAllOrdersFiltered(Long customerId, String status) {
    return orderRepository.findAll()
        .filter(order -> customerId == null || order.getCustomerId().equals(customerId))
        .filter(order -> status == null || order.getStatus().equalsIgnoreCase(status))
        .flatMap(order ->
            orderItemRepository.findAllByOrderId(order.getId())
                .collectList()
                .map(items -> {
                  OrderDTO dto = orderMapper.toDto(order);
                  dto.setItems(items.stream().map(orderItemMapper::toDto).toList());
                  return dto;
                }));
  }

  @Override
  public Mono<byte[]> exportOrdersToCsv(Long customerId, String status) {
    return orderRepository.findAll()
        .filter(order -> customerId == null || order.getCustomerId().equals(customerId))
        .filter(order -> status == null || order.getStatus().equalsIgnoreCase(status))
        .flatMap(order ->
            orderItemRepository.findAllByOrderId(order.getId())
                .collectList()
                .map(items -> {
                  OrderDTO dto = orderMapper.toDto(order);
                  dto.setItems(items.stream().map(orderItemMapper::toDto).toList());
                  return dto;
                }))
        .collectList()
        .flatMap(orderDtos -> {
          // Extraer los IDs de clientes únicos
          List<Long> customerIds = orderDtos.stream()
              .map(OrderDTO::getCustomerId)
              .distinct()
              .toList();

          // Buscar nombres de los clientes
          return customerRepository.findAllById(customerIds)
              .collectMap(customer -> customer.getId(), customer -> customer.getName())
              .map(customerNameMap -> generateCsvFromOrders(orderDtos, customerNameMap));
        });
  }


  private byte[] generateCsvFromOrders(List<OrderDTO> orders, Map<Long, String> customerNameMap) {
    StringBuilder sb = new StringBuilder();

    // Agrega BOM al inicio para que Excel lo reconozca como UTF-8
    sb.append('\uFEFF');

    sb.append("ID,Cliente,CampañaID,SesiónEnVivoID,Apertura,Total,Estado,Acumulación,FechaPago,FechaCreación\n");

    for (OrderDTO order : orders) {
      String customerName = customerNameMap.get(order.getCustomerId());
      sb.append(order.getId()).append(",")
          .append(customerName).append(",")
          .append(order.getCampaignId() != null ? order.getCampaignId() : "").append(",")
          .append(order.getLiveSessionId() != null ? order.getLiveSessionId() : "").append(",")
          .append(order.getApertura()).append(",")
          .append(order.getTotalAmount()).append(",")
          .append(order.getStatus()).append(",")
          .append(order.getAccumulation()).append(",")
          .append(order.getPaymentDueDate() != null ? order.getPaymentDueDate() : "").append(",")
          .append(order.getCreatedAt()).append("\n");
    }

    return sb.toString().getBytes(StandardCharsets.UTF_8);
  }
}