package com.silmaur.shop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtSecurity {

  private static final String SECRET_KEY_STRING = "aVJhbmRvbVNlY3JldEtleUZvckpXVC1HZW5lcmF0aW9u"; // Base64
  private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY_STRING));
//  private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));


  public String generateToken(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día de validez
        .signWith(SECRET_KEY)
        .compact();
  }

  /*public boolean isTokenValid(String token) {

    try {
      extractAllClaims(token);
      log.info("este es el token {}", token);
      return true;
    } catch (Exception e) {
      log.error("Token invalido --> {}", e.getMessage());
      return false;
    }
  }*/

  public boolean isTokenValid(String token) {
    try {
      Claims claims = extractAllClaims(token);
      log.info("Claims extraídos: {}", claims);
      return true;
    } catch (Exception e) {
      log.error("Token inválido --> {}", e.getMessage());
      return false;
    }
  }



  //  Extraer el nombre de usuario
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  //  Extraer un claim específico
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  // Extraer todos los claims del token
/*  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(SECRET_KEY)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }*/

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser()
          .verifyWith(SECRET_KEY)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      log.error("El token ha expirado: {}", e.getMessage());
      throw new RuntimeException("Token expirado");
    } catch (UnsupportedJwtException e) {
      log.error("Formato de token no soportado: {}", e.getMessage());
      throw new RuntimeException("Token no soportado");
    } catch (MalformedJwtException e) {
      log.error("Token mal formado: {}", e.getMessage());
      throw new RuntimeException("Token mal formado");
    } catch (SignatureException e) {
      log.error("Firma del token no válida: {}", e.getMessage());
      throw new RuntimeException("Firma no válida");
    } catch (IllegalArgumentException e) {
      log.error("El token está vacío o es nulo: {}", e.getMessage());
      throw new RuntimeException("Token inválido");
    }
  }



  // ✅ Verificar si el token ha expirado
  public boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }
}