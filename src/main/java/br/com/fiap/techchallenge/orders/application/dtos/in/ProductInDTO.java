package br.com.fiap.techchallenge.orders.application.dtos.in;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductInDTO (
    UUID id,
    String name,
    BigDecimal price
){}
