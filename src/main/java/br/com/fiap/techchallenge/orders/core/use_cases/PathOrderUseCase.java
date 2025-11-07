package br.com.fiap.techchallenge.orders.core.use_cases;


import br.com.fiap.techchallenge.orders.application.dtos.in.PathOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;

public class PathOrderUseCase {
    private final OrderGateway orderGateway;

    public PathOrderUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public CompleteOrderEntity run(PathOrderDTO dto){
        if( dto.id() == null ){
            throw new IllegalArgumentException("O ID do pedido não pode ser nulo.");
        }

        if( dto.status_id() == null ){
            throw new IllegalArgumentException("O novo status do pedido não pode ser nulo.");
        }

        if( dto.status_id() <= 0 ){
            throw new IllegalArgumentException("O novo status deve ser maior que zero.");
        }

        CompleteOrderEntity order = orderGateway.findById(dto.id());

        var completeStatus = OrderStatus.getById(dto.status_id());

        order.setStatus( completeStatus.getDescription() );

        return orderGateway.update(order);
    }
}
