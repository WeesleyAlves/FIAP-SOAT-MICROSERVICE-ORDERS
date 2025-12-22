package br.com.fiap.techchallenge.orders.application.dtos.in;

import java.util.UUID;

public record PaymentInDTO(
        UUID id,
        String qrData
){}
