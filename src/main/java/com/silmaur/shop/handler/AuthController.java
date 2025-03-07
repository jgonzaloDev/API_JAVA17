package com.silmaur.shop.handler;

import com.silmaur.shop.dto.AuthResponse;
import com.silmaur.shop.dto.UserDTO;
import com.silmaur.shop.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtProvider = jwtProvider;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody UserDTO userDTO) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
      );

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String token = jwtProvider.generateToken(userDetails.getUsername());

      return ResponseEntity.ok(new AuthResponse(token));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
