package com.silmaur.shop.handler;

import com.silmaur.shop.dto.OrderItemDTO;
import com.silmaur.shop.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemController {

  private final OrderItemService orderItemService;

  @GetMapping
  public Flux<OrderItemDTO> getItemsByOrderId(@PathVariable Long orderId) {
    return orderItemService.findByOrderId(orderId);
  }

  @PostMapping
  public Mono<ResponseEntity<OrderItemDTO>> createItem(
      @PathVariable Long orderId,
      @RequestBody OrderItemDTO dto
  ) {
    return orderItemService.create(orderId, dto)
        .map(item -> ResponseEntity.status(HttpStatus.CREATED).body(item));
  }

  @DeleteMapping("/{itemId}")
  public Mono<ResponseEntity<Void>> deleteItem(
      @PathVariable Long orderId,
      @PathVariable Long itemId
  ) {
    return orderItemService.delete(orderId, itemId)
        .thenReturn(ResponseEntity.noContent().build());
  }
}
