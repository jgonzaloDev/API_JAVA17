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

  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public Optional<String> extractUsername(String token) {
    return extractClaims(token).map(Claims::getSubject);
  }

  private Optional<Claims> extractClaims(String token) {
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
}

