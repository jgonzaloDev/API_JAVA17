package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.OrderItemDTO;
import com.silmaur.shop.exception.NotFoundException;
import com.silmaur.shop.handler.mapper.OrderItemMapper;
import com.silmaur.shop.model.OrderItem;
import com.silmaur.shop.repository.OrderItemRepository;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

  private final OrderItemRepository orderItemRepository;
  private final OrderItemMapper orderItemMapper;
  private final ProductRepository productRepository;

  @Override
  public Flux<OrderItemDTO> findByOrderId(Long orderId) {
    log.info("Buscando ítems del pedido ID: {}", orderId);
    return orderItemRepository.findAllByOrderId(orderId)
        .map(orderItemMapper::toDto);
  }

  @Override
  public Mono<OrderItemDTO> create(Long orderId, OrderItemDTO dto) {
    log.info("Creando nuevo item para pedido ID: {}", orderId);

    return productRepository.findById(dto.getProductId())
        .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado con ID: " + dto.getProductId())))
        .flatMap(product -> {
          dto.setProductName(product.getName()); // Se asegura que el nombre sea el correcto desde la BD
          OrderItem entity = orderItemMapper.toEntity(orderId, dto);
          return orderItemRepository.save(entity);
        })
        .map(orderItemMapper::toDto);
  }



  @Override
  public Mono<Void> delete(Long orderId, Long itemId) {
    log.info("Eliminando ítem ID: {} del pedido ID: {}", itemId, orderId);

    return orderItemRepository.findById(itemId)
        .switchIfEmpty(Mono.error(new NotFoundException("Item no encontrado con ID: " + itemId)))
        .flatMap(item -> {
          if (!item.getOrderId().equals(orderId)) {
            return Mono.error(new NotFoundException("El ítem no pertenece al pedido especificado"));
          }
          return orderItemRepository.deleteById(itemId);
        });
  }
}
