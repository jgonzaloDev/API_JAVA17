package com.silmaur.shop.handler;

import com.silmaur.shop.dto.OrderCreationDTO;
import com.silmaur.shop.dto.OrderDTO;
import com.silmaur.shop.dto.StatusUpdateDTO;
import com.silmaur.shop.dto.Response;
import com.silmaur.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  /**
   * Crear una nueva orden de pedido.
   */
  @PostMapping
  public Mono<ResponseEntity<Response<OrderDTO>>> createOrder(@RequestBody OrderCreationDTO dto) {
    return orderService.createOrder(dto)
        .map(order -> ResponseEntity
            .status(201)
            .body(Response.success("Pedido creado correctamente", order)));
  }

  /**
   * Obtener un pedido por su ID.
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<Response<OrderDTO>>> getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id)
        .map(order -> ResponseEntity.ok(Response.success("Pedido encontrado", order)));
  }

  /**
   * Listar pedidos por ID de cliente.
   */
  @GetMapping("/customer/{customerId}")
  public Flux<OrderDTO> getOrdersByCustomer(@PathVariable Long customerId) {
    return orderService.getOrdersByCustomerId(customerId);
  }

  /**
   * Actualizar el estado de un pedido.
   */
  @PatchMapping("/{id}/status")
  public Mono<ResponseEntity<Response<OrderDTO>>> updateOrderStatus(@PathVariable Long id,
      @RequestBody StatusUpdateDTO statusDto) {
    return orderService.updateOrderStatus(id, statusDto)
        .map(order -> ResponseEntity.ok(Response.success("Estado actualizado", order)));
  }

  @GetMapping
  public Flux<OrderDTO> getAllOrders(
      @RequestParam(required = false) Long customerId,
      @RequestParam(required = false) String status
  ) {
    return orderService.getAllOrdersFiltered(customerId, status);
  }


  @GetMapping("/export")
  public Mono<ResponseEntity<byte[]>> exportOrdersToCsv(
      @RequestParam(required = false) Long customerId,
      @RequestParam(required = false) String status) {

    return orderService.exportOrdersToCsv(customerId, status)
        .map(csvBytes -> ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"orders.csv\"")
            .contentType(MediaType.TEXT_PLAIN)
            .body(csvBytes));
  }






}
