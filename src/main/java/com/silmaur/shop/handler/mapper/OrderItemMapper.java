package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.OrderItemDTO;
import com.silmaur.shop.dto.OrderItemCreationDTO;
import com.silmaur.shop.model.OrderItem;
import org.mapstruct.*;

/**
 * Mapper para convertir entre entidades OrderItem y sus respectivos DTOs.
 * Utiliza MapStruct para la generación automática de implementación.
 */
@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderItemMapper {

  /**
   * Convierte un DTO de lectura (OrderItemDTO) a una entidad OrderItem.
   * Este se usa en actualizaciones o casos donde ya existe el ítem.
   *
   * @param orderId ID del pedido asociado.
   * @param dto DTO de lectura con datos del ítem.
   * @return Entidad OrderItem.
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "orderId", expression = "java(orderId)")
  OrderItem toEntity(@Context Long orderId, OrderItemDTO dto);

  /**
   * Convierte un DTO de creación (OrderItemCreationDTO) a una entidad OrderItem.
   * Este se usa cuando se crea un pedido con ítems nuevos.
   *
   * @param orderId ID del pedido al que se vinculará el ítem.
   * @param dto DTO de creación del ítem.
   * @return Entidad OrderItem.
   */
  @Mapping(target = "orderId", expression = "java(orderId)")
  OrderItem toEntity(@Context Long orderId, OrderItemCreationDTO dto);



  /**
   * Convierte una entidad OrderItem a su DTO.
   *
   * @param entity Entidad persistida.
   * @return DTO con la información del item.
   */
  OrderItemDTO toDto(OrderItem entity);

  /**
   * Actualiza una entidad OrderItem existente a partir de un DTO.
   *
   * @param dto    DTO con campos actualizados.
   * @param entity Entidad original a actualizar.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateFromDto(OrderItemDTO dto, @MappingTarget OrderItem entity);
}
