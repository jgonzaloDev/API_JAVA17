package com.silmaur.shop.repository;

import com.silmaur.shop.model.LiveSession;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LiveSessionRepository extends ReactiveCrudRepository<LiveSession, Long> {

}
