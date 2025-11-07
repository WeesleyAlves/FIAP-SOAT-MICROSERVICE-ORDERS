package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.dtos.out.QueueOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.application.gateways.OrderNumberGateway;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.use_cases.GetAllOrdersUseCase;
import br.com.fiap.techchallenge.orders.core.use_cases.OrderNumberGeneratorUseCase;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;

import java.util.List;

public class AdminController {
    private final OrderDatasource orderDatasource;

    public AdminController(OrderDatasource orderDatasource) {
        this.orderDatasource = orderDatasource;
    }

    public void resetOrderNumberSequence(){
        var orderNumberGateway = new OrderNumberGateway( orderDatasource );
        var orderNumberGeneratorUseCase = new OrderNumberGeneratorUseCase(orderNumberGateway);

        orderNumberGeneratorUseCase.resetOrderNumberSequence();
    }

    public List<QueueOrderDTO> getAllOrders(){
        OrderGateway orderGateway = new OrderGateway( orderDatasource );

        var getAllOrdersUseCase = new GetAllOrdersUseCase( orderGateway );

        var ordersList = getAllOrdersUseCase.run();

        return OrderPresenter.createQueueOrderDTO(ordersList);
    }
}
