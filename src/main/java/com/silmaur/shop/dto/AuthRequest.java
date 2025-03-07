package com.silmaur.shop.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthRequest {
  @NotBlank(message = "El usuario es obligatorio")
  private String username;

  @NotBlank(message = "La contraseña es obligatoria")
  private String password;

}
