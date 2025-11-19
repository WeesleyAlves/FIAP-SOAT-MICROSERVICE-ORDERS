package br.com.fiap.techchallenge.orders.application.gateways;

import br.com.fiap.techchallenge.orders.core.entities.OrderProductItemEntity;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.ProductsDatasource;

import java.util.List;
import java.util.UUID;

public class ProductGateway {
    private final ProductsDatasource productsDatasource;

    public ProductGateway(ProductsDatasource productsDatasource) {
        this.productsDatasource = productsDatasource;
    }

    public List<OrderProductItemEntity> getAllByIds(List<UUID> ids) {
        var result = productsDatasource.getAllByIds(ids);

        return result.stream().map( p ->
            new OrderProductItemEntity(
               p.id(),
               p.name(),
               p.price()
            )
        ).toList();
    }
}
