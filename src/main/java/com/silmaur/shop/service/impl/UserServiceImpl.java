package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.UserDTO;
import com.silmaur.shop.exception.RoleAlreadyExistsException;
import com.silmaur.shop.exception.UserAlreadyExistsException;
import com.silmaur.shop.model.Role;
import com.silmaur.shop.model.User;
import com.silmaur.shop.repository.RoleRepository;
import com.silmaur.shop.repository.UserRepository;
import com.silmaur.shop.service.UserService;
import io.reactivex.rxjava3.core.Single;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Single<UserDTO> createUser(UserDTO userDTO) {
    return Single.fromCallable(() -> {
      if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
        throw new UserAlreadyExistsException("El usuario " + userDTO.getUsername() + " ya existe");
      }

      // Buscar el rol por ID (ya no por nombre)
      Role role = roleRepository.findById(userDTO.getRoleId())
          .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

      // Crear usuario
      User user = new User();
      user.setUsername(userDTO.getUsername());
      user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      user.setRole(role);


      User savedUser = userRepository.save(user);
      return new UserDTO(savedUser.getUsername(), user.getPassword(), savedUser.getRole().getId()); // Devuelve roleId
    });
  }
}
