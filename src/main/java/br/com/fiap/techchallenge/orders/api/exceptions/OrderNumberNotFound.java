package br.com.fiap.techchallenge.orders.api.exceptions;

public class OrderNumberNotFound extends RuntimeException {
    public OrderNumberNotFound(String message) {
        super(message);
    }
}
