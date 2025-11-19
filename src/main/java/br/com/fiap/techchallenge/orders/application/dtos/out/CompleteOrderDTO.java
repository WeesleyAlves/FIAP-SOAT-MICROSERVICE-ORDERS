package br.com.fiap.techchallenge.orders.application.dtos.out;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record CompleteOrderDTO(
    UUID id,
    Integer order_number,
    String status,
    UUID customer_id,
    String notes,
    BigDecimal price,
    LocalDateTime created_at,
    LocalDateTime updated_at,
    Optional<List<OrderProductItemOutDTO>> products,
    Optional<UUID> payment_id,
    Optional<String> payment_qr_data
) {

    public CompleteOrderDTO withProducts(List<OrderProductItemOutDTO> newProducts) {
        return new CompleteOrderDTO(
            this.id,
            this.order_number,
            this.status,
            this.customer_id,
            this.notes,
            this.price,
            this.created_at,
            this.updated_at,
            Optional.of(newProducts),
            this.payment_id,
            this.payment_qr_data
        );
    }

    public CompleteOrderDTO withStatus(String newStatus) {
        return new CompleteOrderDTO(
                this.id,
                this.order_number,
                newStatus,
                this.customer_id,
                this.notes,
                this.price,
                this.created_at,
                this.updated_at,
                this.products,
                this.payment_id,
                this.payment_qr_data
        );
    }
}
