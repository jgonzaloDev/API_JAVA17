package com.silmaur.shop.security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final ReactiveUserDetailsService userDetailsService;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ReactiveUserDetailsService userDetailsService) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ReactiveAuthenticationManager authenticationManager() {
    UserDetailsRepositoryReactiveAuthenticationManager authManager =
        new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    authManager.setPasswordEncoder(passwordEncoder()); // Para validar contraseñas encriptadas
    return authManager;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Sesiones sin estado
        .authorizeExchange(exchanges -> exchanges
            .pathMatchers("/h2-console/**", "/api/auth/login", "/api/users").permitAll()
            .anyExchange().authenticated())
        .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }
/*
          .authorizeExchange(exchanges -> exchanges
      .pathMatchers("/h2-console/**", "/api/auth/login").permitAll()
            .pathMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN") // Solo ADMIN puede crear usuarios
            .anyExchange().authenticated())*/

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowCredentials(true); // Si necesitas autenticación con cookies o cabeceras seguras
    configuration.setAllowedMethods(List.of(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name(),
        HttpMethod.OPTIONS.name()));
    configuration.setAllowedHeaders(List.of("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
