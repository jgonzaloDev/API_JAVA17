package com.silmaur.shop.service;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface ProductService {
  Mono<Product> createProduct(ProductDTO productDTO);
  Flux<Product> getAllProducts();;
  Mono<Product> updateProduct(Long id, ProductDTO productDTO);
  Mono<Void> deleteProductById(Long id);
  Mono<Product> getProductById(Long id);

}
