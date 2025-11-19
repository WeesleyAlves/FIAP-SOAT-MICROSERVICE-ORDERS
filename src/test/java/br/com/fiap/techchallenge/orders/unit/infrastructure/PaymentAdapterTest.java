package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.application.dtos.in.PaymentInDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.PaymentAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentAdapterTest {

    private PaymentAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PaymentAdapter();
    }

    // -------------------------------------------------------------------------
    // Testes para createPayment()
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve criar um DTO de pagamento com ID e QR code fixos (mock)")
    void shouldCreatePayment() {
        PaymentInDTO result = adapter.createPayment();

        assertNotNull(result);
        assertNotNull(result.uuid());
        assertEquals("qr_code_data", result.qrData());
    }

    // -------------------------------------------------------------------------
    // Testes para getPaymentByOrderId(UUID)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar um DTO de pagamento com QR code contendo o Order ID fornecido")
    void shouldReturnPaymentDtoByOrderId() {
        UUID testOrderId = UUID.randomUUID();

        PaymentInDTO result = adapter.getPaymentByOrderId(testOrderId);

        assertNotNull(result);
        assertNotNull(result.uuid());

        assertTrue(result.qrData().contains(testOrderId.toString()));
        assertTrue(result.qrData().startsWith("qr_code_"));
    }
}