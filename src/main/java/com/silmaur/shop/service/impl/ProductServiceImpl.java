package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.exception.ProductAlreadyExistsException;
import com.silmaur.shop.exception.ProductNotFoundException;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductMapper productMapper;
  private final ProductRepository productRepository;

  @Override
  public Mono<Product> createProduct(ProductDTO productDTO) {
    log.info("Iniciando creación de producto con nombre: {}", productDTO.getName());
    return productRepository.findByName(productDTO.getName())
        .flatMap(existing -> {
          log.warn("Producto con nombre {} ya existe", productDTO.getName());
          return Mono.<Product>error(new ProductAlreadyExistsException("El producto con el nombre "
              + productDTO.getName() + " ya existe"));
        })
        .switchIfEmpty(
            Mono.defer(() -> {
              Product product = productMapper.toEntity(productDTO);
              product.setCreatedAt(LocalDateTime.now());
              product.setUpdatedAt(LocalDateTime.now());
              log.info("Creando nuevo producto: {}", product);
              return productRepository.save(product)
                  .doOnSuccess(savedProduct -> log.info("Producto creado con ID: {}", savedProduct.getId()));
            })
        );
  }



  @Override
  public Mono<Product> getProductById(Long id) {
    return productRepository.findById(id)
        .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)));
  }

  @Override
  public Flux<Product> getAllProducts() {
    return productRepository.findAll()
        .sort(Comparator.comparing(p -> p.getName().toLowerCase()));
  }

  // ProductServiceImpl.java
  @Override
  public Mono<Void> deleteProductById(Long id) {
    return productRepository.deleteByIdCustom(id)
        .then(Mono.empty()); // Asegurar que la eliminación se complete
  }

  @Override
  public Mono<Product> updateProduct(Long id, ProductDTO productDTO) {
    return productRepository.findById(id)
        .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)))
        .flatMap(existingProduct -> {
          // Actualiza los valores sin sobrescribir ID ni fecha de creación
          productMapper.updateProductFromDTO(productDTO, existingProduct);
          existingProduct.setUpdatedAt(LocalDateTime.now());
          return productRepository.save(existingProduct);
        });
  }

}
