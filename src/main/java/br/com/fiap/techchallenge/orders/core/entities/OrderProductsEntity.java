package br.com.fiap.techchallenge.orders.core.entities;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderProductsEntity {
    private final UUID orderId;
    private final UUID productId;
    private final Integer quantity;

    public OrderProductsEntity(UUID orderId, UUID productId, Integer quantity) {
        if (orderId == null) {
            throw new IllegalArgumentException("O ID do pedido não pode ser nulo.");
        }
        if (productId == null) {
            throw new IllegalArgumentException("O ID do produto não pode ser nulo.");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("A quantidade não pode ser nula.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
