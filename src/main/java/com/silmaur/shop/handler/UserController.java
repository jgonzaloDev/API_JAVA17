/*
package com.silmaur.shop.handler;

import com.silmaur.shop.dto.UserDTO;
import com.silmaur.shop.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/create")
  public Mono<ResponseEntity<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
    return userService.createUser(userDTO)
        .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
  }

  @GetMapping("/list")
  public Mono<ResponseEntity<List<UserDTO>>> getAllUsers() {
    return userService.getAllUsers()
        .collectList() // Convertimos Flux<UserDTO> en Mono<List<UserDTO>>
        .map(users -> users.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(users)
        );
  }
}
*/
