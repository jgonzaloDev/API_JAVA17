package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.model.Category;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

  /*@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;*/


  @Mappings({
      @Mapping(target = "id", source = "id"),
      @Mapping(target = "name" , source = "name"),
      @Mapping(target = "description", source = "description")
  })
  Category toEntityCategory(CategoryDto categoryDto);

  @Mappings({
      @Mapping(target = "id", source = "id"),
      @Mapping(target = "name" , source = "name"),
      @Mapping(target = "description", source = "description")
  })
  CategoryDto toCategoryDTO(Category categoryDto);


}
