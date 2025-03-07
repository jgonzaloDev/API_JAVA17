package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.exception.CategoryAlreadyExistsException;
import com.silmaur.shop.exception.ProductAlreadyExistsException;
import com.silmaur.shop.handler.mapper.CategoryMapper;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.model.Category;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.CategoryRepository;
import com.silmaur.shop.service.CategoryService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;
  private final CategoryMapper mapper;


  @Override
  public Single<Category> createCategory(CategoryDto categoryDto) {
    return Single.fromCallable(()-> {
    Category category = mapper.toEntityCategory(categoryDto);
    // Verificar si el producto ya existe por nombre
    if(categoryRepository.findByName(category.getName()).isPresent()){
      throw new CategoryAlreadyExistsException("La categoría con el nombre " + category.getName() + " ya existe");
    }
    category.setName(categoryDto.getName());
    category.setDescription(categoryDto.getDescription());


    return categoryRepository.save(category);
    });
  }

  @Override
  public Single<Category> getCategoryById(String id) {
    return null;
  }

  @Override
  public Single<List<Category>> getAllCategories() {
    return Single.fromCallable(() ->
        categoryRepository.findAll().stream()
            .sorted((p1,p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
            .collect(Collectors.toList())
    );
  }

  /*@Override
  public Single<List<Product>> getAllProducts() {
    return Single.fromCallable(() ->
        productRepository.findAll().stream()
            .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
            .collect(Collectors.toList())
    );
  }*/

  @Override
  public Single<Category> updateCategory(String id, CategoryDto categoryDto) {
    return null;
  }

  @Override
  public Completable deleteCategory(String id) {
    return null;
  }
}
