/*
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    authManager.setPasswordEncoder(passwordEncoder());
    return authManager;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll()) // Permitir todas las solicitudes
        .build();
  }
  */
/*
  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        authorizeExchange(exchanges -> exchanges
            .pathMatchers("/api/auth/login", "/api/users/create", "/api/users/list").permitAll()
            .pathMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "USER")
            .pathMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
            .pathMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
            .pathMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
            .pathMatchers(HttpMethod.GET, "/api/customers/**").hasAnyRole("ADMIN", "USER")
            .pathMatchers(HttpMethod.POST, "/api/customers/**").hasRole("ADMIN")
            .pathMatchers(HttpMethod.PUT, "/api/customers/**").hasRole("ADMIN")
            .pathMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole("ADMIN")
            .anyExchange().authenticated())
        .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll()) // Permitir todas las solicitudes
        .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();
  }*//*


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
*/
