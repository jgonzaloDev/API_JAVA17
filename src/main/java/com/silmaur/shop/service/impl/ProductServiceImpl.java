package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.exception.ProductAlreadyExistsException;
import com.silmaur.shop.exception.ProductNotFoundException;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.model.Product;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.ProductService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.Comparator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductMapper productMapper;
  private final ProductRepository productRepository;

  @Override
  public Single<Product> createProduct(ProductDTO productDTO) {
    return Single.fromCallable(() -> {
      Product product = productMapper.toEntity(productDTO);

      // Verificar si el producto ya existe por nombre
      if (productRepository.findByName(product.getName()).isPresent()) {
        throw new ProductAlreadyExistsException("El producto con el nombre " + product.getName() + " ya existe");
      }

      // Generar un ID personalizado
      String customId = generateCustomId(product.getName());
      product.setId(customId);

      // Asignar la fecha de creación
      product.setCreatedAt(LocalDateTime.now());
      product.setUpdatedAt(LocalDateTime.now());

      return productRepository.save(product);
    });
  }

  @Override
  public Single<Product> getProductById(String id) {
    return Single.fromCallable(() -> productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id)));
  }

  @Override
  public Single<List<Product>> getAllProducts() {
    return Single.fromCallable(() ->
        productRepository.findAll().stream()
            .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
            .collect(Collectors.toList())
    );
  }


  @Override
  public Completable deleteProduct(String id) {
    return Completable.fromAction(() ->
        productRepository.findById(id)
            .ifPresentOrElse(
                productRepository::delete,
                () -> { throw new ProductNotFoundException("Producto no encontrado con ID: " + id); }
            )
    );
  }




  @Override
  public Single<Product> updateProduct(String id, ProductDTO productDTO) {
    return Single.fromCallable(() -> productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id)))
        .map(existingProduct -> {
          // Usar el mapper para actualizar los valores sin sobrescribir ID ni fechas
          productMapper.updateProductFromDTO(productDTO, existingProduct);
          existingProduct.setUpdatedAt(LocalDateTime.now()); // Registrar la actualización
          return productRepository.save(existingProduct);
        });
  }


  private String generateCustomId(String productName) {
    String prefix = productName.substring(0, 3).toUpperCase();
    long timestamp = System.currentTimeMillis();
    return prefix + timestamp;
  }
}
