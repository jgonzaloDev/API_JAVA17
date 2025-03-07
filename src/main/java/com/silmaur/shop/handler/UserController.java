package com.silmaur.shop.handler;

import com.silmaur.shop.dto.UserDTO;
import com.silmaur.shop.service.UserService;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public Single<ResponseEntity<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
    return userService.createUser(userDTO)
        .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
  }
}
