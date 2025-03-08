package com.silmaur.shop.service;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.model.Product;
import reactor.core.publisher.Mono;
import java.util.List;

public interface ProductService {
  Mono<Product> createProduct(ProductDTO productDTO);
  Mono<Product> getProductById(String id);
  Mono<List<Product>> getAllProducts();
  Mono<Product> updateProduct(String id, ProductDTO productDTO);
  Mono<Void> deleteProduct(String id);
}
