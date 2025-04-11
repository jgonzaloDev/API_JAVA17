/*
package com.silmaur.shop.security;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    log.debug(" Token recibido: {}", token);

    return Mono.justOrEmpty(token)
        .filter(jwtProvider::validateToken)
        .flatMap(validToken -> Mono.justOrEmpty(jwtProvider.extractUsername(validToken)))
        .doOnNext(username -> log.debug("✅ Usuario extraído del token: {}", username))
        .flatMap(userDetailsService::findByUsername)
        .flatMap(userDetails -> {
          log.debug(" Usuario encontrado en BD: {}", userDetails.getUsername());

          // ✅ Mapeo de roles asegurando prefijo "ROLE_"
          List<GrantedAuthority> authorities = userDetails.getAuthorities().stream()
              .map(role -> {
                String authority = role.getAuthority();
                return authority.startsWith("ROLE_") ? authority : "ROLE_" + authority; // Agregar "ROLE_" si no lo tiene
              })
              .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
              .collect(Collectors.toList());

          log.info("️  Roles asignados al usuario {}: {}", userDetails.getUsername(), authorities);

          Authentication auth = new UsernamePasswordAuthenticationToken(
              userDetails, null, authorities);

          return chain.filter(exchange)
              .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        })
        .switchIfEmpty(Mono.defer(() -> {
          log.warn("Token inválido o no proporcionado. Solicitud no autorizada.");
          return chain.filter(exchange); // Permitir la solicitud sin autenticación
        }));
  }

  private String getTokenFromRequest(ServerHttpRequest request) {
    String bearerToken = request.getHeaders().getFirst("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      String token = bearerToken.substring(7);
      log.debug("️  Token extraído del header: {}", token);
      return token;
    }
    return null;
  }
}*/
