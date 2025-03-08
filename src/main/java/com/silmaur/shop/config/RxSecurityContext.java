/*
package com.silmaur.shop.config;

import io.reactivex.rxjava3.core.SingleTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class RxSecurityContext {

  private static final Logger log = LoggerFactory.getLogger(RxSecurityContext.class);

  public static <T> SingleTransformer<T, T> propagate(SecurityContext securityContext) {
    return upstream -> upstream
        .doOnSubscribe(disposable -> {
          log.debug("Estableciendo SecurityContext para el usuario: {}",
              securityContext.getAuthentication() != null ? securityContext.getAuthentication().getName() : "anonymous");
          SecurityContextHolder.setContext(securityContext);
        })
        .doFinally(() -> {
          log.debug("Limpiando SecurityContext");
          SecurityContextHolder.clearContext();
        });
  }
}
*/
