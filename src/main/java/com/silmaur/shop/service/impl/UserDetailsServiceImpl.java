/*
package com.silmaur.shop.service.impl;

import com.silmaur.shop.model.Role;
import com.silmaur.shop.model.User;
import com.silmaur.shop.repository.RoleRepository;
import com.silmaur.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository; // Agregado

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    return userRepository.findByUsername(username)
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuario no encontrado")))
        .flatMap(user ->
            roleRepository.findById(user.getRoleId())
                .map(role -> {
                  String roleName = role.getName();
                  SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

                  return new org.springframework.security.core.userdetails.User(
                      user.getUsername(),
                      user.getPassword(),
                      Collections.singletonList(authority)
                  );
                })
        );
  }
}*/
