package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.use_cases.CreateOrderUseCase;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;

public class PublicController {
    private final OrderDatasource orderDatasource;

    public PublicController(
        OrderDatasource orderDatasource
    ){
        this.orderDatasource = orderDatasource;
    }

    public CompleteOrderDTO createOrder(NewOrderDTO dto) {
        var orderGateway = new OrderGateway( orderDatasource );

        var createOrderUseCase = new CreateOrderUseCase( orderGateway );

        var persistedOrder = createOrderUseCase.run(dto, 1);

        return OrderPresenter.createCompleteOrderDTO(persistedOrder);
    }
}
