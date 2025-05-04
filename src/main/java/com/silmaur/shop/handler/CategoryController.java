package com.silmaur.shop.handler;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.exception.CategoryNotFoundException;
import com.silmaur.shop.handler.mapper.CategoryMapper;
import com.silmaur.shop.service.CategoryService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @PostMapping("/create")
  public Mono<ResponseEntity<CategoryDto>> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
    return categoryService.createCategory(categoryDto)
        .map(category -> ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryMapper.toCategoryDTO(category)));
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<CategoryDto>> getCategoryById(@PathVariable String id) {
    return categoryService.getCategoryById(id)
        .map(category -> ResponseEntity.ok(categoryMapper.toCategoryDTO(category)))
        .switchIfEmpty(Mono.error(new CategoryNotFoundException("Categoría no encontrada con id: " + id)));
  }

  @GetMapping
  public Mono<ResponseEntity<List<CategoryDto>>> getAllCategories() {
    return categoryService.getAllCategories()
        .map(categories -> categories.stream()
            .map(categoryMapper::toCategoryDTO)
            .collect(Collectors.toList()))
        .map(ResponseEntity::ok);
  }

  @PutMapping("/update/{id}")
  public Mono<ResponseEntity<CategoryDto>> updateCategory(
      @PathVariable String id,
      @RequestBody CategoryDto categoryDto) {
    return categoryService.updateCategory(id, categoryDto)
        .map(category -> ResponseEntity.ok(categoryMapper.toCategoryDTO(category)))
        .switchIfEmpty(Mono.error(new CategoryNotFoundException("Categoría no encontrada con id: " + id)));
  }

  @DeleteMapping("/delete/{id}")
  public Mono<ResponseEntity<Void>> deleteCategory(@PathVariable String id) {
    return categoryService.deleteCategory(id)
        .then(Mono.just(ResponseEntity.noContent().build()));
  }
}
