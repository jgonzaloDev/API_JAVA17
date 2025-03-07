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

  // Mapea DTO a Entity, asegurando que los atributos coincidan correctamente
  @Mappings({
      @Mapping(target = "id", ignore = true), // Ignora el ID para mantener el existente
      @Mapping(target = "name", source = "name"),
      @Mapping(target = "purchasePrice", source = "purchasePrice"),
      @Mapping(target = "salePrice", source = "salePrice"),
      @Mapping(target = "stock", source = "stock"),
      @Mapping(target = "minStock", source = "minStock"),
      @Mapping(target = "category.id", source = "categoryId"), // Mapea categoryId a category
      @Mapping(target = "createdAt", ignore = true), // No sobrescribir fecha de creación
      @Mapping(target = "updatedAt", ignore = true)  // Se maneja en @PreUpdate
  })
  Product toEntity(ProductDTO dto);

  // Mapea Entity a DTO, asegurando que los atributos coincidan
  @Mappings({
      @Mapping(target = "id", source = "id"),
      @Mapping(target = "name", source = "name"),
      @Mapping(target = "purchasePrice", source = "purchasePrice"),
      @Mapping(target = "salePrice", source = "salePrice"),
      @Mapping(target = "stock", source = "stock"),
      @Mapping(target = "minStock", source = "minStock"),
      @Mapping(target = "categoryId", source = "category.id") // Mapea category a categoryId
  })
  ProductDTO toDTO(Product entity);

  // Método para actualizar una entidad existente desde un DTO sin sobrescribir ID ni fechas
  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true), // No modificar la fecha de creación
      @Mapping(target = "updatedAt", ignore = true)  // Se manejará en @PreUpdate
  })
  void updateProductFromDTO(ProductDTO dto, @MappingTarget Product entity);
}

