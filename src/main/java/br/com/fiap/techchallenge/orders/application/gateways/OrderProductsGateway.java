package br.com.fiap.techchallenge.orders.application.gateways;


import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductOutDTO;
import br.com.fiap.techchallenge.orders.core.entities.OrderProductsEntity;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;

import java.util.List;
import java.util.UUID;

public class OrderProductsGateway {
    private final OrderDatasource orderDatasource;

    public OrderProductsGateway(OrderDatasource orderDatasource) {
        this.orderDatasource = orderDatasource;
    }

    public List<OrderProductsEntity> findAllByOrderId(UUID orderId){
        var result = orderDatasource.findOrderProductsByOrderId(orderId);

        return result.stream()
            .map( item ->
                new OrderProductsEntity(
                    item.orderId(),
                    item.productId(),
                    item.quantity()
                )
            ).toList();
    }

    public List<OrderProductsEntity> saveAll(List<OrderProductsEntity> orderProducts){
        List<OrderProductOutDTO> dtoToPersist = orderProducts.stream()
            .map( item ->
                new OrderProductOutDTO(
                    item.getOrderId(),
                    item.getProductId(),
                    item.getQuantity()
                )
            ).toList();

        var result = orderDatasource.saveAllOrderProducts(dtoToPersist);

        return result.stream().map( dto ->
            new OrderProductsEntity(
                dto.orderId(),
                dto.productId(),
                dto.quantity()
            )
        ).toList();
    }

}
