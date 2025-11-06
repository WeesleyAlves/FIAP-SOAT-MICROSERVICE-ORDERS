package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentAdapter implements PaymentDatasource {

    @Override
    public PaymentInDTO createPayment() {
        return new PaymentInDTO(
            UUID.randomUUID(),
            "qr_code_data"
        );
    }
}
