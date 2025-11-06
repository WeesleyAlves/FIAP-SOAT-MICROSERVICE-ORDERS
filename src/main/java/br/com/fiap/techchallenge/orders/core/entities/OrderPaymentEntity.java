package br.com.fiap.techchallenge.orders.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderPaymentEntity {
    private UUID id;
    private String qrData;

    public OrderPaymentEntity(UUID uuid, String qrCodeData) {
        this.id = uuid;
        this.qrData = qrCodeData;
    }
}
