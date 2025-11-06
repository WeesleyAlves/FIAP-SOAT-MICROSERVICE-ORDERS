package br.com.fiap.techchallenge.orders.infrastructure.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_products")
@Data
@NoArgsConstructor
public class OrderProductsEntityJPA {

    @EmbeddedId
    private OrderProductIdJPA id;

    private Integer quantity;

    public OrderProductsEntityJPA(
        OrderProductIdJPA id,
        Integer quantity
    ) {
        this.id = id;
        this.quantity = quantity;
    }
}