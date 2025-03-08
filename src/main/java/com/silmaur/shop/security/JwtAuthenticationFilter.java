package com.silmaur.shop.security;

import java.util.Optional;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

  private final JwtProvider jwtProvider;
  private final ReactiveUserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtProvider jwtProvider, ReactiveUserDetailsService userDetailsService) {
    this.jwtProvider = jwtProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = getTokenFromRequest(exchange.getRequest());

    return Mono.justOrEmpty(token) // ✅ Manejamos el token de forma reactiva
        .filter(jwtProvider::validateToken) // ✅ Filtra solo tokens válidos
        .flatMap(validToken -> Mono.justOrEmpty(jwtProvider.extractUsername(validToken))) // ✅ Extrae Optional<String> y lo envuelve en Mono<Optional<String>>
        .flatMap(Mono::justOrEmpty) // ✅ Convierte Mono<Optional<String>> en Mono<String>
        .flatMap(userDetailsService::findByUsername) // ✅ Busca el usuario en la BD
        .flatMap(userDetails -> {
          Authentication auth = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());

          return chain.filter(exchange)
              .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)); // ✅ Guarda la autenticación en el contexto de seguridad reactivo
        })
        .switchIfEmpty(chain.filter(exchange)); // ✅ Si no hay usuario válido, continúa sin autenticación
  }









  private String getTokenFromRequest(ServerHttpRequest request) {
    String bearerToken = request.getHeaders().getFirst("Authorization");
    return (bearerToken != null && bearerToken.startsWith("Bearer "))
        ? bearerToken.substring(7)
        : null;
  }
}
