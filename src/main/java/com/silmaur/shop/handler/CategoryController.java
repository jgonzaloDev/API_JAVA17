package com.silmaur.shop.handler;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.handler.mapper.CategoryMapper;
import com.silmaur.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final CategoryMapper mapper;

  @PostMapping
  public Mono<ResponseEntity<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto) {
    return categoryService.createCategory(categoryDto)
        .map(category -> ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toCategoryDTO(category)));
  }

  @GetMapping
  public Mono<ResponseEntity<List<CategoryDto>>> getAllProducts() {
    return categoryService.getAllCategories()
        .map(categories -> categories.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(categories.stream().map(mapper::toCategoryDTO).toList()));
  }
}
