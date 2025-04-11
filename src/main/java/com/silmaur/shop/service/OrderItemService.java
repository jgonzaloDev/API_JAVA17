package com.silmaur.shop.service;

import com.silmaur.shop.dto.OrderItemDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Servicio para gestionar los ítems de un pedido (OrderItem).
 */
public interface OrderItemService {

  Flux<OrderItemDTO> findByOrderId(Long orderId);

  Mono<OrderItemDTO> create(Long orderId, OrderItemDTO orderItemDTO);

  /**
   * Elimina un ítem específico de un pedido.
   *
   * @param orderId ID del pedido asociado.
   * @param itemId ID del ítem a eliminar.
   * @return Mono vacío si se eliminó correctamente.
   */
  Mono<Void> delete(Long orderId, Long itemId);
}
