package com.silmaur.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

  private Long id;
  @NotBlank(message = "El nombre no puede estar vacío")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$", message = "El nombre solo puede contener letras y espacios")
  private String name;
  @Size(min = 3, max = 100, message = "La descripción debe tener entre 3 y 100 caracteres")
  private String description;


}
