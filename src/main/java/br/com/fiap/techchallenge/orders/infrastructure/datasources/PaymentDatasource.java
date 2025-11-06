package br.com.fiap.techchallenge.orders.infrastructure.datasources;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;

public interface PaymentDatasource {
    PaymentInDTO createPayment();
}
