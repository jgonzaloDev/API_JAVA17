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
        .flatMap(existing -> {
          log.warn("Ya existe un producto con nombre: {}", productDTO.getName());
          return Mono.<Product>error(new ProductAlreadyExistsException(
              "El producto con el nombre " + productDTO.getName() + " ya existe"));
        })
        .switchIfEmpty(Mono.defer(() -> {
          Product product = productMapper.toEntity(productDTO);
          product.setCreatedAt(LocalDateTime.now());
          product.setUpdatedAt(LocalDateTime.now());

          return productRepository.save(product)
              .doOnSuccess(saved -> log.info("Producto creado con ID: {}", saved.getId()));
        }))
        .onErrorResume(DataIntegrityViolationException.class, ex -> {
          String message = ex.getMessage();

          if (message != null && (message.contains("UNIQUE") || message.contains("UK") || message.contains("constraint [UK"))) {
            log.error("Violación de unicidad al guardar producto: {}", message);
            return Mono.<Product>error(new ProductAlreadyExistsException("Ya existe un producto con el mismo nombre"));
          }

          if (message != null && message.contains("CONSTRAINT_F2")) {
            return Mono.<Product>error(new SalePriceLessThanPurchasePriceException(
                "El precio de venta ('" + productDTO.getSalePrice()
                    + "') debe ser mayor o igual al precio de compra ('"
                    + productDTO.getPurchasePrice() + "')."));
          }

          return Mono.<Product>error(ex);
        });
  }



  @Override
  public Mono<Product> getProductById(Long id) {
    return productRepository.findById(id);
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
        .flatMap(existingProduct ->
            productRepository.findByName(productDTO.getName())
                .flatMap(anotherProduct -> {
                  // 💥 Si existe otro producto con el mismo nombre y diferente ID => ERROR
                  if (!anotherProduct.getId().equals(id)) {
                    return Mono.error(new ProductAlreadyExistsException("El producto con el nombre " + productDTO.getName() + " ya existe"));
                  }
                  return Mono.just(existingProduct); // ✅ Es el mismo, permite continuar
                })
                .switchIfEmpty(Mono.just(existingProduct)) // No existe nadie con ese nombre, también se permite
                .flatMap(prod -> {
                  productMapper.updateProductFromDTO(productDTO, prod);
                  prod.setUpdatedAt(LocalDateTime.now());
                  return productRepository.save(prod);
                })
        );
  }




}
