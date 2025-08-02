package com.silmaur.shop.handler;

import com.silmaur.shop.dto.DraftOrderDTO;
import com.silmaur.shop.dto.OrderDTO;
import com.silmaur.shop.dto.Response;
import com.silmaur.shop.handler.mapper.DraftOrderMapper;
import com.silmaur.shop.service.DraftOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/draft-orders")
@RequiredArgsConstructor
@Slf4j
public class DraftOrderController {

  private final DraftOrderService draftOrderService;
  private final DraftOrderMapper draftOrderMapper;

  @PostMapping
  public Mono<ResponseEntity<Response<DraftOrderDTO>>> createDraftOrder(@RequestBody DraftOrderDTO draftOrderDTO) {
    return draftOrderService.createDraftOrder(draftOrderDTO)
        .map(order -> ResponseEntity.ok(Response.success("Pedido en borrador creado", draftOrderMapper.toDto(order))));
  }

  @GetMapping("/session/{sessionId}")
  public Flux<DraftOrderDTO> getDraftOrdersBySession(@PathVariable Long sessionId) {
    return draftOrderService.findByLiveSessionId(sessionId);
  }


  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteDraftOrder(@PathVariable Long id) {
    return draftOrderService.deleteById(id)
        .thenReturn(ResponseEntity.noContent().build());
  }


  @PutMapping("/{id}/status")
  public Mono<ResponseEntity<Response<String>>> updateStatus(
      @PathVariable Long id,
      @RequestParam String status
  ) {
    return draftOrderService.updateStatus(id, status)
        .thenReturn(ResponseEntity.ok(Response.success("Estado actualizado correctamente", null)));
  }


  @PostMapping("/{id}/confirm")
  public Mono<ResponseEntity<Response<OrderDTO>>> confirmDraftOrder(@PathVariable Long id) {
    return draftOrderService.confirmDraftOrder(id)
        .map(order -> ResponseEntity.ok(Response.success("Pedido confirmado", order)));
  }

  @PatchMapping("/{id}")
  public Mono<ResponseEntity<Response<DraftOrderDTO>>> updateDraftOrder(
      @PathVariable Long id,
      @RequestBody DraftOrderDTO draftOrderDTO
  ) {
    return draftOrderService.updateDraftOrder(id, draftOrderDTO)
        .map(updated -> ResponseEntity.ok(Response.success("Pedido actualizado correctamente", draftOrderMapper.toDto(updated))));
  }



}
