package br.com.fiap.techchallenge.orders.application.gateways;

import br.com.fiap.techchallenge.orders.core.entities.OrderPaymentEntity;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;

import java.util.Optional;
import java.util.UUID;

public class PaymentGateway {
    private final PaymentDatasource paymentDatasource;

    public PaymentGateway(
            PaymentDatasource paymentDatasource
    ) {
        this.paymentDatasource = paymentDatasource;
    }

    public OrderPaymentEntity createPayment() {
        var result = paymentDatasource.createPayment();

        return  new OrderPaymentEntity(
            result.uuid(),
            result.qrData()
        );
    }

    public Optional<OrderPaymentEntity> getByOrderID(UUID orderId) {
        var result = paymentDatasource.getPaymentByOrderId( orderId);

        return Optional.of(
            new OrderPaymentEntity(
                result.uuid(),
                result.qrData()
            )
        );
    }
}
