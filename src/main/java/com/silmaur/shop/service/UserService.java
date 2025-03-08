package com.silmaur.shop.service;

import com.silmaur.shop.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
  Mono<UserDTO> createUser(UserDTO userDTO);
}
