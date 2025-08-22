/*
package com.silmaur.shop.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

  @PostConstruct
  public void setTimeZone() {
    // Fuerza hora Lima en toda la JVM
    TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
    System.out.println("✅ Zona horaria seteada a: " + TimeZone.getDefault().getID());
  }
}*/
