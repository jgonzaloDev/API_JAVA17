/*
package com.silmaur.shop.handler;

import com.silmaur.shop.dto.AuthRequest;
import com.silmaur.shop.dto.AuthResponse;
import com.silmaur.shop.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final ReactiveAuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @PostMapping("/login")
  public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
    log.info("Intento de inicio de sesión para el usuario: {}", request.getUsername());

    return authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()))
        .map(authentication -> {
          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          List<GrantedAuthority> authorities = userDetails.getAuthorities().stream().collect(Collectors.toList());
          String token = jwtProvider.generateToken(userDetails.getUsername(), authorities);
          log.info("Usuario autenticado exitosamente: {}", userDetails.getUsername());
          return ResponseEntity.ok(new AuthResponse(token));
        });
  }
}*/
