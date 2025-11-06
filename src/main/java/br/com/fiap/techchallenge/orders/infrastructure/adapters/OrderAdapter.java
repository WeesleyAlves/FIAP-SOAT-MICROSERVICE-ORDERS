package br.com.fiap.techchallenge.orders.infrastructure.adapters;


import br.com.fiap.techchallenge.orders.api.exceptions.OrderNotFoundException;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CreateOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductOutDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderEntityJPA;
import br.com.fiap.techchallenge.orders.infrastructure.repositories.OrdersRepository;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderAdapter implements OrderDatasource {
    private final OrdersRepository ordersRepository;
//    private final OrderProductsRepository orderProductsRepository;
//    private final OrderNumberSequenceRepository sequenceRepository;

    @Autowired
    public OrderAdapter(
        OrdersRepository ordersRepository
//        OrderProductsRepository orderProductsRepository,
//        OrderNumberSequenceRepository sequenceRepository
    ) {
        this.ordersRepository = ordersRepository;
//        this.orderProductsRepository = orderProductsRepository;
//        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public CompleteOrderDTO findOrderByID(UUID orderId) {
        var result = ordersRepository.findById(orderId)
            .orElseThrow(
                () -> new OrderNotFoundException("Pedido não encontrado para o id: " + orderId)
            );

        return new CompleteOrderDTO(
            result.getId(),
            result.getOrderNumber(),
            result.getStatus().getDescription(),
            result.getCustomerId(),
            result.getNotes(),
            result.getOriginalPrice(),
            result.getCreatedAt(),
            result.getUpdatedAt(),
            null,
            null,
            null
        );
    }

    @Override
    public List<CompleteOrderDTO> findOrderByStatusIn(List<OrderStatus> statusIn) {
        var result = ordersRepository.findByStatusIn(statusIn);

        return mountCompleteOrdersList(result);
    }

    @Override
    public List<CompleteOrderDTO> findAllOrders() {
        var result = ordersRepository.findAll();

        return mountCompleteOrdersList(result);
    }

    @Override
    public CompleteOrderDTO saveOrder(CreateOrderDTO dto) {
        var result = ordersRepository.save(
            new OrderEntityJPA(
                dto.customerId(),
                dto.notes(),
                OrderStatus.getByDescription(dto.status()),
                dto.orderNumber(),
                dto.price()
            )
        );

        return new CompleteOrderDTO(
            result.getId(),
            result.getOrderNumber(),
            result.getStatus().getDescription(),
            result.getCustomerId(),
            result.getNotes(),
            result.getOriginalPrice(),
            result.getCreatedAt(),
            result.getUpdatedAt(),
            null,
            null,
            null
        );
    }

    @Override
    public CompleteOrderDTO updateOrder(CompleteOrderDTO dto) {
       var result =  ordersRepository.save( new OrderEntityJPA(
           dto.id(),
           dto.order_number(),
           OrderStatus.getByDescription(dto.status()),
           dto.price(),
           dto.customer_id(),
           dto.notes(),
           dto.created_at()
       ) );

        return new CompleteOrderDTO(
            result.getId(),
            result.getOrderNumber(),
            result.getStatus().getDescription(),
            result.getCustomerId(),
            result.getNotes(),
            result.getOriginalPrice(),
            result.getCreatedAt(),
            result.getUpdatedAt(),
            null,
            null,
            null
        );
    }

//    @Override
//    public List<OrderProductOutDTO> findOrderProductsByOrderId(UUID orderId) {
//        var result = orderProductsRepository.findAllById_OrderId(orderId);
//
//        return result.stream()
//            .map( orderProduct ->
//                new OrderProductOutDTO(
//                    orderProduct.getId().getOrderId(),
//                    orderProduct.getId().getProductId(),
//                    orderProduct.getQuantity()
//                )
//            ).toList();
//    }

//    @Override
//    public List<OrderProductOutDTO> saveAllOrderProducts(List<OrderProductOutDTO> dto) {
//        List<OrderProductsEntityJPA> toPersiste = dto.stream()
//            .map( item ->
//                new OrderProductsEntityJPA(
//                    new OrderProductIdJPA(
//                        item.orderId(),
//                        item.productId()
//                    ),
//                    item.quantity()
//                )
//            ).toList();
//
//        var result = orderProductsRepository.saveAll(toPersiste);
//
//        return result.stream()
//            .map(item ->
//                new OrderProductOutDTO(
//                    item.getId().getOrderId(),
//                    item.getId().getProductId(),
//                    item.getQuantity()
//                )
//            ).toList();
//    }

//    @Override
//    @Transactional
//    public OrderNumberDTO saveOrderNumber(OrderNumberDTO dto) {
//        var result = sequenceRepository.save(
//            new OrderNumberEntityJPA(
//                dto.id(),
//                dto.currentValue()
//            )
//        );
//
//        return new OrderNumberDTO(
//            result.getId(),
//            result.getCurrentValue()
//        );
//    }

//    @Override
//    @Transactional
//    public OrderNumberDTO findTopOrderNumber() {
//        var result = sequenceRepository.findTopByOrderByIdAsc()
//            .orElseThrow(() ->
//                new OrderNumberSequenceNotInitializedException(
//                    ORDER_SEQUENCE_NOT_INITIALIZED
//                )
//            );
//
//        return new OrderNumberDTO(
//            result.getId(),
//            result.getCurrentValue()
//        );
//    }

//    @Override
//    @Transactional
//    public Long countOrderNumber() {
//        return sequenceRepository.count();
//    }
//
//    @PostConstruct
//    @Transactional
//    public void initSequence() {
//        if (sequenceRepository.count() == 0) {
//            OrderNumberEntityJPA initial = new OrderNumberEntityJPA();
//            initial.setCurrentValue(0);
//            sequenceRepository.save(initial);
//        }
//    }

    private List<CompleteOrderDTO> mountCompleteOrdersList(List<OrderEntityJPA> orders) {
        return orders.stream()
            .map(order ->
                new CompleteOrderDTO(
                    order.getId(),
                    order.getOrderNumber(),
                    order.getStatus().getDescription(),
                    order.getCustomerId(),
                    order.getNotes(),
                    order.getOriginalPrice(),
                    order.getCreatedAt(),
                    order.getUpdatedAt(),
                    null,
                    null,
                    null
                )
            ).toList();
    }

}
