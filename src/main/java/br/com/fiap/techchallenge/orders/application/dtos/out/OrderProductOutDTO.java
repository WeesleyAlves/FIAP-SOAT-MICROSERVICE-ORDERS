package br.com.fiap.techchallenge.orders.application.dtos.out;

import java.util.UUID;

public record OrderProductOutDTO(
        UUID orderId,
        UUID productId,
        Integer quantity
) {}
