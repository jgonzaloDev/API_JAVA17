package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.exception.CategoryAlreadyExistsException;
import com.silmaur.shop.exception.CategoryNotFoundException;
import com.silmaur.shop.handler.mapper.CategoryMapper;
import com.silmaur.shop.model.Category;
import com.silmaur.shop.repository.CategoryRepository;
import com.silmaur.shop.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper mapper;

  @Override
  public Mono<Category> createCategory(CategoryDto categoryDto) {
    Category category = mapper.toEntityCategory(categoryDto);
    return categoryRepository.findByName(category.getName())
        .flatMap(existing ->
            Mono.<Category>error(new CategoryAlreadyExistsException(
                "La categoría con el nombre " + category.getName() + " ya existe"))
        )
        .switchIfEmpty(Mono.defer(() -> {
          // Actualiza los campos si es necesario
          category.setName(categoryDto.getName());
          category.setDescription(categoryDto.getDescription());
          return categoryRepository.save(category);
        }));
  }

  @Override
  public Mono<Category> getCategoryById(String id) {
    Long categoryId;
    try {
      categoryId = Long.parseLong(id);
    } catch (NumberFormatException e) {
      return Mono.error(new IllegalArgumentException("ID inválido: " + id));
    }
    return categoryRepository.findById(categoryId)
        .switchIfEmpty(Mono.error(new CategoryNotFoundException("Categoría no encontrada con ID: " + id)));
  }

  @Override
  public Mono<List<Category>> getAllCategories() {
    return categoryRepository.findAll()
        .sort(Comparator.comparing(c -> c.getName().toLowerCase()))
        .collectList();
  }

  @Override
  public Mono<Category> updateCategory(String id, CategoryDto categoryDto) {
    Long categoryId;
    try {
      categoryId = Long.parseLong(id);
    } catch (NumberFormatException e) {
      return Mono.error(new IllegalArgumentException("ID inválido: " + id));
    }
    return categoryRepository.findById(categoryId)
        .switchIfEmpty(Mono.error(new CategoryNotFoundException("Categoría no encontrada con ID: " + id)))
        .flatMap(existing -> {
          mapper.updateCategoryFromDto(categoryDto, existing);
          return categoryRepository.save(existing);
        });
  }

  @Override
  public Mono<Void> deleteCategory(String id) {
    Long categoryId;
    try {
      categoryId = Long.parseLong(id);
    } catch (NumberFormatException e) {
      return Mono.error(new IllegalArgumentException("ID inválido: " + id));
    }
    return categoryRepository.findById(categoryId)
        .switchIfEmpty(Mono.error(new CategoryNotFoundException("Categoría no encontrada con ID: " + id)))
        .flatMap(categoryRepository::delete);
  }
}
