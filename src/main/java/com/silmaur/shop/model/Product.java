package com.silmaur.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa un producto en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("products")
public class Product {

  /**
   * Identificador único del producto.
   */
  @Id
  private Long id;

  /**
   * Nombre del producto.
   */
  @Column("name")
  private String name;

  /**
   * Identificador de la categoría a la que pertenece el producto.
   */
  @Column("category_id")
  private Long categoryId;

  /**
   * Precio de compra del producto.
   */
  @Column("purchase_price")
  private BigDecimal purchasePrice;

  /**
   * Precio de venta del producto.
   */
  @Column("sale_price")
  private BigDecimal salePrice;

  /**
   * Cantidad de unidades disponibles en stock.
   */
  @Column("stock")
  private int stock;

  /**
   * Cantidad mínima de unidades en stock para generar una alerta.
   */
  @Column("min_stock")
  private int minStock;

  /**
   * Descripción detallada del producto.
   */
  @Column("description")
  private String description;

  /**
   * URL de la imagen del producto.
   */
  @Column("image_url")
  private String imageUrl;

  /**
   * Marca del producto.
   */
  @Column("brand")
  private String brand;

  /**
   * Modelo del producto.
   */
  @Column("model")
  private String model;

/*
  */


  /**
   * Estado del producto (activo, inactivo, etc.).
   */
  @Column("status")
  private String status;

  /**
   * Unidad de medida del producto (unidad, kilogramo, etc.).
   */
  @Column("unit")
  private String unit;

  /**
   * Código de barras del producto.
   */
  @Column("barcode")
  private String barcode;

/**
 * Especificaciones del producto
 * */

@Column("specification_material")
  private String material;
  @Column("specification_capacity")
  private String capacity;
  @Column("specification_color")
  private String color;

  /**
   * SKU (Stock Keeping Unit) del producto.
   */
  @Column("sku")
  private String sku;

  /**
   * Fecha y hora de creación del producto.
   */
  @Column("created_at")
  private LocalDateTime createdAt;

  /**
   * Fecha y hora de la última actualización del producto.
   */
  @Column("updated_at")
  private LocalDateTime updatedAt;

  /**
   * Descuento aplicado al producto.
   */
  @Column("discount")
  private BigDecimal discount;

  /**
   * Verifica si el precio de venta es mayor o igual al precio de compra.
   *
   * @return true si el precio de venta es válido, false de lo contrario.
   */
  public boolean isValidPrice() {
    return salePrice.compareTo(purchasePrice) >= 0;
  }
}