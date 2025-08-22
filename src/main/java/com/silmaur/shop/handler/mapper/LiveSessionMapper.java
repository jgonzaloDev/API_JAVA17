package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.LiveSessionDTO;
import com.silmaur.shop.dto.LiveSessionRequestDTO;
import com.silmaur.shop.dto.LiveSessionSummaryDTO;
import com.silmaur.shop.dto.response.LiveSessionResponseDTO;
import com.silmaur.shop.model.LiveSession;
import com.silmaur.shop.model.LiveSessionSummary;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LiveSessionMapper {

  // Crear entidad desde request (id/createdAt/status los maneja el backend)
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "status", ignore = true)
  })
  LiveSession toEntity(LiveSessionRequestDTO dto);

  // Mapper “clásico” si lo usas en otros endpoints
  LiveSessionResponseDTO toDto(LiveSession entity);

  // Actualización parcial (no permitir tocar id/createdAt/status desde el DTO)
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "status", ignore = true)
  })
  void updateSessionFromDTO(LiveSessionRequestDTO dto, @MappingTarget LiveSession entity);

  // Enviar al front el status real guardado en BD
  @Mapping(target = "status", source = "status")
  LiveSessionDTO toSessionDto(LiveSession entity);

  // Resumen de sesión
  @Mapping(target = "sessionTitle", source = "sessionTitle")
  LiveSessionSummaryDTO toSummaryDto(LiveSessionSummary summary, @Context String sessionTitle);
}
