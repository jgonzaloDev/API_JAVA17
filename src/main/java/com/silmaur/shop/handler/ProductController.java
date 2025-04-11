package com.silmaur.shop.handler;

import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.dto.Response;
import com.silmaur.shop.exception.DocumentIdAlreadyExistsException;
import com.silmaur.shop.exception.SalePriceLessThanPurchasePriceException;
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

  /**
   * Endpoint para crear un producto.
   */
  @PostMapping
  public Mono<ResponseEntity<Response<ProductDTO>>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.createProduct(productDTO)
        .map(product -> ResponseEntity.status(HttpStatus.CREATED)
            .body(Response.success("PRODUCTO CREADO CORRECTAMENTE", productMapper.toDto(product))));
  }




  /**
   * Endpoint para obtener todos los productos.
   */
  @GetMapping
  public Mono<ResponseEntity<List<ProductDTO>>> getAllProducts() {
    return productService.getAllProducts()
        .collectList() // Collect Flux<Product> into Mono<List<Product>>
        .map(products -> {
          if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // No hay productos
          } else {
            // Convertimos cada producto a un ProductDTO
            List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
          }
        });
  }

  /**
   * Endpoint para eliminar un producto por su ID.
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteProduct(@PathVariable Long id) {
    return productService.deleteProductById(id);
  }

  /**
   * Endpoint para actualizar un producto existente.
   */
  @PutMapping("/{id}")
  public Mono<ResponseEntity<ProductDTO>> updateProduct(@PathVariable Long id,
      @Valid @RequestBody ProductDTO productDTO) {
    return productService.updateProduct(id, productDTO)
        .map(
            product -> ResponseEntity.ok(productMapper.toDto(product))) // Mapeamos la entidad a DTO
        .onErrorReturn(
            ResponseEntity.notFound().build()); // Si no se encuentra el producto, devolvemos 404
  }


  /**
   * Endpoint para obtener un producto por su ID.
   */

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Response<ProductDTO>>> getProductById(@PathVariable Long id) {
    return productService.getProductById(id)
        .map(product -> ResponseEntity.ok(
            Response.success("Producto encontrado", productMapper.toDto(product))
        ))
        .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body((Response<ProductDTO>) (Object) Response.error(
                HttpStatus.NOT_FOUND,
                "Producto no encontrado",
                "PRODUCT_NOT_FOUND",
                "id"
            ))));
  }


}
