package br.com.fiap.techchallenge.orders.infrastructure.adapters;

import br.com.fiap.techchallenge.orders.api.exceptions.OrderNotFoundException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderNumberSequenceNotInitializedException;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CreateOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderNumberDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductItemOutDTO;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderEntityJPA;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderNumberEntityJPA;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderProductIdJPA;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderProductsEntityJPA;
import br.com.fiap.techchallenge.orders.infrastructure.repositories.OrderNumberSequenceRepository;
import br.com.fiap.techchallenge.orders.infrastructure.repositories.OrdersRepository;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.techchallenge.orders.utils.constants.OrderConstants.ORDER_NOT_FOUND;
import static br.com.fiap.techchallenge.orders.utils.constants.OrderConstants.ORDER_SEQUENCE_NOT_INITIALIZED;

@Service
public class OrderAdapter implements OrderDatasource {
    private final OrdersRepository ordersRepository;
    private final OrderNumberSequenceRepository sequenceRepository;

    @Autowired
    public OrderAdapter(
        OrdersRepository ordersRepository,
        OrderNumberSequenceRepository sequenceRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.sequenceRepository = sequenceRepository;
    }

    @Override
    public CompleteOrderDTO findOrderByID(UUID orderId) {
        var result = ordersRepository.findById(orderId)
            .orElseThrow(
                () -> new OrderNotFoundException( String.format(ORDER_NOT_FOUND, orderId) )
            );

        return createCompleteOrderDTOFromJPA(result);
    }

    @Override
    public List<CompleteOrderDTO> findOrderByStatusIn(List<OrderStatus> statusIn) {
        var result = ordersRepository.findByStatusInWithProducts(statusIn);

        return mountCompleteOrdersList(result);
    }

    @Override
    public List<CompleteOrderDTO> findAllOrders() {
        var result = ordersRepository.findAll();

        return mountCompleteOrdersList(result);
    }

    @Override
    public CompleteOrderDTO saveOrder(CreateOrderDTO dto) {
        OrderEntityJPA orderJPA = new OrderEntityJPA(
            dto.customerId(),
            dto.notes(),
            OrderStatus.getByDescription(dto.status()),
            dto.orderNumber(),
            dto.price()
        );

        List<OrderProductsEntityJPA> productEntities = dto.products().stream()
            .map(productDTO -> {
                OrderProductIdJPA productPk = new OrderProductIdJPA(null, productDTO.id() );

                OrderProductsEntityJPA productJPA = new OrderProductsEntityJPA(
                    productPk,
                    productDTO.name(),
                    productDTO.quantity(),
                    productDTO.price(),
                    productDTO.totalValue()
                );

                productJPA.setOrder(orderJPA);

                return productJPA;
            })
            .toList();

        orderJPA.setProducts(productEntities);

        OrderEntityJPA result = ordersRepository.save(orderJPA);

        return createCompleteOrderDTOFromJPA(result);
    }

    @Override
    public CompleteOrderDTO updateOrder(CompleteOrderDTO dto) {
        OrderEntityJPA order = ordersRepository.findById(dto.id())
            .orElseThrow(
                () -> new OrderNotFoundException( String.format(ORDER_NOT_FOUND, dto.id()) )
            );

        order.setStatus( OrderStatus.getByDescription(dto.status()) );

        var result = ordersRepository.save(order);

        return createCompleteOrderDTOFromJPA(result);
    }

    @Override
    @Transactional
    public OrderNumberDTO saveOrderNumber(OrderNumberDTO dto) {
        var result = sequenceRepository.save(
            new OrderNumberEntityJPA(
                dto.id(),
                dto.currentValue()
            )
        );

        return new OrderNumberDTO(
            result.getId(),
            result.getCurrentValue()
        );
    }

    @Override
    @Transactional
    public OrderNumberDTO findTopOrderNumber() {
        var result = sequenceRepository.findTopByOrderByIdAsc()
            .orElseThrow(() ->
                new OrderNumberSequenceNotInitializedException(
                    ORDER_SEQUENCE_NOT_INITIALIZED
                )
            );

        return new OrderNumberDTO(
            result.getId(),
            result.getCurrentValue()
        );
    }

    @PostConstruct
    @Transactional
    public void initSequence() {
        if (sequenceRepository.count() == 0) {
            OrderNumberEntityJPA initial = new OrderNumberEntityJPA();
            initial.setCurrentValue(0);
            sequenceRepository.save(initial);
        }
    }

    private List<CompleteOrderDTO> mountCompleteOrdersList(List<OrderEntityJPA> orders) {
        return orders.stream()
            .map( this::createCompleteOrderDTOFromJPA )
            .toList();
    }

    private CompleteOrderDTO createCompleteOrderDTOFromJPA(OrderEntityJPA jpaEntity) {
        List<OrderProductItemOutDTO> productItems = jpaEntity.getProducts().stream()
            .map(productJPA ->
                new OrderProductItemOutDTO(
                    productJPA.getId().getProductId(),
                    productJPA.getName(),
                    productJPA.getPrice(),
                    productJPA.getQuantity(),
                    productJPA.getTotalValue()
                )
            ).toList();

        return new CompleteOrderDTO(
            jpaEntity.getId(),
            jpaEntity.getOrderNumber(),
            jpaEntity.getStatus().getDescription(),
            jpaEntity.getCustomerId(),
            jpaEntity.getNotes(),
            jpaEntity.getOriginalPrice(),
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt(),
            Optional.of(productItems),
            null,
            null
        );
    }

}
