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

  // ya existentes
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true)
  })
  LiveSession toEntity(LiveSessionRequestDTO dto);

  LiveSessionResponseDTO toDto(LiveSession entity);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true)
  })
  void updateSessionFromDTO(LiveSessionRequestDTO dto, @MappingTarget LiveSession entity);

  LiveSessionDTO toSessionDto(LiveSession entity); // ✅ agrega esta línea

  // 🎯 NUEVO: convertir modelo + título a DTO resumen
  @Mapping(target = "sessionTitle", source = "sessionTitle")
  LiveSessionSummaryDTO toSummaryDto(LiveSessionSummary summary, @Context String sessionTitle);
}
