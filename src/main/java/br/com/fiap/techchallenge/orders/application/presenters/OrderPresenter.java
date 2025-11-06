package br.com.fiap.techchallenge.orders.application.presenters;


import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.entities.OrderPaymentEntity;

import java.util.Optional;
import java.util.UUID;

public class OrderPresenter {

    private OrderPresenter() {
        throw new IllegalStateException("Order presenter não pode ser instanciado, é uma classe de utilidade.");
    }

    public static CompleteOrderDTO createCompleteOrderDTO(CompleteOrderEntity order) {
        OrderPaymentEntity payment = order.getPayment();
        Optional<UUID> paymentId = Optional.ofNullable(payment).map(OrderPaymentEntity::getId);
        Optional<String> paymentQrData = Optional.ofNullable(payment).map(OrderPaymentEntity::getQrData);

        return new CompleteOrderDTO(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getCustomerId(),
                order.getNotes(),
                order.getOriginalPrice(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                Optional.ofNullable( order.getProducts() ),
                paymentId,
                paymentQrData
        );
    }
}