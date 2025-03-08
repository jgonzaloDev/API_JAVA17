package com.silmaur.shop.handler.mapper;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

  Category toEntityCategory(CategoryDto categoryDto);

  CategoryDto toCategoryDTO(Category category);

  void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
