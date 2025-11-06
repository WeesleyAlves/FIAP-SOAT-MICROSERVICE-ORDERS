package br.com.fiap.techchallenge.orders.application.dtos.out;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderDTO(
    UUID customerId,
    String notes,
    Integer orderNumber,
    BigDecimal price,
    String status
){}
