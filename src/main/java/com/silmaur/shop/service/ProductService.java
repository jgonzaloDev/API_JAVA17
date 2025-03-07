package com.silmaur.shop.service;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.model.Product;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Completable;
import java.util.List;
import java.util.Optional;

public interface ProductService {
  Single<Product> createProduct(ProductDTO productDTO);
  Single<Product> getProductById(String id);
  Single<List<Product>> getAllProducts();
  Single<Product> updateProduct(String id, ProductDTO productDTO);
  Completable deleteProduct(String id);

}
