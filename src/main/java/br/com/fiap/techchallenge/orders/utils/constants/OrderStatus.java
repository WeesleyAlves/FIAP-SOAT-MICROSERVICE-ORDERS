package br.com.fiap.techchallenge.orders.utils.constants;


import br.com.fiap.techchallenge.orders.api.exceptions.OrderStatusNotFoundException;
import lombok.Getter;

import java.util.Arrays;

import static br.com.fiap.techchallenge.orders.utils.constants.OrderConstants.*;

@Getter
public enum OrderStatus {
    AWAITING_PAYMENT(1, "Aguardando Pagamento", 4),
    PENDING(2, "Recebido", 3),
    IN_PREPARATION(3, "Em Preparação", 2),
    READY(4, "Pronto", 1),
    COMPLETED(5, "Finalizado", 5),
    DISCARDED(6, "Descartado", 6),
    CANCELLED(7, "Cancelado", 7);

    private final int id;
    private final String description;
    private final int sortOrder;

    OrderStatus(int id, String description, int sortOrder) {
        this.id = id;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public static OrderStatus getById(int statusId) {
        return Arrays.stream(OrderStatus.values())
            .filter(status -> status.getId() == statusId)
            .findFirst()
            .orElseThrow(
                () -> new OrderStatusNotFoundException( String.format(ORDER_STATUS_NOT_FOUND, statusId) )
            );
    }

    public static OrderStatus getByDescription(String description) {
        return Arrays.stream(OrderStatus.values())
            .filter( status -> status.getDescription().equals(description) )
            .findFirst()
            .orElseThrow(
                () -> new OrderStatusNotFoundException( String.format(ORDER_DESCRIPTION_NOT_FOUND, description) )
            );
    }
}