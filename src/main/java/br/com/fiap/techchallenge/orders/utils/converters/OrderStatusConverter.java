package br.com.fiap.techchallenge.orders.utils.converters;


import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderStatus status) {
        return (status == null) ? null : status.getId();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer statusId) {
        if (statusId == null) {
            return null;
        }

        return OrderStatus.getById(statusId);
    }
}