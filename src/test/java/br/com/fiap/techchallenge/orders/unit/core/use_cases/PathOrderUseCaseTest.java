package br.com.fiap.techchallenge.orders.unit.core.use_cases;

import br.com.fiap.techchallenge.orders.application.dtos.in.PathOrderDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.use_cases.PathOrderUseCase;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para PathOrderUseCase")
class PathOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private PathOrderUseCase pathOrderUseCase;

    private UUID validOrderId;
    private CompleteOrderEntity mockOrderEntity;
    private PathOrderDTO validDto;

    @BeforeEach
    void setUp() {
        validOrderId = UUID.randomUUID();

        mockOrderEntity = new CompleteOrderEntity(
                validOrderId,
                10,
                OrderStatus.PENDING.getDescription(),
                BigDecimal.TEN,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // DTO de entrada para mudar o status para PENDING (ID=2)
        validDto = new PathOrderDTO(validOrderId, 2);
    }

    @Test
    @DisplayName("Deve atualizar o status do pedido e salvar com sucesso")
    void shouldUpdateStatusAndSaveOrderSuccessfully() {
        String expectedStatus = OrderStatus.PENDING.getDescription();


        when(orderGateway.findById(validOrderId)).thenReturn(mockOrderEntity);

        when(orderGateway.update(any(CompleteOrderEntity.class))).thenReturn(mockOrderEntity);


        ArgumentCaptor<CompleteOrderEntity> orderCaptor = ArgumentCaptor.forClass(CompleteOrderEntity.class);


        CompleteOrderEntity result = pathOrderUseCase.run(validDto);


        verify(orderGateway, times(1)).findById(validOrderId);
        verify(orderGateway, times(1)).update(orderCaptor.capture());

        CompleteOrderEntity updatedOrder = orderCaptor.getValue();
        assertEquals(expectedStatus, updatedOrder.getStatus());

        assertEquals(validOrderId, result.getId());
        assertEquals(expectedStatus, result.getStatus());
    }

    // -------------------------------------------------------------------------
    // Testes de Validação de Entrada (DTO)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve lançar exceção se o ID do pedido for nulo")
    void shouldThrowExceptionIfOrderIdIsNull() {
        PathOrderDTO dto = new PathOrderDTO(null, 1);

        assertThrows(IllegalArgumentException.class, () -> pathOrderUseCase.run(dto));

        verify(orderGateway, times(0)).findById(any());
        verify(orderGateway, times(0)).update(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se o novo status_id for nulo")
    void shouldThrowExceptionIfStatusIdIsNull() {
        PathOrderDTO dto = new PathOrderDTO(validOrderId, null);

        assertThrows(IllegalArgumentException.class, () -> pathOrderUseCase.run(dto));

        verify(orderGateway, times(0)).findById(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se o novo status_id for zero ou negativo")
    void shouldThrowExceptionIfStatusIdIsZeroOrNegative() {
        PathOrderDTO dtoZero = new PathOrderDTO(validOrderId, 0);
        PathOrderDTO dtoNegative = new PathOrderDTO(validOrderId, -1);

        assertThrows(IllegalArgumentException.class, () -> pathOrderUseCase.run(dtoZero));

        assertThrows(IllegalArgumentException.class, () -> pathOrderUseCase.run(dtoNegative));

        verify(orderGateway, times(0)).findById(any());
    }
}