package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.exception.DocumentIdAlreadyExistsException;
import com.silmaur.shop.exception.ProductAlreadyExistsException;
import com.silmaur.shop.exception.ProductNotFoundException;
import com.silmaur.shop.exception.SalePriceLessThanPurchasePriceException;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductMapper productMapper;
  private final ProductRepository productRepository;

  @Override
  @Transactional
  public Mono<Product> createProduct(ProductDTO productDTO) {
    log.info("Iniciando creación de producto con nombre: {}", productDTO.getName());

    return productRepository.findByName(productDTO.getName())
        .flatMap(existing -> Mono.<Product>error(new ProductAlreadyExistsException(
            "El producto con el nombre " + productDTO.getName() + " ya existe")))
        .switchIfEmpty(Mono.defer(() -> {
          Product product = productMapper.toEntity(productDTO);
          product.setCreatedAt(LocalDateTime.now());
          product.setUpdatedAt(LocalDateTime.now());

          return productRepository.save(product)
              .doOnSuccess(savedProduct -> log.info("Producto creado con ID: {}", savedProduct.getId()));
        }))
        .onErrorMap(DataIntegrityViolationException.class, ex -> {
          if (ex.getMessage().contains("CONSTRAINT_F2")) {
            return new SalePriceLessThanPurchasePriceException(
                "El precio de venta ('" + productDTO.getSalePrice()
                    + "') debe ser mayor o igual al precio de compra ('"
                    + productDTO.getPurchasePrice() + "').");
          }
          return ex;
        });
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
        .switchIfEmpty(
            Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)))
        .flatMap(existingProduct -> {
          // Actualiza los valores sin sobrescribir ID ni fecha de creación
          productMapper.updateProductFromDTO(productDTO, existingProduct);
          existingProduct.setUpdatedAt(LocalDateTime.now());
          return productRepository.save(existingProduct);
        });
  }

}
