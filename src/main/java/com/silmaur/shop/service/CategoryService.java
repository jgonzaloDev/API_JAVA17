package com.silmaur.shop.service;

import com.silmaur.shop.dto.CategoryDto;
import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.model.Category;
import com.silmaur.shop.model.Product;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface CategoryService {
  Single<Category> createCategory(CategoryDto categoryDto);
  Single<Category> getCategoryById(String id);
  Single<List<Category>> getAllCategories();
  Single<Category> updateCategory(String id, CategoryDto categoryDto);
  Completable deleteCategory(String id);

}
