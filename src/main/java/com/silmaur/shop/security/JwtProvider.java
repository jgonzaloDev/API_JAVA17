package com.silmaur.shop.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.Base64;

@Component
public class JwtProvider {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private long expirationTime;

  private Key getSigningKey() {
    byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Optional<String> extractUsername(String token) {
    return extractClaims(token).map(Claims::getSubject);
  }

  private Optional<Claims> extractClaims(String token) {
    try {
      return Optional.of(Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody());
    } catch (JwtException e) {
      return Optional.empty();
    }
  }

  public boolean validateToken(String token, String username) {
    return extractUsername(token)
        .map(extractedUser -> extractedUser.equals(username) && !isTokenExpired(token))
        .orElse(false);
  }

  private boolean isTokenExpired(String token) {
    return extractClaims(token)
        .map(Claims::getExpiration)
        .map(exp -> exp.before(new Date()))
        .orElse(true);
  }
}
