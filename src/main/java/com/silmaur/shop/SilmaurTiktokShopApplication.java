package com.silmaur.shop;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(scanBasePackages = "com.silmaur.shop")
@EnableAspectJAutoProxy // Habilita AspectJ para @Async y otras funcionalidades
public class SilmaurTiktokShopApplication {

  public static void main(String[] args) {
    // 1. Configurar la estrategia de SecurityContextHolder al inicio
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);

    // 2. Configurar RxJavaPlugins para propagar el SecurityContext
    RxJavaPlugins.setScheduleHandler(runnable ->
        new DelegatingSecurityContextRunnable(runnable, SecurityContextHolder.getContext())
    );

    // 3. Iniciar la aplicación Spring Boot
    SpringApplication.run(SilmaurTiktokShopApplication.class, args);
  }
}