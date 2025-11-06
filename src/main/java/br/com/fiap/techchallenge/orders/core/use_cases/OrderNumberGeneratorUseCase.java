package br.com.fiap.techchallenge.orders.core.use_cases;


import br.com.fiap.techchallenge.orders.application.gateways.OrderNumberGateway;
import br.com.fiap.techchallenge.orders.core.entities.OrderNumberEntity;

public class OrderNumberGeneratorUseCase {
    private final OrderNumberGateway orderNumberGateway;

    public OrderNumberGeneratorUseCase(OrderNumberGateway orderNumberGateway) {
        this.orderNumberGateway = orderNumberGateway;
    }

    public Integer getNextOrderNumber() {
        OrderNumberEntity sequence = getSequenceOrThrow();
        int nextValue = sequence.getCurrentValue() + 1;
        sequence.setCurrentValue(nextValue);
        orderNumberGateway.save(sequence);
        return nextValue;
    }

    public void resetOrderNumberSequence() {
        OrderNumberEntity sequence = getSequenceOrThrow();
        sequence.setCurrentValue(0);
        orderNumberGateway.save(sequence);
    }

    private OrderNumberEntity getSequenceOrThrow() {
        return orderNumberGateway.findTopByOrderByIdAsc();
    }
}