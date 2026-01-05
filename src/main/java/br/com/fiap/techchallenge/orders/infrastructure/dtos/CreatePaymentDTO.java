package br.com.fiap.techchallenge.orders.infrastructure.dtos;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record CreatePaymentDTO(
        UUID orderId,
        Optional<UUID> customerId,
        BigDecimal amount
) {
}
