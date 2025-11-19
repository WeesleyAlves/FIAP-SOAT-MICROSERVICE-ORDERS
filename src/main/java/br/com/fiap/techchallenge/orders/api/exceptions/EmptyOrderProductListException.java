package br.com.fiap.techchallenge.orders.api.exceptions;

public class EmptyOrderProductListException extends RuntimeException {
    public EmptyOrderProductListException(String message) {
        super(message);
    }
}