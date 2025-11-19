package br.com.fiap.techchallenge.orders.application.dtos.in;

import java.util.UUID;

public record PathOrderDTO(
        UUID id,
        Integer status_id
) {}
