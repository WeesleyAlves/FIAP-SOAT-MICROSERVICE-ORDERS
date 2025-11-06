package br.com.fiap.techchallenge.orders.core.use_cases;

import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.in.OrderProductInDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public class CreateOrderUseCase {
    private final OrderGateway orderGateway;
//    private final ProductGateway productGateway;
//    private final OrderProductsGateway orderProductsGateway;
//
    public CreateOrderUseCase(
            OrderGateway orderGateway
    ) {
        this.orderGateway = orderGateway;
    }

    public CompleteOrderEntity run(NewOrderDTO dto, Integer orderNumber) {
        if( dto.products() == null || dto.products().isEmpty() ) {
            throw new IllegalArgumentException("A lista de produtos não pode estar vazia.");
        }

        List<UUID> productsIds = dto.products().stream()
            .map(OrderProductInDTO::id)
            .toList();
//
//        List<ProductEntity> productEntities = productGateway.getAllByIds(productsIds);
//
//        List<OrderProductItemEntity> listItensOrder = new ArrayList<>();
//
//        BigDecimal totalPrice = BigDecimal.ZERO;
//
//        for (var orderProduct : dto.products()) {
//            ProductEntity product = productEntities.stream()
//                    .filter( p -> p.getId().equals( orderProduct.id() ) )
//                    .findFirst()
//                    .orElseThrow(
//                            () -> new ProductNotFoundException("Produto não encontrado para o ID: " + orderProduct.id())
//                    );
//
//            if(orderProduct.quantity() == null){
//                throw new IllegalArgumentException("A quantidade de produtos não pode estar vazia.");
//            }
//
//            var totalPriceProduct = product.getPrice()
//                    .multiply(
//                            new BigDecimal( orderProduct.quantity() )
//                    );
//
//            totalPrice = totalPrice.add(totalPriceProduct);
//
//            listItensOrder.add( new OrderProductItemEntity(
//                    product.getId(),
//                    product.getName(),
//                    orderProduct.quantity(),
//                    product.getPrice(),
//                    totalPriceProduct
//            ) );
//        }
//
        CompleteOrderEntity orderEntity = new CompleteOrderEntity(
                orderNumber,
                OrderStatus.AWAITING_PAYMENT.getDescription(),
                BigDecimal.valueOf(Math.random()),
                dto.customer_id()
        );

        orderEntity.setNotes(dto.notes());
//
//        orderEntity = orderGateway.save(orderEntity);
//
//        List<OrderProductsEntity> orderProductsToPersist = new ArrayList<>();
//
//        for ( var productItem: listItensOrder ){
//            orderProductsToPersist.add( new OrderProductsEntity(
//                    orderEntity.getId(),
//                    productItem.getId(),
//                    productItem.getQuantity()
//            ));
//        }
//
//        orderProductsGateway.saveAll(orderProductsToPersist);
//
//        orderEntity.setProducts(listItensOrder);
//
        return orderEntity;
    }
}