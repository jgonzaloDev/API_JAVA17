package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.LiveSessionDTO;
import com.silmaur.shop.dto.LiveSessionRequestDTO;
import com.silmaur.shop.dto.LiveSessionSummaryDTO;
import com.silmaur.shop.exception.LiveSessionNotFoundException;
import com.silmaur.shop.handler.mapper.LiveSessionMapper;
import com.silmaur.shop.model.LiveSession;
import com.silmaur.shop.repository.CustomLiveSessionRepository;
import com.silmaur.shop.repository.LiveSessionRepository;
import com.silmaur.shop.service.LiveSessionService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveSessionServiceImpl implements LiveSessionService {

  private final LiveSessionRepository repository;
  private final CustomLiveSessionRepository customRepository;
  private final LiveSessionMapper mapper;

  /**
   * 📄 Lista todas las sesiones en vivo existentes.
   */
  @Override
  public Flux<LiveSessionDTO> findAll() {
    log.debug("🗂️ Listando todas las sesiones en vivo");
    return repository.findAll()
        .map(mapper::toSessionDto); // Usamos LiveSessionDTO directo (con status dinámico)
  }

  /**
   * 🆕 Crea una nueva sesión en vivo.
   */
  @Override
  public Mono<LiveSessionDTO> create(LiveSessionRequestDTO dto) {
    log.info("📥 Creando nueva sesión en vivo: {}", dto.getTitle());

    LiveSession entity = mapper.toEntity(dto);

    // Asignar fecha de creación automáticamente
    entity.setCreatedAt(LocalDateTime.now());

    // Validar que endTime > startTime
    if (entity.getEndTime() != null && entity.getStartTime() != null &&
        !entity.getEndTime().isAfter(entity.getStartTime())) {
      return Mono.error(new IllegalArgumentException("La hora de fin debe ser mayor que la de inicio"));
    }

    return repository.save(entity)
        .doOnSuccess(saved -> log.info("✅ Sesión creada con ID {}", saved.getId()))
        .map(mapper::toSessionDto);
  }

  /**
   * 🔍 Busca una sesión en vivo por su ID.
   */
  @Override
  public Mono<LiveSessionDTO> findById(Long id) {
    log.debug("🔍 Buscando sesión por ID: {}", id);
    return repository.findById(id)
        .map(mapper::toSessionDto);
  }

  /**
   * 🗑️ Elimina una sesión en vivo por su ID.
   */
  @Override
  public Mono<Void> deleteById(Long id) {
    log.warn("⚠️ Eliminando sesión con ID {}", id);
    return repository.deleteById(id);
  }

  /**
   * 📊 Obtiene un resumen completo de una sesión en vivo.
   */
  @Override
  public Mono<LiveSessionSummaryDTO> getSessionSummary(Long sessionId) {
    log.info("🔍 Buscando resumen de la sesión con ID {}", sessionId);

    return repository.findById(sessionId)
        .switchIfEmpty(Mono.error(new LiveSessionNotFoundException(
            "Sesión no encontrada con ID: " + sessionId)))
        .flatMap(session ->
            customRepository.getSessionSummary(sessionId)
                .map(summary -> {
                  log.debug("📦 Datos brutos del resumen: {}", summary);
                  return mapper.toSummaryDto(summary, session.getTitle());
                })
        )
        .doOnSuccess(summary -> log.info("✅ Resumen generado exitosamente para sesión {}", sessionId))
        .doOnError(error -> log.error("❌ Error al generar el resumen de la sesión {}", sessionId, error));
  }
}
