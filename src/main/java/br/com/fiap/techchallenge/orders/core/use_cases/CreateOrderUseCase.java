package br.com.fiap.techchallenge.orders.core.use_cases;

import br.com.fiap.techchallenge.orders.api.exceptions.ProductNotFoundException;
import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.in.OrderProductInDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.application.gateways.ProductGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.entities.OrderProductItemEntity;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CreateOrderUseCase {
    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public CreateOrderUseCase(
            OrderGateway orderGateway,
            ProductGateway productGateway
    ) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    public CompleteOrderEntity run(NewOrderDTO dto, Integer orderNumber) {
        if( dto.products() == null || dto.products().isEmpty() ) {
            throw new IllegalArgumentException("A lista de produtos não pode estar vazia.");
        }

        List<UUID> productsIds = dto.products().stream()
            .map(OrderProductInDTO::id)
            .toList();


        List<OrderProductItemEntity> productEntitiesResult = productGateway.getAllByIds(productsIds);

        List<OrderProductItemEntity> productsEntitiesCompleteList = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (var orderProduct : dto.products() ) {
            var productEntity = productEntitiesResult.stream()
                .filter( p -> p.getId().equals( orderProduct.id() ) )
                .findFirst()
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado para o ID: " + orderProduct.id())
                );

            if(orderProduct.quantity() == null){
                throw new IllegalArgumentException("A quantidade de produtos não pode estar vazia.");
            }

            var totalPriceProduct = productEntity
                .getPrice()
                .multiply(
                    new BigDecimal( orderProduct.quantity() )
                );

            totalPrice = totalPrice.add(totalPriceProduct);

            productEntity.setQuantity( orderProduct.quantity() );
            productEntity.setTotalValue( totalPriceProduct );

            productsEntitiesCompleteList.add( productEntity );
        }

        CompleteOrderEntity orderEntity = new CompleteOrderEntity(
            orderNumber,
            OrderStatus.AWAITING_PAYMENT.getDescription(),
            totalPrice,
            dto.customer_id()
        );

        orderEntity.setNotes(dto.notes());

        orderEntity.setProducts(productsEntitiesCompleteList);

        orderEntity = orderGateway.save(orderEntity);


        return orderEntity;
    }
}