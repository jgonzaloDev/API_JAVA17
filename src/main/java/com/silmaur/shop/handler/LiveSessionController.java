package com.silmaur.shop.handler;

import com.silmaur.shop.dto.LiveSessionDTO;
import com.silmaur.shop.dto.LiveSessionRequestDTO;
import com.silmaur.shop.dto.LiveSessionSummaryDTO;
import com.silmaur.shop.dto.response.LiveSessionResponseDTO;
import com.silmaur.shop.model.LiveSessionSummary;
import com.silmaur.shop.service.LiveSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/live-sessions")
@RequiredArgsConstructor
public class LiveSessionController {

  private final LiveSessionService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<LiveSessionDTO> create(@RequestBody LiveSessionRequestDTO dto) {
    return service.create(dto);
  }

  @GetMapping
  public Flux<LiveSessionDTO> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<LiveSessionDTO>> findById(@PathVariable Long id) {
    return service.findById(id)
        .map(ResponseEntity::ok);
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
    return service.deleteById(id)
        .thenReturn(ResponseEntity.noContent().build());
  }

  // 🎯 NUEVO: resumen de sesión en vivo
  @GetMapping("/{id}/summary")
  public Mono<ResponseEntity<LiveSessionSummaryDTO>> getSummary(@PathVariable Long id) {
    return service.getSessionSummary(id)
        .map(ResponseEntity::ok);
  }
}
