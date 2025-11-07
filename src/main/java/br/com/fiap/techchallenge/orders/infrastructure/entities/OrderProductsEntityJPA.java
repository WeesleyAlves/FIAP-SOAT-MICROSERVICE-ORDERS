package br.com.fiap.techchallenge.orders.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_products")
@Data
@NoArgsConstructor
public class OrderProductsEntityJPA {

    @EmbeddedId
    private OrderProductIdJPA id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OrderEntityJPA order;

    private Integer quantity;

    private String name;

    private BigDecimal price;

    private BigDecimal totalValue;

    public OrderProductsEntityJPA(OrderProductIdJPA productPk, String name, Integer quantity, BigDecimal price,  BigDecimal totalValue) {
        this.id = productPk;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.totalValue = totalValue;
    }
}