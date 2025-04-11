package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.OrderCreationDTO;
import com.silmaur.shop.dto.OrderDTO;
import com.silmaur.shop.model.Order;
import java.math.BigDecimal;
import org.mapstruct.*;

/**
 * Mapper para convertir entre las entidades Order y los DTO asociados.
 * Utiliza MapStruct para generación automática de implementación.
 */
@Mapper(componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "totalAmount", source = "totalAmount"),
      @Mapping(target = "status", source = "status"),
      @Mapping(target = "diasSinPagar", source = "diasSinPagar"),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "updatedAt", ignore = true),
      @Mapping(target = "realAmountToPay", ignore = true) // ⚠️ Lo vas a setear en el servicio
  })
  Order toEntity(OrderCreationDTO dto, BigDecimal totalAmount, String status, Integer diasSinPagar);

  @Mappings({
      @Mapping(source = "aperture", target = "apertura"),
      @Mapping(source = "realAmountToPay", target = "realAmountToPay") // ✅ Asegura que se mapee
  })
  OrderDTO toDto(Order order);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "customerId", ignore = true),
      @Mapping(target = "campaignId", ignore = true),
      @Mapping(target = "liveSessionId", ignore = true),
      @Mapping(target = "aperture", ignore = true),
      @Mapping(target = "totalAmount", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "updatedAt", ignore = true),
      @Mapping(target = "diasSinPagar", ignore = true),
      @Mapping(target = "realAmountToPay", ignore = true)
  })
  void updateOrderFromDto(OrderDTO dto, @MappingTarget Order entity);
}
