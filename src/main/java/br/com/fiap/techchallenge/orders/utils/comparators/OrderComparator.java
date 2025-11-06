package br.com.fiap.techchallenge.orders.utils.comparators;


import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;

import java.util.Comparator;

public class OrderComparator {
    private OrderComparator() {}

    public static Comparator<CompleteOrderEntity> run() {
        return (order1, order2) -> {
            OrderStatus statusEnum1 = OrderStatus.getByDescription(order1.getStatus());
            OrderStatus statusEnum2 = OrderStatus.getByDescription(order2.getStatus());

            int statusComparison = Integer.compare(statusEnum1.getSortOrder(), statusEnum2.getSortOrder());

            if (statusComparison != 0) {
                return statusComparison;
            }

            return order1.getCreatedAt().compareTo(order2.getCreatedAt());
        };
    }
}