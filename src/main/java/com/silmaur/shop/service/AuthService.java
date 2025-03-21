package com.silmaur.shop.service;

import com.silmaur.shop.dto.AuthRequest;
import com.silmaur.shop.dto.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
  Mono<AuthResponse> login(AuthRequest request);


}
