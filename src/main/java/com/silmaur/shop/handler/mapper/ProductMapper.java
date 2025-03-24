package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.dto.SpecificationsDTO;
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

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "updatedAt", ignore = true),
      @Mapping(target = "material", source = "specifications.material"),
      @Mapping(target = "capacity", source = "specifications.capacity"),
      @Mapping(target = "color", source = "specifications.color"),
      // otros campos explícitos (si deseas simplificar, MapStruct los detecta automáticamente por nombre)
  })
  Product toEntity(ProductDTO dto);

  @Mappings({
      @Mapping(target = "specifications.material", source = "material"),
      @Mapping(target = "specifications.capacity", source = "capacity"),
      @Mapping(target = "specifications.color", source = "color"),
      // otros campos explícitos si deseas
  })
  ProductDTO toDto(Product entity);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = "createdAt", ignore = true),
      @Mapping(target = "updatedAt", ignore = true),
      @Mapping(target = "material", source = "specifications.material"),
      @Mapping(target = "capacity", source = "specifications.capacity"),
      @Mapping(target = "color", source = "specifications.color"),
      // otros campos si deseas
  })
  void updateProductFromDTO(ProductDTO dto, @MappingTarget Product entity);

  // Métodos auxiliares explícitos
  default SpecificationsDTO mapSpecifications(Product entity) {
    SpecificationsDTO spec = new SpecificationsDTO();
    spec.setMaterial(entity.getMaterial());
    spec.setCapacity(entity.getCapacity());
    spec.setColor(entity.getColor());
    return spec;
  }

  default void mapSpecifications(ProductDTO dto, @MappingTarget Product entity) {
    if (dto.getSpecifications() != null) {
      entity.setMaterial(dto.getSpecifications().getMaterial());
      entity.setCapacity(dto.getSpecifications().getCapacity());
      entity.setColor(dto.getSpecifications().getColor());
    }
  }
}

