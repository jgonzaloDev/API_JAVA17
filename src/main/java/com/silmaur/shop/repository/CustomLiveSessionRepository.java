package com.silmaur.shop.repository;


import com.silmaur.shop.model.LiveSessionSummary;
import reactor.core.publisher.Mono;

public interface CustomLiveSessionRepository {
  Mono<LiveSessionSummary> getSessionSummary(Long sessionId);

}
