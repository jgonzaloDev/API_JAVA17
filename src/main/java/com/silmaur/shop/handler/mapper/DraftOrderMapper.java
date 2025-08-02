package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.DraftOrderDTO;
import com.silmaur.shop.dto.DraftOrderItemDTO;
import com.silmaur.shop.model.DraftOrder;
import com.silmaur.shop.model.DraftOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DraftOrderMapper {
  DraftOrderDTO toDto(DraftOrder entity);
  DraftOrder toEntity(DraftOrderDTO dto);
  DraftOrderItemDTO toItemDto(DraftOrderItem item);
  DraftOrderItem toItemEntity(DraftOrderItemDTO itemDto);
  List<DraftOrderItemDTO> toItemDtoList(List<DraftOrderItem> items);
}

