package br.com.fiap.techchallenge.orders.infrastructure.datasources;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.dtos.CreatePaymentDTO;

import java.util.UUID;

public interface PaymentDatasource {
    PaymentInDTO createPayment( CreatePaymentDTO createPaymentDTO );
    PaymentInDTO getPaymentByOrderId(UUID orderId);
}
