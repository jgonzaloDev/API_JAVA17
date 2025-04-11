package com.silmaur.shop.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;

@Configuration
public class WebFluxUtf8Config implements WebFluxConfigurer {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    ObjectMapper objectMapper = new ObjectMapper();

    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));

    objectMapper.registerModule(javaTimeModule);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    configurer.defaultCodecs().jackson2JsonEncoder(
        new Jackson2JsonEncoder(
            objectMapper,
            new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
        )
    );
  }
}

