package com.silmaur.shop.repository;

import com.silmaur.shop.model.LiveSessionSale;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LiveSessionSaleRepository extends ReactiveCrudRepository<LiveSessionSale, Long> {

  // 📜 Histórico completo (incluye archivadas)
  Flux<LiveSessionSale> findByLiveSessionId(Long liveSessionId);

  // 📜 Histórico filtrando solo las no archivadas
  Flux<LiveSessionSale> findByLiveSessionIdAndArchivedFalse(Long liveSessionId);

  // 📜 Ventas visibles (no completadas y no archivadas)
  @Query("""
        SELECT lss.*
        FROM live_session_sales lss
        WHERE lss.live_session_id = :sessionId
          AND lss.archived = FALSE
          AND NOT EXISTS (
              SELECT 1
              FROM draft_orders d
              WHERE d.customer_id = lss.customer_id
                AND d.live_session_id = :sessionId
                AND d.status = 'COMPLETADO'
          )
        """)
  Flux<LiveSessionSale> findVisibleBySessionId(Long sessionId);

  // 📜 Ventas por cliente (para archivarlas todas juntas)
  Flux<LiveSessionSale> findByLiveSessionIdAndCustomerId(Long liveSessionId, Long customerId);
}
