package com.silmaur.shop.model;

import com.silmaur.shop.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity  // Indica que esta clase es una entidad JPA y se mapeará a una tabla en la base de datos.
@Table(name = "products") // Especifica que esta entidad se asociará con la tabla "products".
@Data  // Genera automáticamente los métodos getter, setter, toString, equals y hashCode (Lombok).
@NoArgsConstructor  // Genera un constructor sin argumentos.
@AllArgsConstructor  // Genera un constructor con todos los argumentos.
public class Product {

  @Id // Indica que este campo es la clave primaria.
  private String id;

  @Column(nullable = false, unique = true) // No permite valores nulos y debe ser único en la base de datos.
  private String name;

  @ManyToOne // Relación Muchos a Uno con la entidad Category.
  @JoinColumn(name = "category_id", nullable = false) // Clave foránea en la tabla "products" referenciando "categories(id)".
  private Category category;

  @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2) // Precio de compra con precisión de 10 y 2 decimales.
  private BigDecimal purchasePrice;

  @Column(name = "sale_price", nullable = false, precision = 10, scale = 2) // Precio de venta con precisión de 10 y 2 decimales.
  private BigDecimal salePrice;

  @Column(nullable = false) // Stock disponible, no permite valores nulos.
  private int stock;

  @Column(name = "min_stock", nullable = false) // Stock mínimo antes de alerta, no permite valores nulos.
  private int minStock;

  @Column(name = "created_at", nullable = false, updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  // Fecha de creación, se asigna automáticamente en la base de datos.
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  // Fecha de última actualización, se actualiza automáticamente cuando se modifica el registro.
  private LocalDateTime updatedAt;

  @PreUpdate
  public void setLastUpdate() {
    this.updatedAt = LocalDateTime.now(); // Se ejecuta antes de actualizar el registro para asignar la fecha actual.
  }

  @AssertTrue(message = "Sale price must be greater than or equal to purchase price")
  @Transient // Este método no se guarda en la base de datos.
  public boolean isValidPrice() {
    return salePrice.compareTo(purchasePrice) >= 0; // Verifica que el precio de venta sea mayor o igual al de compra.
  }
}
