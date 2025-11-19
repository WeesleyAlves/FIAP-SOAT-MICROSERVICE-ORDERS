package br.com.fiap.techchallenge.orders.core.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderProductItemEntity {
    private final UUID id;

    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalValue;

    public OrderProductItemEntity(
        UUID id,
        String name,
        BigDecimal price
    ) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do produto não pode ser nulo.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser nulo ou vazio.");
        }

        if (price == null) {
            throw new IllegalArgumentException("O preço unitário do produto não pode ser nulo.");
        }

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço unitário do produto deve ser maior que zero.");
        }

        this.name = name;
        this.price = price;
        this.id = id;
    }

    public OrderProductItemEntity( UUID id ){
        if (id == null) {
            throw new IllegalArgumentException("O ID do produto não pode ser nulo.");
        }

        this.id = id;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade do produto deve ser maior que zero.");
        }

        this.quantity = quantity;
    }

    public void setTotalValue(BigDecimal totalValue){
        if (totalValue == null) {
            throw new IllegalArgumentException("O valor total do produto não pode ser nulo.");
        }

        if (totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor total do produto não pode ser zero.");
        }

        this.totalValue = totalValue;
    }
}
