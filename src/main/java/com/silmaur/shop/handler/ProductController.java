package com.silmaur.shop.handler;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.service.ProductService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  @PostMapping
  public Mono<ResponseEntity<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.createProduct(productDTO)
        .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDTO(product)));
  }



  @GetMapping
  public Mono<ResponseEntity<List<ProductDTO>>> getAllProducts() {
    return productService.getAllProducts()
        .collectList() // Collect Flux<Product> into Mono<List<Product>>
        .map(products -> {
          if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
          } else {
            List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
          }
        });
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteProduct(@PathVariable Long id) {
    return productService.deleteProductById(id);
  }


  @PutMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> updateProduct(@PathVariable Long id,
      @Valid @RequestBody ProductDTO productDTO) {
    return productService.updateProduct(id, productDTO)
        .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
        .onErrorReturn(ResponseEntity.notFound().build());
  }
}
