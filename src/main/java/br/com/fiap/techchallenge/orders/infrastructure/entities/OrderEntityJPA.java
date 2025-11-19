package br.com.fiap.techchallenge.orders.infrastructure.entities;

import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import br.com.fiap.techchallenge.orders.utils.converters.OrderStatusConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "orders")
@Table(name= "orders")
@Data
@NoArgsConstructor
public class OrderEntityJPA {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private Integer orderNumber;

    @Column(name = "status_id")
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    private BigDecimal originalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Column(name = "customer_id")
    private UUID customerId;

    @JsonIgnore
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderProductsEntityJPA> products = new ArrayList<>();

    public OrderEntityJPA(UUID customerId, String notes, OrderStatus status, Integer orderNumber, BigDecimal originalPrice) {
        this.customerId = customerId;
        this.notes = notes;
        this.status = status;
        this.orderNumber = orderNumber;
        this.originalPrice = originalPrice;
    }

    public OrderEntityJPA(UUID id, Integer orderNumber, OrderStatus status, BigDecimal originalPrice, UUID customerId, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.status = status;
        this.originalPrice = originalPrice;
        this.customerId = customerId;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public void addProduct(OrderProductsEntityJPA product) {
        products.add(product);
        product.setOrder(this);
    }
}
