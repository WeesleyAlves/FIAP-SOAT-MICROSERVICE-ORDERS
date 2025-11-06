package br.com.fiap.techchallenge.orders.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@ToString
@Data
public class OrderProductIdJPA implements Serializable {

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_id")
    private UUID productId;

    public OrderProductIdJPA(UUID orderId, UUID productId) {
        this.orderId = orderId;
        this.productId = productId;
    }
}