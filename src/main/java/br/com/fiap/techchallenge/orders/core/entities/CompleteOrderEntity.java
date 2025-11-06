package br.com.fiap.techchallenge.orders.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class CompleteOrderEntity {

    private UUID id;
    private final Integer orderNumber;
    private String status;
    private final BigDecimal originalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderProductItemEntity> products;

    @Setter
    private  UUID customerId;

    @Setter
    private String notes;

    @Setter
    private OrderPaymentEntity payment;

    public CompleteOrderEntity(
        UUID id,
        Integer orderNumber,
        String status,
        BigDecimal originalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        if (id == null) {
            throw new IllegalArgumentException("O ID do pedido não pode ser nulo.");
        }

        if (orderNumber == null) {
            throw new IllegalArgumentException("O número do pedido não pode ser nulo.");
        }

        if (orderNumber <= 0) {
            throw new IllegalArgumentException("O número do pedido deve ser um valor positivo.");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status não pode ser nulo ou vazio.");
        }

        if (originalPrice == null) {
            throw new IllegalArgumentException("O preço original não pode ser nulo.");
        }

        if (originalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço original não pode ser negativo.");
        }

        if (createdAt == null) {
            throw new IllegalArgumentException("A data de criação não pode ser nula.");
        }

        this.id = id;
        this.orderNumber = orderNumber;
        this.status = status;
        this.originalPrice = originalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public CompleteOrderEntity(
        Integer orderNumber,
        String status,
        BigDecimal originalPrice,
        UUID customerId
    ) {
        if (orderNumber == null || orderNumber <= 0) {
            throw new IllegalArgumentException("O número do pedido deve ser um valor positivo e não pode ser nulo.");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status do pedido não pode ser nulo ou vazio.");
        }

        if (originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço original não pode ser nulo ou negativo.");
        }

        this.orderNumber = orderNumber;
        this.status = status;
        this.originalPrice = originalPrice;
        this.customerId = customerId;
    }

    public void setProducts(List<OrderProductItemEntity> products) {
        if (products == null) {
            throw new IllegalArgumentException("A lista de produtos não pode ser nula.");
        }

        if( products.isEmpty() ) {
            throw new IllegalArgumentException("A lista de produtos não pode estar vazia.");
        }

        this.products = products;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("O status do pedido não pode ser nulo ou vazio.");
        }

        this.status = status;
    }
}
