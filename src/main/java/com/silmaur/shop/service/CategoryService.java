package com.silmaur.shop.service;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.model.Category;
import reactor.core.publisher.Mono;
import java.util.List;

public interface CategoryService {
  Mono<Category> createCategory(CategoryDto categoryDto);
  Mono<Category> getCategoryById(String id);
  Mono<List<Category>> getAllCategories();
  Mono<Category> updateCategory(String id, CategoryDto categoryDto);
  Mono<Void> deleteCategory(String id);
}
