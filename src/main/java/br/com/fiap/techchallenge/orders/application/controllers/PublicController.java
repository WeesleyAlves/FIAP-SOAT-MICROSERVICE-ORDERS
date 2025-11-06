package br.com.fiap.techchallenge.orders.application.controllers;

import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public class PublicController {
    public CompleteOrderDTO createOrder(NewOrderDTO dto) {
        var persistedOrder = new CompleteOrderEntity(
            UUID.randomUUID(),
            10,
            "Recebido",
            BigDecimal.valueOf(Math.random()),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        return OrderPresenter.createCompleteOrderDTO(persistedOrder);
    }
}
