package com.silmaur.shop.handler;

import com.silmaur.shop.dto.AuthResponse;
import com.silmaur.shop.dto.UserDTO;
import com.silmaur.shop.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final ReactiveAuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @PostMapping("/login")
  public Mono<ResponseEntity<AuthResponse>> login(@RequestBody UserDTO userDTO) {
    return authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()))
        .map(authentication -> {
          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          String token = jwtProvider.generateToken(userDetails.getUsername());
          return ResponseEntity.ok(new AuthResponse(token));
        })
        .onErrorResume(e -> {
          log.warn("Error en autenticación: {}", e.getMessage());
          return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Error de autenticación")));
        });

  }
}
