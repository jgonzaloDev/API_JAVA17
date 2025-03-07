package com.silmaur.shop.service;

import com.silmaur.shop.dto.AuthRequest;
import com.silmaur.shop.dto.AuthResponse;
import io.reactivex.rxjava3.core.Single;

public interface AuthService {
  Single<AuthResponse> login(AuthRequest request);

}
