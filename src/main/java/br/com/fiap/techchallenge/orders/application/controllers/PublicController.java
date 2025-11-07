package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.QueueOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.*;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.use_cases.CreateOrderUseCase;
import br.com.fiap.techchallenge.orders.core.use_cases.GetCompleteOrderByIdUseCase;
import br.com.fiap.techchallenge.orders.core.use_cases.GetPublicOrdersUseCase;
import br.com.fiap.techchallenge.orders.core.use_cases.OrderNumberGeneratorUseCase;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.InventoryDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.PaymentDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;

import java.util.List;
import java.util.UUID;


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

    public List<QueueOrderDTO> getPublicOrders() {
        OrderGateway orderGateway = new OrderGateway(orderDatasource);

        var getPublicOrdersUseCase = new GetPublicOrdersUseCase(orderGateway);

        List<CompleteOrderEntity> ordersList = getPublicOrdersUseCase.run();

        return OrderPresenter.createQueueOrderDTO(ordersList);
    }

    public CompleteOrderDTO getOrderById(UUID orderId) {
        OrderGateway orderGateway = new OrderGateway(orderDatasource);
        PaymentGateway paymentGateway = new PaymentGateway( paymentDatasource );

        var getOrderUseCase = new GetCompleteOrderByIdUseCase( orderGateway );
//      var getPaymentByOrderIdUseCase = new GetPaymentByOrderIdUseCase(paymentGateway);

        var orderEntity = getOrderUseCase.run(orderId);

//        var paymentEntity = getPaymentByOrderIdUseCase.run(orderId);
//
//        paymentEntity.ifPresent(orderEntity::setPayment);

        return OrderPresenter.createCompleteOrderDTO(orderEntity);
    }
}
