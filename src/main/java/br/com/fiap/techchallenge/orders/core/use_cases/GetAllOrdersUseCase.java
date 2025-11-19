package br.com.fiap.techchallenge.orders.core.use_cases;

import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.utils.comparators.OrderComparator;

import java.util.List;

public class GetAllOrdersUseCase {
    OrderGateway orderGateway;

    public GetAllOrdersUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public List<CompleteOrderEntity> run(){
        List<CompleteOrderEntity> orderList = orderGateway.getAll();

        return orderList.stream()
            .sorted(OrderComparator.run())
            .toList();
    }
}
