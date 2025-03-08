package com.silmaur.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(scanBasePackages = "com.silmaur.shop")
@EnableAspectJAutoProxy // Habilita AspectJ para @Async y otras funcionalidades
public class SilmaurTiktokShopApplication {

  public static void main(String[] args) {

    SpringApplication.run(SilmaurTiktokShopApplication.class, args);
  }
}