package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.AuthRequest;
import com.silmaur.shop.dto.AuthResponse;
import com.silmaur.shop.security.JwtProvider;
import com.silmaur.shop.service.AuthService;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtProvider jwtProvider;


  @Override
  public Single<AuthResponse> login(AuthRequest request) {
    return Single.fromCallable(() -> {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
      );

      UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
      String token = jwtProvider.generateToken(userDetails.getUsername());
      log.info("ESTE ES EL TOKEN QUE NECESITO VALIDAR: {}", token);

      return new AuthResponse(token);
    });
  }
}
