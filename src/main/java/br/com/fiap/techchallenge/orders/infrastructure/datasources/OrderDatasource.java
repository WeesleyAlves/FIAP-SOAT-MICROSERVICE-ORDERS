package br.com.fiap.techchallenge.orders.infrastructure.datasources;

import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CreateOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderNumberDTO;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderDatasource {
    CompleteOrderDTO findOrderByID(UUID orderId);
    List<CompleteOrderDTO> findOrderByStatusIn(List<OrderStatus> statusIn);
    List<CompleteOrderDTO> findAllOrders();
    CompleteOrderDTO saveOrder(CreateOrderDTO dto);
    CompleteOrderDTO updateOrder(CompleteOrderDTO dto);

    OrderNumberDTO saveOrderNumber(OrderNumberDTO dto);
    OrderNumberDTO findTopOrderNumber();
}