package br.com.fiap.techchallenge.orders.application.dtos.out;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProductItemOutDTO(
        UUID id,
        String name,
        BigDecimal price,
        int quantity,
        BigDecimal totalValue
) {}
