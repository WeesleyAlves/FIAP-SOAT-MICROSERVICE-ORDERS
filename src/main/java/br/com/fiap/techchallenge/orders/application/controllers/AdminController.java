package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.gateways.OrderNumberGateway;
import br.com.fiap.techchallenge.orders.core.use_cases.OrderNumberGeneratorUseCase;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;

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
}
