package br.com.fiap.techchallenge.orders.infrastructure.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_number_sequence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderNumberEntityJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "current_value", nullable = false)
    @JsonIgnore
    private Integer currentValue;
}
