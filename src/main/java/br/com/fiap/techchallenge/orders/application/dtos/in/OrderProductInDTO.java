package br.com.fiap.techchallenge.orders.application.dtos.in;

import java.util.UUID;

public record OrderProductInDTO(
    UUID id,
    Integer quantity
) {}
