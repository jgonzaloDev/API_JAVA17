package com.silmaur.shop.handler;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  @PostMapping
  public Mono<ResponseEntity<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.createProduct(productDTO)
        .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDTO(product)));
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable String id) {
    return productService.getProductById(id)
        .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
        .onErrorReturn(ResponseEntity.notFound().build());
  }

  @GetMapping
  public Mono<ResponseEntity<List<ProductDTO>>> getAllProducts() {
    return productService.getAllProducts()
        .map(products -> products.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(products.stream().map(productMapper::toDTO).toList()));
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteProduct(@PathVariable String id) {
    return productService.deleteProduct(id);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> updateProduct(@PathVariable String id,
      @Valid @RequestBody ProductDTO productDTO) {
    return productService.updateProduct(id, productDTO)
        .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
        .onErrorReturn(ResponseEntity.notFound().build());
  }
}
