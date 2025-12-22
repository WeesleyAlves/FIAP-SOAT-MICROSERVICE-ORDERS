package br.com.fiap.techchallenge.orders.infrastructure.dtos;

import java.util.UUID;

public record UpdateInventoryDTO(
        UUID productId,
        Integer quantity
) {
}
