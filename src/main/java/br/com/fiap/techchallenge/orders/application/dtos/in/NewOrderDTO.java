package br.com.fiap.techchallenge.orders.application.dtos.in;

import java.util.List;
import java.util.UUID;

public record NewOrderDTO(
    UUID customer_id,
    List<OrderProductInDTO> products,
    String notes
){}
