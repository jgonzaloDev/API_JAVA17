/*
package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.AuthRequest;
import com.silmaur.shop.dto.AuthResponse;
import com.silmaur.shop.security.JwtProvider;
import com.silmaur.shop.service.AuthService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final ReactiveAuthenticationManager authenticationManager;
  private final ReactiveUserDetailsService userDetailsService;

  private final JwtProvider jwtProvider;

  @Override
  public Mono<AuthResponse> login(AuthRequest request) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
    ).flatMap(authentication ->
        userDetailsService.findByUsername(request.getUsername())
    ).map(userDetails -> {
      List<GrantedAuthority> authorities = userDetails.getAuthorities().stream()
          .collect(Collectors.toList()); // Obtener la lista de GrantedAuthority
      String token = jwtProvider.generateToken(userDetails.getUsername(), authorities); // Pasar la lista
      log.info("ESTE ES EL TOKEN QUE NECESITO VALIDAR: {}", token);
      return new AuthResponse(token);
    });
  }


}
*/
