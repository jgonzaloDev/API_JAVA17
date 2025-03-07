package com.silmaur.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  @NotBlank(message = "El nombre de usuario es obligatorio")
  private String username;

  @NotBlank(message = "La contraseña es obligatoria")
  private String password;

  @NotNull(message = "El rol es obligatorio")
  private Long roleId;

}
