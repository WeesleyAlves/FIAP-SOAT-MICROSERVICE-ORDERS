package br.com.fiap.techchallenge.orders.infrastructure.datasources;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;

import java.util.UUID;

public interface PaymentDatasource {
    PaymentInDTO createPayment();
    PaymentInDTO getPaymentByOrderId(UUID orderId);
}
