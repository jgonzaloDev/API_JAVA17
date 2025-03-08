package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.exception.ProductAlreadyExistsException;
import com.silmaur.shop.exception.ProductNotFoundException;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductMapper productMapper;
  private final ProductRepository productRepository;

  @Override
  public Mono<Product> createProduct(ProductDTO productDTO) {
    // Convertir el DTO a entidad
    Product product = productMapper.toEntity(productDTO);
    // Verificar si ya existe un producto con el mismo nombre
    return productRepository.findByName(product.getName())
        .flatMap(existing ->
            Mono.<Product>error(new ProductAlreadyExistsException("El producto con el nombre "
                + product.getName() + " ya existe"))
        )
        .switchIfEmpty(
            Mono.defer(() -> {
              String customId = generateCustomId(product.getName());
              product.setId(customId);
              product.setCreatedAt(LocalDateTime.now());
              product.setUpdatedAt(LocalDateTime.now());
              return productRepository.save(product);
            })
        );
  }

  @Override
  public Mono<Product> getProductById(String id) {
    return productRepository.findById(id)
        .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)));
  }

  @Override
  public Mono<List<Product>> getAllProducts() {
    return productRepository.findAll()
        .collectList()
        .map(list -> list.stream()
            .sorted(Comparator.comparing(p -> p.getName().toLowerCase()))
            .collect(Collectors.toList()));
  }

  @Override
  public Mono<Void> deleteProduct(String id) {
    return productRepository.findById(id)
        .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)))
        .flatMap(productRepository::delete);
  }

  @Override
  public Mono<Product> updateProduct(String id, ProductDTO productDTO) {
    return productRepository.findById(id)
        .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto no encontrado con ID: " + id)))
        .flatMap(existingProduct -> {
          // Actualiza los valores sin sobrescribir ID ni fecha de creación
          productMapper.updateProductFromDTO(productDTO, existingProduct);
          existingProduct.setUpdatedAt(LocalDateTime.now());
          return productRepository.save(existingProduct);
        });
  }

  private String generateCustomId(String productName) {
    String prefix = productName.substring(0, 3).toUpperCase();
    long timestamp = System.currentTimeMillis();
    return prefix + timestamp;
  }
}
