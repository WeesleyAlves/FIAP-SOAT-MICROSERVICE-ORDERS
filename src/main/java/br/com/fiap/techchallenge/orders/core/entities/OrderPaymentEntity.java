package br.com.fiap.techchallenge.orders.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderPaymentEntity {
    private UUID paymentId;
    private String qrCode;
}
