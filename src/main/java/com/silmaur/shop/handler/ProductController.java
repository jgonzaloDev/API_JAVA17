package com.silmaur.shop.handler;

import com.silmaur.shop.config.RxSecurityContext;
import com.silmaur.shop.dto.ProductDTO;
import com.silmaur.shop.handler.mapper.ProductMapper;
import com.silmaur.shop.service.ProductService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;

  @PostMapping
  public Single<ResponseEntity<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
    return productService.createProduct(productDTO)
        .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDTO(product)));
  }

  @GetMapping("/{id}")
  public Single<ResponseEntity<ProductDTO>> getProductById(@PathVariable String id) {
    return productService.getProductById(id)
        .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
        .onErrorReturnItem(ResponseEntity.notFound().build());
  }

  @GetMapping
  public Single<ResponseEntity<List<ProductDTO>>> getAllProducts() {
    SecurityContext currentContext = SecurityContextHolder.getContext();
    System.out.println("Contexto antes de propagar: " + currentContext); // Agrega este log
    return productService.getAllProducts()
        .compose(RxSecurityContext.propagate(currentContext))
        .map(products -> {
          System.out.println("Contexto dentro del map: " + SecurityContextHolder.getContext()); // Agrega este log
          if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
          } else {
            return ResponseEntity.ok(
                products.stream()
                    .map(productMapper::toDTO)
                    .collect(Collectors.toList())
            );
          }
        });
  }




  @GetMapping("/sync")
  public ResponseEntity<List<ProductDTO>> getAllProductsSync() {
    try {
      // Bloquea la ejecución hasta obtener la lista de productos
      List<ProductDTO> productDTOs = productService.getAllProducts()
          .blockingGet()
          .stream()
          .map(productMapper::toDTO)
          .collect(Collectors.toList());
      return ResponseEntity.ok(productDTOs);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }





  @DeleteMapping("/{id}")
  public Completable deleteProduct(@PathVariable String id) {
    return productService.deleteProduct(id);
  }



  @PutMapping("/{id}")
  public Single<ResponseEntity<ProductDTO>> updateProduct(@PathVariable String id, @Valid @RequestBody ProductDTO productDTO) {
    return productService.updateProduct(id, productDTO)
        .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
        .onErrorReturnItem(ResponseEntity.notFound().build());
  }
}
