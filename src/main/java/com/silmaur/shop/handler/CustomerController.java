package com.silmaur.shop.handler;

import com.silmaur.shop.dto.CustomerDTO;
import com.silmaur.shop.dto.Response;
import com.silmaur.shop.exception.DocumentIdAlreadyExistsException;
import com.silmaur.shop.service.CustomerService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
@Slf4j
public class CustomerController {

  private final CustomerService customerService;


  @PostMapping("/create")
  public Mono<ResponseEntity<Response<CustomerDTO>>> createCustomer(
      @Valid @RequestBody CustomerDTO customerDto) {
    return customerService.createCustomer(customerDto)
        .map(dto -> ResponseEntity.status(HttpStatus.CREATED)
            .body(Response.success("Cliente creado correctamente", dto)))
        .onErrorResume(DocumentIdAlreadyExistsException.class, ex ->
            Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Response.error(HttpStatus.CONFLICT, ex.getMessage(), null))));
  }

  // Obtener todos los clientes (con paginación opcional)


  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<CustomerDTO> getAllCustomers(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {

    if (page < 0 || size < 1) {
      return Flux.error(new IllegalArgumentException("Page and size must be positive numbers."));
    }

    Flux<CustomerDTO> customersFlux = customerService.getAllCustomers();
    long skipCount = (long) page * size;
    return customersFlux.skip(skipCount).take(size);
  }

  // Obtener un cliente por ID
  @GetMapping("/{id}")
  public Mono<CustomerDTO> getCustomerById(@PathVariable Long id) {
    return customerService.getCustomerById(id);
  }

  // Actualizar un cliente
  @PutMapping("/{id}")
  public Mono<CustomerDTO> updateCustomer(@PathVariable Long id,
      @Valid @RequestBody CustomerDTO customerDto) {
    return customerService.updateCustomer(id, customerDto);
  }

  // Eliminar un cliente

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteCustomer(@PathVariable Long id) {
    log.debug("Recibida solicitud DELETE para el cliente con ID: {}", id);
    return customerService.deleteCustomer(id)
        .doOnSuccess(
            v -> log.debug("Cliente con ID: {} eliminado correctamente en el controlador.", id))
        .doOnError(
            e -> log.error("Error al eliminar el cliente con ID: {} en el controlador.", id, e))
        .then();
  }

  private Map<String, String> createErrorResponse(String errorMessage) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", errorMessage);
    return errorResponse;
  }

  private Map<String, String> createSuccessResponse(CustomerDTO customerDTO) {
    Map<String, String> successResponse = new HashMap<>();
    successResponse.put("message", "Cliente creado correctamente");
    successResponse.put("customerId", customerDTO.getId().toString()); // Optional
    return successResponse;
  }
}