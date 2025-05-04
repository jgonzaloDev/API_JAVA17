package com.silmaur.shop.repository;

import com.silmaur.shop.model.Product;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

  // Buscar producto por nombre (asegúrate de que el nombre es único)
  @Query("SELECT * FROM products WHERE name = :name")
  Mono<Product> findByName(String name);


  // Método para eliminar un producto por ID
  @Modifying
  @Query("DELETE FROM products WHERE id = :id")
  Mono<Void> deleteByIdCustom(Long id);
}
