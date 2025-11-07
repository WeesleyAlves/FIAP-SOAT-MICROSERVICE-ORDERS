package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.*;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.use_cases.CreateOrderUseCase;
import br.com.fiap.techchallenge.orders.core.use_cases.OrderNumberGeneratorUseCase;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;


public class PublicController {
    private final OrderDatasource orderDatasource;
    private final PaymentDatasource paymentDatasource;
    private final ProductsDatasource productsDatasource;
    private final InventoryDatasource inventoryDatasource;

    public PublicController(
        OrderDatasource orderDatasource,
        PaymentDatasource paymentDatasource,
        ProductsDatasource productsDatasource,
        InventoryDatasource inventoryDatasource
    ){
        this.orderDatasource = orderDatasource;
        this.paymentDatasource = paymentDatasource;
        this.productsDatasource = productsDatasource;
        this.inventoryDatasource = inventoryDatasource;
    }

    public CompleteOrderDTO createOrder(NewOrderDTO dto) {
        var orderGateway = new OrderGateway( orderDatasource );
        var orderNumberGateway = new OrderNumberGateway( orderDatasource );
        var paymentGateway = new PaymentGateway( paymentDatasource );
        var productGateway = new ProductGateway( productsDatasource );
        var inventoryGateway = new InventoryGateway( inventoryDatasource );

        var orderNumberUseCase = new OrderNumberGeneratorUseCase( orderNumberGateway );
        var createOrderUseCase = new CreateOrderUseCase( orderGateway, productGateway );

        var orderNumber = orderNumberUseCase.getNextOrderNumber();

        var persistedOrder = createOrderUseCase.run(dto, orderNumber);
        var createdPayment = paymentGateway.createPayment();

        persistedOrder.setPayment(createdPayment);

        inventoryGateway.updateInventory();

        return OrderPresenter.createCompleteOrderDTO(persistedOrder);
    }
}
