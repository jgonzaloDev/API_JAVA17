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

  /** 📄 Lista todas las sesiones en vivo */
  @Override
  public Flux<LiveSessionDTO> findAll() {
    log.debug("🗂️ Listando todas las sesiones en vivo");
    return repository.findAll()
        .map(this::calculateStatus)   // recalcula estado dinámicamente
        .map(mapper::toSessionDto);
  }

  /** 🆕 Crear nueva sesión */
  /** 🆕 Crear nueva sesión */
  @Override
  public Mono<LiveSessionDTO> create(LiveSessionRequestDTO dto) {
    log.info("📥 Creando nueva sesión en vivo: {}", dto.getTitle());

    // 🛠 Paso 1: loguear lo que llega del FRONT
    log.debug(">>> DTO startTime={}, endTime={}", dto.getStartTime(), dto.getEndTime());

    LiveSession entity = mapper.toEntity(dto);
    entity.setCreatedAt(LocalDateTime.now());

    // 🛠 Paso 2: loguear cómo quedó la ENTITY
    log.debug(">>> ENTITY startTime={}, endTime={}", entity.getStartTime(), entity.getEndTime());
    log.debug(">>> NOW (backend)={}", LocalDateTime.now());

    // Validar que endTime > startTime si existe
    if (entity.getEndTime() != null && entity.getStartTime() != null &&
        !entity.getEndTime().isAfter(entity.getStartTime())) {
      return Mono.error(new IllegalArgumentException("La hora de fin debe ser mayor que la de inicio"));
    }

    // Calculamos status antes de guardar
    calculateStatus(entity);

    return repository.save(entity)
        .doOnSuccess(saved -> log.info("✅ Sesión creada con ID {} [{} - {}]",
            saved.getId(), saved.getStartTime(), saved.getEndTime()))
        .map(mapper::toSessionDto);
  }


  /** 🔍 Buscar sesión por ID */
  @Override
  public Mono<LiveSessionDTO> findById(Long id) {
    log.debug("🔍 Buscando sesión por ID: {}", id);

    return repository.findById(id)
        .switchIfEmpty(Mono.error(new LiveSessionNotFoundException(
            "Sesión no encontrada con ID: " + id)))
        .map(session -> {
          LocalDateTime now = LocalDateTime.now();

          if (session.getStartTime() == null) {
            session.setStatus("PROGRAMADA");
          } else if (now.isBefore(session.getStartTime())) {
            session.setStatus("PROGRAMADA");
          } else if (session.getEndTime() != null && now.isAfter(session.getEndTime())) {
            session.setStatus("FINALIZADA");
          } else {
            session.setStatus("ACTIVA");
          }

          return mapper.toSessionDto(session);
        });
  }


  /** 🗑️ Eliminar sesión por ID */
  @Override
  public Mono<Void> deleteById(Long id) {
    log.warn("⚠️ Eliminando sesión con ID {}", id);
    return repository.deleteById(id);
  }

  /** 📊 Obtener resumen de la sesión */
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
        .doOnSuccess(summary -> log.info("✅ Resumen generado para sesión {}", sessionId))
        .doOnError(error -> log.error("❌ Error al generar resumen de sesión {}", sessionId, error));
  }

  /** 🛑 Finalizar sesión (marca endTime y status FINALIZADA) */
  @Override
  public Mono<Void> finalizeSession(Long id) {
    log.info("🛑 Finalizando sesión con ID {}", id);

    return repository.findById(id)
        .switchIfEmpty(Mono.error(new LiveSessionNotFoundException(
            "Sesión no encontrada con ID: " + id)))
        .flatMap(session -> {
          session.setEndTime(LocalDateTime.now());
          session.setStatus("FINALIZADA");
          return repository.save(session)
              .doOnSuccess(saved -> log.info("✅ Sesión {} finalizada", saved.getId()))
              .doOnError(err -> log.error("❌ Error al finalizar sesión {}", id, err));
        })
        .then();
  }

  /** 🔑 Helper para calcular estado dinámicamente */
  private LiveSession calculateStatus(LiveSession entity) {
    LocalDateTime now = LocalDateTime.now();

    if (entity.getStartTime() == null) {
      entity.setStatus("PROGRAMADA");
    } else if (now.isBefore(entity.getStartTime())) {
      entity.setStatus("PROGRAMADA");
    } else if (entity.getEndTime() != null && now.isAfter(entity.getEndTime())) {
      entity.setStatus("FINALIZADA");
    } else {
      entity.setStatus("ACTIVA");
    }
    return entity;
  }
}

