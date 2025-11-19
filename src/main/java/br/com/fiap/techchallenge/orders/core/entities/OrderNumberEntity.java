package br.com.fiap.techchallenge.orders.core.entities;

import lombok.Getter;

@Getter
public class OrderNumberEntity {
    private Long id;
    private Integer currentValue;

    public OrderNumberEntity(Long id, Integer currentValue) {
        if (id == null) {
            throw new IllegalArgumentException("O ID de pedido não pode ser nulo.");
        }

        if (currentValue == null) {
            throw new IllegalArgumentException("O valor atual do número de pedido não pode ser nulo.");
        }

        if (currentValue < 0) {
            throw new IllegalArgumentException("O valor atual do número de pedido não pode ser negativo.");
        }

        this.id = id;
        this.currentValue = currentValue;
    }

    public void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID de pedido não pode ser nulo.");
        }

        this.id = id;
    }

    public void setCurrentValue(Integer currentValue) {
        if (currentValue == null) {
            throw new IllegalArgumentException("O valor atual do número de pedido não pode ser nulo.");
        }

        if (currentValue < 0) {
            throw new IllegalArgumentException("O valor atual do número de pedido não pode ser negativo.");
        }

        this.currentValue = currentValue;
    }
}
