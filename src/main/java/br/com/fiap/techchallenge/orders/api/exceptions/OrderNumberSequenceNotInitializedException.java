package br.com.fiap.techchallenge.orders.api.exceptions;

public class OrderNumberSequenceNotInitializedException extends RuntimeException {
    public OrderNumberSequenceNotInitializedException(String message) {
        super(message);
    }
}