package com.silmaur.shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.silmaur.shop.model.enums.ShippingPreferences;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

  private Long id;

  //@Size(min = 7, max = 10,  message = "El documento debe tener entre 7 y 12 dígitos")
  private String documentId;

/*  @Pattern(
      regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ\\s]{3,}$",
      message = "El nombre debe tener al menos 3 letras y solo contener letras y espacios"
  )*/
  private String name;

  // ✅ Opcional pero validado si viene
//  @Size(min = 7, max = 10, message = "El número de teléfono debe tener entre 7 y 10 dígitos")
  private String phone;

  // ✅ Opcional pero validado si viene
  @Email(message = "El correo debe tener formato válido")
  private String email;

  @JsonIgnore
  private LocalDateTime createdAt;

  // ✅ Obligatorio
  @NotBlank(message = "El nick de Tiktok es obligatorio")
  private String nickTiktok;

  @DecimalMin(value = "0.0", inclusive = true, message = "La apertura debe ser un monto positivo")
  @NotNull(message = "La apertura es obligatoria")
  private BigDecimal initialDeposit;

  private ShippingPreferences shippingPreference;

  private BigDecimal remainingDeposit;

}
