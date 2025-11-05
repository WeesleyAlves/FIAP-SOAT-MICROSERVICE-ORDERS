package br.com.fiap.techchallenge.orders.core.entities;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderProductItemEntity {
    private final String name;
    private final int quantity;
    private final BigDecimal price;
    private final BigDecimal totalValue;
    private final UUID id;

    public OrderProductItemEntity(
        UUID id,
        String name,
        int quantity,
        BigDecimal price,
        BigDecimal totalValue
    ) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do produto não pode ser nulo.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser nulo ou vazio.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("A quantidade do produto deve ser maior que zero.");
        }

        if (price == null) {
            throw new IllegalArgumentException("O preço unitário do produto não pode ser nulo.");
        }

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço unitário do produto deve ser maior que zero.");
        }

        if (totalValue == null) {
            throw new IllegalArgumentException("O valor total do produto não pode ser nulo.");
        }

        if (totalValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor total do produto não pode ser zero.");
        }

        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalValue = totalValue;
        this.id = id;
    }
}
