/*
package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.UserDTO;
import com.silmaur.shop.exception.UserAlreadyExistsException;
import com.silmaur.shop.model.Role;
import com.silmaur.shop.model.User;
import com.silmaur.shop.repository.RoleRepository;
import com.silmaur.shop.repository.UserRepository;
import com.silmaur.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Mono<UserDTO> createUser(UserDTO userDTO) {
    return userRepository.findByUsername(userDTO.getUsername())
        .flatMap(existingUser -> Mono.<UserDTO>error(
            new UserAlreadyExistsException("El usuario " + userDTO.getUsername() + " ya existe"))
        )
        .switchIfEmpty(
            roleRepository.findById(userDTO.getRoleId()) // Busca el rol en la BD
                .switchIfEmpty(Mono.error(new RuntimeException("Rol no encontrado")))
                .flatMap(role -> {
                  User user = User.builder()
                      .username(userDTO.getUsername())
                      .password(passwordEncoder.encode(userDTO.getPassword())) // 🔐 Encripta la contraseña
                      .roleId(userDTO.getRoleId())  // Asegura que el rol tenga el prefijo "ROLE_"
                      .build();
                  return userRepository.save(user);
                })
                .map(savedUser -> new UserDTO(savedUser.getUsername(), savedUser.getPassword(), savedUser.getRoleId()))
        );
  }


  @Override
  public Flux<UserDTO> getAllUsers() {
    return userRepository.findAll().map(this::toDTO);
  }

  private UserDTO toDTO(User user) {
    return new UserDTO(user.getUsername(), user.getPassword(), user.getRoleId());
  }
}
*/
