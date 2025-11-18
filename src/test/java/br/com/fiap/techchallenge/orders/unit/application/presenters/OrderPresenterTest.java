package br.com.fiap.techchallenge.orders.unit.application.presenters;

import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.QueueOrderDTO;
import br.com.fiap.techchallenge.orders.application.presenters.OrderPresenter;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.entities.OrderPaymentEntity;
import br.com.fiap.techchallenge.orders.core.entities.OrderProductItemEntity;

import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderPresenterTest {

    private UUID orderId;
    private Integer orderNumber;
    private UUID customerId;
    private LocalDateTime now;
    private OrderPaymentEntity paymentEntity;
    private CompleteOrderEntity completeOrderEntity;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        orderNumber = 1;
        customerId = UUID.randomUUID();
        now = LocalDateTime.now();

        paymentEntity = new OrderPaymentEntity(UUID.randomUUID(), "QR_DATA_TEST");

        OrderProductItemEntity productItem = new OrderProductItemEntity(
            UUID.randomUUID(), "Produto X", BigDecimal.TEN
        );

        productItem.setQuantity(2);
        productItem.setTotalValue(BigDecimal.valueOf(20));

        completeOrderEntity = new CompleteOrderEntity(
                orderId,
                orderNumber,
                OrderStatus.AWAITING_PAYMENT.getDescription(),
                BigDecimal.valueOf(20),
                now,
                now
        );

        completeOrderEntity.setPayment(paymentEntity);
        completeOrderEntity.setProducts( List.of(productItem) );
        completeOrderEntity.setNotes("Sem gelo");
        completeOrderEntity.setCustomerId(customerId);
    }

    // -------------------------------------------------------------------------
    // Teste de Instanciação (Construtor Publico)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar instanciar")
    void shouldThrowExceptionWhenInstantiating() {
        assertThrows(IllegalStateException.class, OrderPresenter::new);
    }

    // -------------------------------------------------------------------------
    // Testes para createCompleteOrderDTO
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve converter CompleteOrderEntity em CompleteOrderDTO corretamente com pagamento")
    void shouldConvertCompleteOrderEntityToDTOWithPayment() {
        CompleteOrderDTO dto = OrderPresenter.createCompleteOrderDTO(completeOrderEntity);

        assertEquals(orderId, dto.id());
        assertEquals(orderNumber, dto.order_number());
        assertEquals(OrderStatus.AWAITING_PAYMENT.getDescription(), dto.status());
        assertEquals(customerId, dto.customer_id());
        assertEquals(BigDecimal.valueOf(20), dto.price());
        assertEquals(1, dto.products().orElseThrow().size() );

        assertTrue(dto.payment_id().isPresent());
        assertEquals(paymentEntity.getId(), dto.payment_id().get());
        assertTrue(dto.payment_qr_data().isPresent());
        assertEquals(paymentEntity.getQrData(), dto.payment_qr_data().get());
    }

    @Test
    @DisplayName("Deve converter CompleteOrderEntity em CompleteOrderDTO corretamente sem pagamento")
    void shouldConvertCompleteOrderEntityToDTOWithoutPayment() {
        completeOrderEntity.setPayment(null);

        CompleteOrderDTO dto = OrderPresenter.createCompleteOrderDTO(completeOrderEntity);

        assertFalse(dto.payment_id().isPresent());
        assertFalse(dto.payment_qr_data().isPresent());
    }

    // -------------------------------------------------------------------------
    // Testes para createQueueOrderDTO
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve converter uma lista de CompleteOrderEntity para QueueOrderDTO")
    void shouldConvertListOfEntitiesToQueueOrderDTOList() {
        // ARRANGE
        CompleteOrderEntity secondOrder = new CompleteOrderEntity(
                UUID.randomUUID(),
                2,
                OrderStatus.IN_PREPARATION.getDescription(),
                BigDecimal.valueOf(50),
                now.plusMinutes(5),
                now.plusMinutes(5)
        );

        List<CompleteOrderEntity> entities = List.of(completeOrderEntity, secondOrder);

        List<QueueOrderDTO> dtos = OrderPresenter.createQueueOrderDTO(entities);

        assertEquals(2, dtos.size());

        QueueOrderDTO firstDto = dtos.getFirst();
        assertEquals(completeOrderEntity.getId(), firstDto.id());
        assertEquals(completeOrderEntity.getOrderNumber(), firstDto.order_number());
        assertEquals(1, firstDto.products().size());

        // Verifica o segundo item
        QueueOrderDTO secondDto = dtos.get(1);
        assertEquals(secondOrder.getOrderNumber(), secondDto.order_number());
        assertEquals(OrderStatus.IN_PREPARATION.getDescription(), secondDto.status());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se a entrada for lista vazia")
    void shouldReturnEmptyListWhenInputIsEmpty() {
        List<QueueOrderDTO> dtos = OrderPresenter.createQueueOrderDTO(Collections.emptyList());

        assertTrue(dtos.isEmpty());
    }
}