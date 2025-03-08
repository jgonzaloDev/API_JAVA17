package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.model.Product;
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
public interface ProductMapper {

  // Mapea DTO a Entity
  @Mappings({
      @Mapping(target = "id", ignore = true), // Ignora el ID para mantener el existente
      @Mapping(target = "name", source = "name"),
      @Mapping(target = "purchasePrice", source = "purchasePrice"),
      @Mapping(target = "salePrice", source = "salePrice"),
      @Mapping(target = "stock", source = "stock"),
      @Mapping(target = "minStock", source = "minStock"),
      // En lugar de "category.id", mapea directamente a "categoryId"
      @Mapping(target = "categoryId", source = "categoryId"),
      @Mapping(target = "createdAt", ignore = true), // No sobrescribir fecha de creación
      @Mapping(target = "updatedAt", ignore = true)  // Se maneja en @PreUpdate o manualmente
  })
  Product toEntity(ProductDTO dto);

  // Mapea Entity a DTO
  @Mappings({
      @Mapping(target = "id", source = "id"),
      @Mapping(target = "name", source = "name"),
      @Mapping(target = "purchasePrice", source = "purchasePrice"),
      @Mapping(target = "salePrice", source = "salePrice"),
      @Mapping(target = "stock", source = "stock"),
      @Mapping(target = "minStock", source = "minStock"),
      // Mapea directamente "categoryId" de la entidad al DTO
      @Mapping(target = "categoryId", source = "categoryId")
  })
  ProductDTO toDTO(Product entity);

  // Actualiza una entidad existente a partir del DTO sin sobrescribir ID ni fechas
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "updatedAt", ignore = true)
  })
  void updateProductFromDTO(ProductDTO dto, @MappingTarget Product entity);
}
