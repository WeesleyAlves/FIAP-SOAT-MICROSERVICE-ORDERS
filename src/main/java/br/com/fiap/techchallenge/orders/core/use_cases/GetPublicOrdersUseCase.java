package br.com.fiap.techchallenge.orders.core.use_cases;

import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.utils.comparators.OrderComparator;

import java.util.List;

public class GetPublicOrdersUseCase {
    private final OrderGateway orderGateway;

    public GetPublicOrdersUseCase(
        OrderGateway orderGateway
    ) {
        this.orderGateway = orderGateway;
    }

    public List<CompleteOrderEntity> run() {
        List<CompleteOrderEntity> orderList = orderGateway.getAllPublic();

        return orderList.stream()
            .sorted(OrderComparator.run())
            .toList();
    }
}
