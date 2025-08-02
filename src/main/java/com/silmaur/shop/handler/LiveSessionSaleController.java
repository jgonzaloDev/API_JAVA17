package com.silmaur.shop.handler;


import com.silmaur.shop.dto.LiveSessionSaleRequestDTO;
import com.silmaur.shop.dto.response.LiveSessionSaleResponseDTO;
import com.silmaur.shop.service.LiveSessionSaleService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/live-session-sales")
public class LiveSessionSaleController {

  private final LiveSessionSaleService service;

  public LiveSessionSaleController(LiveSessionSaleService service) {
    this.service = service;
  }

  @PostMapping
  public Mono<LiveSessionSaleResponseDTO> create(@RequestBody LiveSessionSaleRequestDTO dto) {
    return service.create(dto);
  }

  @GetMapping("/{sessionId}")
  public Flux<LiveSessionSaleResponseDTO> list(@PathVariable Long sessionId) {
    return service.findBySession(sessionId);
  }
}
