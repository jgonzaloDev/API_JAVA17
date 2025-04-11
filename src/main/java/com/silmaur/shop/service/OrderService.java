package com.silmaur.shop.service;

import com.silmaur.shop.dto.OrderCreationDTO;
import com.silmaur.shop.dto.OrderDTO;
import com.silmaur.shop.dto.StatusUpdateDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Define las operaciones del negocio relacionadas con pedidos (orders).
 */
public interface OrderService {

  /**
   * Crea un nuevo pedido a partir del DTO de creación.
   *
   * @param dto DTO con la información para crear el pedido.
   * @return El pedido creado.
   */
  Mono<OrderDTO> createOrder(OrderCreationDTO dto);

  /**
   * Obtiene un pedido por su ID.
   *
   * @param id Identificador del pedido.
   * @return Pedido correspondiente.
   */
  Mono<OrderDTO> getOrderById(Long id);

  /**
   * Lista los pedidos realizados por un cliente específico.
   *
   * @param customerId ID del cliente.
   * @return Lista de pedidos del cliente.
   */
  Flux<OrderDTO> getOrdersByCustomerId(Long customerId);

  /**
   * Actualiza el estado de un pedido (por ejemplo, de NO_PAGADO a PAGADO).
   *
   * @param id     ID del pedido.
   * @param status Nuevo estado.
   * @return Pedido actualizado.
   */
  Mono<OrderDTO> updateOrderStatus(Long id, StatusUpdateDTO status);


  Flux<OrderDTO> getAllOrdersFiltered(Long customerId, String status);


  Mono<byte[]> exportOrdersToCsv(Long customerId, String status);




}
