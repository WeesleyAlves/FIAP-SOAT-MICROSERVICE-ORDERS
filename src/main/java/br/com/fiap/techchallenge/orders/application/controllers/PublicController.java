package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.application.gateways.OrderNumberGateway;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.use_cases.CreateOrderUseCase;
import br.com.fiap.techchallenge.orders.core.use_cases.OrderNumberGeneratorUseCase;
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
        var orderNumberGateway = new OrderNumberGateway( orderDatasource );

        var orderNumberUseCase = new OrderNumberGeneratorUseCase( orderNumberGateway );
        var createOrderUseCase = new CreateOrderUseCase( orderGateway );

        var orderNumber = orderNumberUseCase.getNextOrderNumber();


        var persistedOrder = createOrderUseCase.run(dto, orderNumber);

        return OrderPresenter.createCompleteOrderDTO(persistedOrder);
    }
}
