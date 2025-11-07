package br.com.fiap.techchallenge.orders.core.use_cases;

import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;

import java.util.UUID;

public class GetCompleteOrderByIdUseCase {
    private final OrderGateway orderGateway;

    public GetCompleteOrderByIdUseCase(
        OrderGateway orderGateway
    ) {
        this.orderGateway = orderGateway;
    }

    public CompleteOrderEntity run(UUID orderId) {
        return orderGateway.findById(orderId);
    }
}
