package com.silmaur.shop.repository.impl;

import com.silmaur.shop.model.ClienteDeuda;
import com.silmaur.shop.model.LiveSessionSummary;
import com.silmaur.shop.model.ProductoResumen;
import com.silmaur.shop.repository.CustomLiveSessionRepository;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomLiveSessionRepositoryImpl implements CustomLiveSessionRepository {

  private final ConnectionFactory connectionFactory;

  @Override
  public Mono<LiveSessionSummary> getSessionSummary(Long sessionId) {
    Mono<BigDecimal> totalVendido = queryTotalVendido(sessionId);
    Mono<Integer> totalOrdenes = queryTotalOrdenes(sessionId);
    Mono<Integer> productosDistintos = queryProductosDistintos(sessionId);
    Mono<List<ProductoResumen>> topProductos = queryTopProductos(sessionId);
    Mono<List<ClienteDeuda>> clientesConDeuda = queryClientesConDeuda(sessionId);

    return Mono.zip(totalVendido, totalOrdenes, productosDistintos, topProductos, clientesConDeuda)
        .map(tuple -> LiveSessionSummary.builder()
            .sessionId(sessionId)
            .totalVendido(tuple.getT1())
            .totalOrdenes(tuple.getT2())
            .productosDistintos(tuple.getT3())
            .topProductos(tuple.getT4())
            .clientesConDeuda(tuple.getT5())
            .build());
  }

  private Mono<BigDecimal> queryTotalVendido(Long sessionId) {
    String sql = """
      SELECT COALESCE(SUM(total_amount), 0) AS total
      FROM orders
      WHERE live_session_id = $1
      """;

    return Mono.usingWhen(
        connectionFactory.create(),
        conn -> Mono.from(conn.createStatement(sql)
                .bind(0, sessionId)
                .execute())
            .flatMapMany(result -> Flux.from(result.map((row, meta) -> row.get("total", BigDecimal.class))))
            .single()
            .defaultIfEmpty(BigDecimal.ZERO),
        conn -> conn.close()
    );
  }


  private Mono<Integer> queryTotalOrdenes(Long sessionId) {
    String sql = """
      SELECT COUNT(*) AS total
      FROM orders
      WHERE live_session_id = $1
      """;

    return Mono.usingWhen(
        connectionFactory.create(),
        conn -> Mono.from(conn.createStatement(sql)
                .bind(0, sessionId)
                .execute())
            .flatMapMany(result -> Flux.from(result.map((row, meta) -> row.get("total", Integer.class))))
            .single()
            .defaultIfEmpty(0),
        conn -> conn.close()
    );
  }


  private Mono<Integer> queryProductosDistintos(Long sessionId) {
    String sql = """
      SELECT COUNT(DISTINCT oi.product_id) AS total
      FROM order_items oi
      JOIN orders o ON o.id = oi.order_id
      WHERE o.live_session_id = $1
      """;

    return Mono.usingWhen(
        connectionFactory.create(),
        conn -> Mono.from(conn.createStatement(sql)
                .bind(0, sessionId)
                .execute())
            .flatMapMany(result -> Flux.from(result.map((row, meta) ->
                row.get("total", Integer.class)
            )))
            .single()
            .defaultIfEmpty(0),
        conn -> conn.close()
    );
  }


  private Mono<List<ProductoResumen>> queryTopProductos(Long sessionId) {
    String sql = """
      SELECT oi.product_id, oi.product_name,
             SUM(oi.quantity) AS cantidad,
             SUM(oi.price * oi.quantity) AS total
      FROM order_items oi
      JOIN orders o ON o.id = oi.order_id
      WHERE o.live_session_id = $1
      GROUP BY oi.product_id, oi.product_name
      ORDER BY cantidad DESC
      LIMIT 10
      """;

    return Mono.usingWhen(
        connectionFactory.create(),
        conn -> Flux.from(conn.createStatement(sql)
                .bind(0, sessionId)
                .execute())
            .flatMap(result -> result.map((row, meta) -> ProductoResumen.builder()
                .productId(row.get("product_id", Long.class))
                .nombre(row.get("product_name", String.class))
                .cantidadVendida(row.get("cantidad", Integer.class))
                .totalGenerado(row.get("total", BigDecimal.class))
                .build()))
            .collectList(),
        conn -> conn.close()
    );
  }

  private Mono<List<ClienteDeuda>> queryClientesConDeuda(Long sessionId) {
    String sqlClientes = """
      SELECT c.id,
             COALESCE(c.nickname, c.name, 'Cliente sin nombre') AS nombre,
             COALESCE(c.initial_deposit, 0) AS initial_deposit,
             SUM(o.real_amount_to_pay) AS total_pedido,
             -- Deuda: pedidos que superan el depósito inicial
             GREATEST(SUM(o.real_amount_to_pay) - COALESCE(c.initial_deposit,0), 0) AS deuda,
             -- Saldo a favor: depósito inicial menos lo que ya gastó
             GREATEST(COALESCE(c.initial_deposit,0) - SUM(o.real_amount_to_pay), 0) AS saldo_a_favor
      FROM orders o
      JOIN customers c ON c.id = o.customer_id
      WHERE o.live_session_id = $1
      GROUP BY c.id, nombre, c.initial_deposit
      """;

    String sqlProductos = """
      SELECT o.customer_id,
             oi.product_id,
             oi.product_name,
             SUM(oi.quantity) AS cantidad,
             SUM(oi.price * oi.quantity) AS total
      FROM order_items oi
      JOIN orders o ON o.id = oi.order_id
      WHERE o.live_session_id = $1
      GROUP BY o.customer_id, oi.product_id, oi.product_name
      """;

    log.info("🔍 Ejecutando queryClientesConDeuda + productos para sessionId={}", sessionId);

    Mono<List<ClienteDeuda>> clientesMono = Mono.usingWhen(
        connectionFactory.create(),
        conn -> Flux.from(conn.createStatement(sqlClientes).bind(0, sessionId).execute())
            .flatMap(result -> result.map((row, meta) -> {
              BigDecimal deuda = row.get("deuda", BigDecimal.class);
              BigDecimal saldoAFavor = row.get("saldo_a_favor", BigDecimal.class);
              BigDecimal initialDeposit = row.get("initial_deposit", BigDecimal.class);

              if (deuda == null) deuda = BigDecimal.ZERO;
              if (saldoAFavor == null) saldoAFavor = BigDecimal.ZERO;
              if (initialDeposit == null) initialDeposit = BigDecimal.ZERO;

              log.debug("🧾 Cliente={} | initialDeposit={} | deuda={} | saldoAFavor={}",
                  row.get("nombre", String.class), initialDeposit, deuda, saldoAFavor);

              return ClienteDeuda.builder()
                  .customerId(row.get("id", Long.class))
                  .nombre(row.get("nombre", String.class))
                  .initialDeposit(initialDeposit)
                  .deuda(deuda)
                  .saldoAFavor(saldoAFavor)
                  .productos(new ArrayList<>()) // se llena después
                  .build();
            }))
            .collectList(),
        conn -> conn.close()
    );

    Mono<Map<Long, List<ProductoResumen>>> productosMono = Mono.usingWhen(
        connectionFactory.create(),
        conn -> Flux.from(conn.createStatement(sqlProductos).bind(0, sessionId).execute())
            .flatMap(result -> result.map((row, meta) -> {
              ProductoResumen p = ProductoResumen.builder()
                  .productId(row.get("product_id", Long.class))
                  .nombre(row.get("product_name", String.class))
                  .cantidadVendida(row.get("cantidad", Integer.class))
                  .totalGenerado(row.get("total", BigDecimal.class))
                  .build();
              return Pair.of(row.get("customer_id", Long.class), p);
            }))
            .collectMultimap(Pair::getFirst, Pair::getSecond)
            .map(m -> m.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> new ArrayList<>(e.getValue())
                ))),
        conn -> conn.close()
    );

    return Mono.zip(clientesMono, productosMono)
        .map(tuple -> {
          List<ClienteDeuda> clientes = tuple.getT1();
          Map<Long, List<ProductoResumen>> productosPorCliente = tuple.getT2();
          clientes.forEach(c ->
              c.setProductos(productosPorCliente.getOrDefault(c.getCustomerId(), new ArrayList<>()))
          );
          return clientes;
        });
  }










}
