/*
package com.silmaur.shop.handler;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.service.ProductServiceReact;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/reactor")
@RequiredArgsConstructor
public class ProductReactController {

  private final ProductServiceReact productService;
  private final ProductMapper productMapper;

  @PostMapping
  public Mono<ResponseEntity<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.createProduct(productDTO)
        .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDTO(product)))
        .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable String id) {
    return productService.getProductById(id)
        .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
        .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
  }

  @GetMapping("/reactor")
  public Mono<ResponseEntity<List<ProductDTO>>> getAllProducts() {
    return ReactiveSecurityContextHolder.getContext()
        .flatMap(context -> {
          String username = context.getAuthentication().getName();
          System.out.println("Usuario autenticado: " + username);
          return productService.getAllProducts()
              .collectList()
              .map(products ->
                  products.isEmpty()
                      ? ResponseEntity.noContent().build()
                      : ResponseEntity.ok(
                          products.stream()
                              .map(productMapper::toDTO)
                              .collect(Collectors.toList())
                      )
              );
        });
  }



  @PutMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDTO productDTO) {
    return productService.updateProduct(id, productDTO)
        .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
        .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id) {
    return productService.deleteProduct(id)
        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
        .onErrorResume(e -> Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND)));
  }








}*/
