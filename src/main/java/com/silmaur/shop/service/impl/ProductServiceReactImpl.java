/*
package com.silmaur.shop.service.impl;

import com.silmaur.shop.service.ProductServiceReact;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.exception.ProductAlreadyExistsException;
import com.silmaur.shop.exception.ProductNotFoundException;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceReactImpl implements ProductServiceReact {
  private final ProductMapper productMapper;
  private final ProductRepository productRepository;

  @Override
  public Mono<Product> createProduct(ProductDTO productDTO) {
    return Mono.fromCallable(() -> productRepository.findByName(productDTO.getName()))
        .flatMap(optional -> {
          if (optional.isPresent()) {
            return Mono.error(new ProductAlreadyExistsException("El producto con el nombre " + productDTO.getName() + " ya existe"));
          }
          Product product = productMapper.toEntity(productDTO);
          String customId = generateCustomId(product.getName());
          product.setId(customId);
          product.setCreatedAt(LocalDateTime.now());
          product.setUpdatedAt(LocalDateTime.now());
          return Mono.fromCallable(() -> productRepository.save(product));
        });
  }

  @Override
  public Mono<Product> getProductById(String id) {
    return Mono.fromCallable(() -> productRepository.findById(id))
        .flatMap(optional -> optional
            .map(Mono::just)
            .orElseGet(() -> Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id))));
  }

  @Override
  public Flux<Product> getAllProducts() {
    return Flux.fromIterable(productRepository.findAll())
        .sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
  }

  @Override
  public Mono<Product> updateProduct(String id, ProductDTO productDTO) {
    return Mono.fromCallable(() -> productRepository.findById(id))
        .flatMap(optional -> optional
            .map(existingProduct -> {
              productMapper.updateProductFromDTO(productDTO, existingProduct);
              existingProduct.setUpdatedAt(LocalDateTime.now());
              return Mono.fromCallable(() -> productRepository.save(existingProduct));
            })
            .orElseGet(() -> Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id))));
  }

  @Override
  public Mono<Void> deleteProduct(String id) {
    return Mono.fromRunnable(() -> {
      productRepository.findById(id).ifPresentOrElse(
          productRepository::delete,
          () -> { throw new ProductNotFoundException("Producto no encontrado con ID: " + id); }
      );
    });
  }

  private String generateCustomId(String productName) {
    String prefix = productName.substring(0, 3).toUpperCase();
    long timestamp = System.currentTimeMillis();
    return prefix + timestamp;
  }
}
*/
