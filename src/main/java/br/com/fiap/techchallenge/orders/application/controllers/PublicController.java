package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.*;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.use_cases.CreateOrderUseCase;
import br.com.fiap.techchallenge.orders.core.use_cases.OrderNumberGeneratorUseCase;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;

public class PublicController {
    private final OrderDatasource orderDatasource;
    private final PaymentDatasource paymentDatasource;
    private final ProductsDatasource productsDatasource;

    public PublicController(
        OrderDatasource orderDatasource,
        PaymentDatasource paymentDatasource,
        ProductsDatasource productsDatasource
    ){
        this.orderDatasource = orderDatasource;
        this.paymentDatasource = paymentDatasource;
        this.productsDatasource = productsDatasource;
    }

    public CompleteOrderDTO createOrder(NewOrderDTO dto) {
        var orderGateway = new OrderGateway( orderDatasource );
        var orderNumberGateway = new OrderNumberGateway( orderDatasource );
        var paymentGateway = new PaymentGateway( paymentDatasource );
        var productGateway = new ProductGateway( productsDatasource );
        var orderProductsGateway = new OrderProductsGateway( orderDatasource );

        var orderNumberUseCase = new OrderNumberGeneratorUseCase( orderNumberGateway );
        var createOrderUseCase = new CreateOrderUseCase( orderGateway, productGateway, orderProductsGateway );

        var orderNumber = orderNumberUseCase.getNextOrderNumber();

        var persistedOrder = createOrderUseCase.run(dto, orderNumber);
        var createdPayment = paymentGateway.createPayment();

        persistedOrder.setPayment(createdPayment);

        return OrderPresenter.createCompleteOrderDTO(persistedOrder);
    }
}
