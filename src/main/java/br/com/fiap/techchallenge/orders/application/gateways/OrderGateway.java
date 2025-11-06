package br.com.fiap.techchallenge.orders.application.gateways;

import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CreateOrderDTO;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;

import java.util.List;
import java.util.UUID;

public class OrderGateway {
    private final OrderDatasource datasource;

    public OrderGateway(OrderDatasource datasource) {
        this.datasource = datasource;
    }

    public CompleteOrderEntity findById(UUID orderId){
        return createOrderEntity(
            datasource.findOrderByID(orderId)
        );
    }

    public List<CompleteOrderEntity> getAllPublic(){
        var result = datasource.findOrderByStatusIn(List.of(
            OrderStatus.PENDING,
            OrderStatus.IN_PREPARATION,
            OrderStatus.READY
        ));

        return  mountCompletOrderList(result);
    }

    public List<CompleteOrderEntity> getAll(){
        var result = datasource.findAllOrders();

        return  mountCompletOrderList(result);
    }

    public CompleteOrderEntity save(CompleteOrderEntity order){
        return createOrderEntity(
            datasource.saveOrder(
                new CreateOrderDTO(
                    order.getCustomerId(),
                    order.getNotes(),
                    order.getOrderNumber(),
                    order.getOriginalPrice(),
                    order.getStatus()
                )
            )
        );
    }

    public CompleteOrderEntity update(CompleteOrderEntity order){
        return createOrderEntity(
            datasource.updateOrder(
                new CompleteOrderDTO(
                    order.getId(),
                    order.getOrderNumber(),
                    order.getStatus(),
                    order.getCustomerId(),
                    order.getNotes(),
                    order.getOriginalPrice(),
                    order.getCreatedAt(),
                    order.getUpdatedAt(),
                    null,
                    null,
                    null
                )
            )
        );
    }

    private CompleteOrderEntity createOrderEntity(CompleteOrderDTO dto){
        var orderEntity = new CompleteOrderEntity(
            dto.id(),
            dto.order_number(),
            dto.status(),
            dto.price(),
            dto.created_at(),
            dto.updated_at()
        );

        orderEntity.setCustomerId(dto.customer_id());
        orderEntity.setNotes(dto.notes());

        return orderEntity;
    }

    private List<CompleteOrderEntity> mountCompletOrderList(List<CompleteOrderDTO> dtoList){
        return dtoList.stream()
            .map( this::createOrderEntity )
            .toList();
    }

}
