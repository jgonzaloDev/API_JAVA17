package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.dto.ProductQuickDTO;
import com.silmaur.shop.exception.DocumentIdAlreadyExistsException;
import com.silmaur.shop.exception.ProductAlreadyExistsException;
import com.silmaur.shop.exception.ProductNotFoundException;
import com.silmaur.shop.exception.SalePriceLessThanPurchasePriceException;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.CategoryRepository;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.ProductService;
import java.math.BigDecimal;
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
  private final CategoryRepository categoryRepository; // Necesario para buscar "RÁPIDOS"

  @Override
  @Transactional
  public Mono<Product> createProduct(ProductDTO productDTO) {
    log.info("Iniciando creación de producto con nombre: {}", productDTO.getName());

    return validarDuplicado(productDTO.getName())
        .switchIfEmpty(Mono.defer(() -> {
          Product product = productMapper.toEntity(productDTO);
          product.setCreatedAt(LocalDateTime.now());
          product.setUpdatedAt(LocalDateTime.now());

          return productRepository.save(product)
              .doOnSuccess(saved -> log.info("Producto creado con ID: {}", saved.getId()));
        }))
        .cast(Product.class)
        .onErrorResume(DataIntegrityViolationException.class, this::manejarErrorIntegridad);
  }

  @Override
  @Transactional
  public Mono<Product> createProductQuick(ProductQuickDTO quickDTO) {
    log.info("Iniciando creación de producto RÁPIDO con nombre: {}", quickDTO.getName());

    return categoryRepository.findByNameIgnoreCase("RÁPIDOS")
        .switchIfEmpty(Mono.error(new RuntimeException("La categoría 'RÁPIDOS' no existe.")))
        .map(categoria -> {
          // 🔹 Convertimos manualmente ProductQuickDTO → ProductDTO
          ProductDTO dto = new ProductDTO();
          dto.setName(quickDTO.getName());
          dto.setSalePrice(quickDTO.getSalePrice());
          dto.setStock(quickDTO.getStock());

          // 🔹 Aplicar defaults
          return aplicarDefaultsRapido(dto, categoria.getId());
        })
        .flatMap(this::createProduct);
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

  @Override
  public Mono<Void> deleteProductById(Long id) {
    return productRepository.deleteByIdCustom(id).then();
  }

  @Override
  public Mono<Product> updateProduct(Long id, ProductDTO productDTO) {
    return productRepository.findById(id)
        .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)))
        .flatMap(existingProduct ->
            productRepository.findByName(productDTO.getName())
                .flatMap(anotherProduct -> {
                  if (!anotherProduct.getId().equals(id)) {
                    return Mono.error(new ProductAlreadyExistsException("El producto con el nombre " + productDTO.getName() + " ya existe"));
                  }
                  return Mono.just(existingProduct);
                })
                .switchIfEmpty(Mono.just(existingProduct))
                .flatMap(prod -> {
                  productMapper.updateProductFromDTO(productDTO, prod);
                  prod.setUpdatedAt(LocalDateTime.now());
                  return productRepository.save(prod);
                })
        );
  }

  // Helpers internos
  private Mono<Product> validarDuplicado(String nombre) {
    return productRepository.findByName(nombre)
        .flatMap(existing -> Mono.error(new ProductAlreadyExistsException("El producto con el nombre " + nombre + " ya existe")));
  }

  private Mono<Product> manejarErrorIntegridad(DataIntegrityViolationException ex) {
    String message = ex.getMessage();
    if (message != null && (message.contains("UNIQUE") || message.contains("UK") || message.contains("constraint [UK"))) {
      return Mono.error(new ProductAlreadyExistsException("Ya existe un producto con el mismo nombre"));
    }
    if (message != null && message.contains("CONSTRAINT_F2")) {
      return Mono.error(new SalePriceLessThanPurchasePriceException("El precio de venta debe ser mayor o igual al precio de compra"));
    }
    return Mono.error(ex);
  }

  private ProductDTO aplicarDefaultsRapido(ProductDTO dto, Long categoryId) {
    dto.setCategoryId(categoryId);
    if (dto.getPurchasePrice() == null) dto.setPurchasePrice(BigDecimal.ZERO);
    if (dto.getSalePrice() == null) dto.setSalePrice(BigDecimal.ZERO);
    if (dto.getStock() == null) dto.setStock(1);
    if (dto.getMinStock() == null) dto.setMinStock(1);
    if (dto.getStatus() == null) dto.setStatus("ACTIVE");
    if (dto.getUnit() == null) dto.setUnit("UND");
    if (dto.getDiscount() == null) dto.setDiscount(BigDecimal.ZERO);
    return dto;
  }

}

