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

  /** Crear nueva venta en sesión en vivo */
  @PostMapping
  public Mono<LiveSessionSaleResponseDTO> create(@RequestBody LiveSessionSaleRequestDTO dto) {
    return service.create(dto);
  }

  /** Ventas visibles (oculta las asociadas a pedidos COMPLETADOS en la sesión) */
  @GetMapping("/session/{sessionId}")
  public Flux<LiveSessionSaleResponseDTO> list(@PathVariable Long sessionId) {
    return service.findBySession(sessionId);
  }

  /** Histórico completo de ventas (sin filtro) */
  @GetMapping("/history/{sessionId}")
  public Flux<LiveSessionSaleResponseDTO> history(@PathVariable Long sessionId) {
    return service.findAllBySession(sessionId);
  }

  /** Archivar todas las ventas de un cliente en una sesión en vivo */
  @PostMapping("/{sessionId}/archive/{customerId}")
  public Mono<Void> archiveCustomerSales(
      @PathVariable Long sessionId,
      @PathVariable Long customerId
  ) {
    return service.archiveSalesByCustomer(sessionId, customerId);
  }

  /** Eliminar (o cancelar) una venta específica */
  /** Eliminar (o cancelar) una venta específica */
  @DeleteMapping("/sale/{id}")
  public Mono<Void> delete(@PathVariable Long id) {
    System.out.println("🗑️ [CONTROLLER] Solicitud DELETE para venta ID=" + id);
    return service.deleteSale(id)
        .doOnSubscribe(sub -> System.out.println("➡️ [SERVICE] Eliminando venta id=" + id))
        .doOnSuccess(v -> System.out.println("✅ [SERVICE] Venta eliminada correctamente id=" + id))
        .doOnError(err -> System.err.println("❌ [SERVICE] Error eliminando venta id=" + id + " -> " + err.getMessage()));
  }



}
