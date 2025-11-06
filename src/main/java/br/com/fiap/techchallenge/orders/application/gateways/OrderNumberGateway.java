package br.com.fiap.techchallenge.orders.application.gateways;


import br.com.fiap.techchallenge.orders.api.exceptions.OrderNumberNotFound;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderNumberDTO;
import br.com.fiap.techchallenge.orders.core.entities.OrderNumberEntity;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;

public class OrderNumberGateway {
    private final OrderDatasource orderDatasource;

    public OrderNumberGateway(OrderDatasource orderDatasource) {
        this.orderDatasource = orderDatasource;
    }

    public OrderNumberEntity save(OrderNumberEntity orderNumberEntity) {
        var result = orderDatasource.saveOrderNumber( new OrderNumberDTO(
            orderNumberEntity.getId(),
            orderNumberEntity.getCurrentValue()
        ));

        return new OrderNumberEntity(
            result.id(),
            result.currentValue()
        );
    }

    public OrderNumberEntity findTopByOrderByIdAsc(){
        var result = orderDatasource.findTopOrderNumber();

        if( result == null ){
            throw new OrderNumberNotFound("Numeração de ordem não encontrada.");
        }

        return new OrderNumberEntity(
            result.id(),
            result.currentValue()
        );
    }
}
