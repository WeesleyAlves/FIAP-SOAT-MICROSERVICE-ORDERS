package br.com.fiap.techchallenge.orders.api.exceptions;

public class OrderStatusNotFoundException extends RuntimeException {

    public OrderStatusNotFoundException(String message) {
        super(message);
    }
}