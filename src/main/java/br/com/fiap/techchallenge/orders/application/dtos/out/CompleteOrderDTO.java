package br.com.fiap.techchallenge.orders.application.dtos.out;

import br.com.fiap.techchallenge.orders.core.entities.OrderProductItemEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record CompleteOrderDTO(
    UUID id,
    Integer order_number,
    String status,
    UUID customer_id,
    String notes,
    BigDecimal price,
    LocalDateTime created_at,
    LocalDateTime updated_at,
    Optional<List<OrderProductItemEntity>> products,
    Optional<UUID> payment_id,
    Optional<String> payment_qr_data
) {}
