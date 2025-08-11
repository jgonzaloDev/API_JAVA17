package com.silmaur.shop.service.impl;

import com.silmaur.shop.dto.DraftOrderDTO;
import com.silmaur.shop.dto.OrderDTO;
import com.silmaur.shop.exception.NotFoundException;
import com.silmaur.shop.handler.mapper.DraftOrderMapper;
import com.silmaur.shop.dto.DraftOrderItemDTO;
import com.silmaur.shop.handler.mapper.OrderMapper;
import com.silmaur.shop.model.DraftOrder;
import com.silmaur.shop.model.DraftOrderItem;
import com.silmaur.shop.model.Order;
import com.silmaur.shop.model.OrderItem;
import com.silmaur.shop.repository.DraftOrderItemRepository;
import com.silmaur.shop.repository.DraftOrderRepository;
import com.silmaur.shop.repository.OrderItemRepository;
import com.silmaur.shop.repository.OrderRepository;
import com.silmaur.shop.repository.ProductRepository;
import com.silmaur.shop.service.DraftOrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class DraftOrderServiceImpl implements DraftOrderService {

  private final DraftOrderRepository draftOrderRepository;
  private final DraftOrderItemRepository draftOrderItemRepository;
  private final DraftOrderMapper draftOrderMapper;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final OrderMapper orderMapper;
  private final ProductRepository productRepository; // ✅ Agrega esta línea

  @Override
  public Mono<DraftOrder> createDraftOrder(DraftOrderDTO dto) {
    DraftOrder draftOrder = draftOrderMapper.toEntity(dto);

    return draftOrderRepository.save(draftOrder)
        .flatMap(savedOrder -> {
          Flux<DraftOrderItem> items = Flux.fromIterable(dto.getItems())
              .map(draftOrderMapper::toItemEntity)
              .map(item -> {
                item.setDraftOrderId(savedOrder.getId());
                return item;
              });
          return items.flatMap(draftOrderItemRepository::save)
              .then(Mono.just(savedOrder));
        });
  }

  @Override
  public Flux<DraftOrderDTO> findByLiveSessionId(Long sessionId) {
    return draftOrderRepository.findByLiveSessionId(sessionId)
        // 🟢 Ordenar completados por completedAt DESC, pendientes se quedan como están
        .sort((o1, o2) -> {
          if ("COMPLETADO".equals(o1.getStatus()) && "COMPLETADO".equals(o2.getStatus())) {
            return o2.getCompletedAt().compareTo(o1.getCompletedAt());
          }
          return 0; // no altera el orden de pendientes
        })
        .flatMap(draftOrder ->
            draftOrderItemRepository.findByDraftOrderId(draftOrder.getId())
                .flatMap(item ->
                    productRepository.findById(item.getProductId())
                        .map(product -> {
                          item.setProductName(product.getName()); // 🟢 enriquecemos dinámicamente
                          return item;
                        })
                        .defaultIfEmpty(item) // por si el producto no existe
                )
                .collectList()
                .map(items -> DraftOrderDTO.builder()
                    .id(draftOrder.getId())
                    .liveSessionId(draftOrder.getLiveSessionId())
                    .customerId(draftOrder.getCustomerId())
                    .nickname(draftOrder.getNickname())
                    .totalAmount(draftOrder.getTotalAmount())
                    .createdAt(draftOrder.getCreatedAt())
                    .completedAt(draftOrder.getCompletedAt()) // 🟢 agregado
                    .status(draftOrder.getStatus())
                    .items(items.stream().map(item -> DraftOrderItemDTO.builder()
                        .id(item.getId())
                        .draftOrderId(item.getDraftOrderId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .build()).toList())
                    .build())
        );
  }




  @Override
  public Mono<Void> deleteById(Long id) {
    return draftOrderRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Draft order no encontrado")))
        .flatMap(draft ->
            draftOrderItemRepository.findByDraftOrderId(id)
                .flatMap(draftOrderItemRepository::delete)
                .then(draftOrderRepository.deleteById(id))
        );
  }


  @Override
  public Mono<Void> updateStatus(Long id, String status) {
    return draftOrderRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Draft order no encontrado")))
        .flatMap(order -> {
          order.setStatus(status);
          return draftOrderRepository.save(order);
        })
        .then();
  }


  @Override
  public Mono<OrderDTO> confirmDraftOrder(Long draftOrderId) {
    return draftOrderRepository.findById(draftOrderId)
        .switchIfEmpty(Mono.error(new NotFoundException("Draft order no encontrado")))
        .flatMap(draftOrder -> {

          // 🟢 Marcar como completado en la entidad DraftOrder
          draftOrder.setCompletedAt(LocalDateTime.now());
          draftOrder.setStatus("COMPLETADO");

          return draftOrderRepository.save(draftOrder) // Guardar cambios en el borrador antes de crear Order
              .then(
                  draftOrderItemRepository.findByDraftOrderId(draftOrderId)
                      .collectList()
                      .flatMap(draftItems -> {
                        // 1️⃣ Crear Order base
                        Order order = Order.builder()
                            .customerId(draftOrder.getCustomerId())
                            .liveSessionId(draftOrder.getLiveSessionId())
                            .campaignId(null)
                            .aperture(BigDecimal.ZERO)
                            .totalAmount(draftOrder.getTotalAmount())
                            .realAmountToPay(draftOrder.getTotalAmount())
                            .originType("LIVE")
                            .accumulation(false)
                            .status("NO_PAGADO")
                            .paymentDueDate(LocalDateTime.now().plusDays(7))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                        // 2️⃣ Guardar Order
                        return orderRepository.save(order)
                            .flatMap(savedOrder ->
                                Flux.fromIterable(draftItems)
                                    .flatMap(draftItem ->
                                        productRepository.findById(draftItem.getProductId())
                                            .switchIfEmpty(Mono.error(new NotFoundException(
                                                "Producto no encontrado: " + draftItem.getProductId())))
                                            .map(product ->
                                                OrderItem.builder()
                                                    .orderId(savedOrder.getId())
                                                    .productId(draftItem.getProductId())
                                                    .productName(product.getName())
                                                    .quantity(draftItem.getQuantity())
                                                    .price(draftItem.getUnitPrice())
                                                    .discount(BigDecimal.ZERO)
                                                    .build()
                                            )
                                    )
                                    .collectList()
                                    .flatMap(orderItems -> orderItemRepository.saveAll(orderItems)
                                        .then(Mono.just(savedOrder))
                                    )
                            );
                      })
              );
        })
        // 🔹 Ya NO eliminamos el draft order
        .flatMap(savedOrder ->
            orderItemRepository.findByOrderId(savedOrder.getId())
                .collectList()
                .map(items -> {
                  savedOrder.setItems(items);
                  return savedOrder;
                })
        )
        .map(orderMapper::toDto);
  }


  @Override
  public Mono<DraftOrder> updateDraftOrder(Long id, DraftOrderDTO dto) {
    return draftOrderRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Pedido en borrador no encontrado")))
        .flatMap(existing -> {
          existing.setTotalAmount(dto.getTotalAmount());
          existing.setNickname(dto.getNickname());
          existing.setCustomerId(dto.getCustomerId());
          existing.setLiveSessionId(dto.getLiveSessionId());
          return draftOrderRepository.save(existing);
        })
        .flatMap(saved -> {
          return draftOrderItemRepository.deleteByDraftOrderId(id)
              .thenMany(Flux.fromIterable(dto.getItems()))
              .flatMap(itemDto -> {
                DraftOrderItem item = DraftOrderItem.builder()
                    .draftOrderId(id)
                    .productId(itemDto.getProductId())
                    .productName(itemDto.getProductName())
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getUnitPrice())
                    .build();
                return draftOrderItemRepository.save(item);
              })
              .then(Mono.just(saved));
        });
  }


}