/*
package com.silmaur.shop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
@Component
public class JwtProvider {

  private final Key signingKey;

  public JwtProvider(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long expirationTime) {
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationTime = expirationTime;
  }

  private final long expirationTime;

  public String generateToken(String username, List<GrantedAuthority> authorities) {
    List<String> roles = authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    return Jwts.builder()
        .setSubject(username)
        .claim("roles", roles) // Agregar la reclamación de roles
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Optional<String> extractUsername(String token) {
    return extractClaims(token).map(Claims::getSubject);
  }

  public Optional<Claims> extractClaims(String token) { // Cambiado a public
    try {
      return Optional.of(Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token)
          .getBody());
    } catch (JwtException e) {
      log.warn("Error al validar el token: {}", e.getMessage());
      return Optional.empty();
    }
  }

  public boolean validateToken(String token) {
    return extractClaims(token)
        .map(claims -> claims.getExpiration().after(new Date()))
        .orElse(false);
  }
}*/
