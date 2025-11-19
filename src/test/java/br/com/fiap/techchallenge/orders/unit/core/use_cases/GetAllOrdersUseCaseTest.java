package br.com.fiap.techchallenge.orders.unit.core.use_cases;

import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.use_cases.GetAllOrdersUseCase;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para GetAllOrdersUseCase")
class GetAllOrdersUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private GetAllOrdersUseCase getAllOrdersUseCase;

    private CompleteOrderEntity orderReady;
    private CompleteOrderEntity orderPendingOld;
    private CompleteOrderEntity orderPendingNew;
    private CompleteOrderEntity orderAwaitingPayment;
    private List<CompleteOrderEntity> unsortedList;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Status e sortOrder: READY(1), PENDING(3), AWAITING_PAYMENT(4)

        // 1. Pronto (sortOrder=1) - MAIOR PRIORIDADE
        orderReady = new CompleteOrderEntity(
                UUID.randomUUID(), 4, OrderStatus.READY.getDescription(), BigDecimal.TEN, now.plusMinutes(20), now.plusMinutes(20)
        );

        // 2. Recebido (sortOrder=3) - Mais Antigo
        orderPendingOld = new CompleteOrderEntity(
                UUID.randomUUID(), 2, OrderStatus.PENDING.getDescription(), BigDecimal.TEN, now.plusMinutes(5), now.plusMinutes(5)
        );

        // 3. Recebido (sortOrder=3) - Mais Recente
        orderPendingNew = new CompleteOrderEntity(
                UUID.randomUUID(), 5, OrderStatus.PENDING.getDescription(), BigDecimal.TEN, now.plusMinutes(10), now.plusMinutes(10)
        );

        // 4. Aguardando Pagamento (sortOrder=4) - MENOR PRIORIDADE
        orderAwaitingPayment = new CompleteOrderEntity(
                UUID.randomUUID(), 1, OrderStatus.AWAITING_PAYMENT.getDescription(), BigDecimal.TEN, now.plusMinutes(1), now.plusMinutes(1)
        );

        // Lista Desordenada (Simulando a ordem que viria do DB)
        unsortedList = List.of(orderPendingNew, orderAwaitingPayment, orderReady, orderPendingOld);
    }

    @Test
    @DisplayName("Deve buscar todos os pedidos e ordená-los corretamente por Status e Data")
    void shouldFetchAndSortAllOrdersCorrectly() {
        // ARRANGE
        // Simula que o Gateway retorna a lista em ordem aleatória/DB
        when(orderGateway.getAll()).thenReturn(unsortedList);

        // ACT
        List<CompleteOrderEntity> sortedList = getAllOrdersUseCase.run();

        // ASSERT
        verify(orderGateway, times(1)).getAll();
        assertFalse(sortedList.isEmpty());
        assertEquals(4, sortedList.size());

        /*
         * Ordem Esperada (Baseado no sortOrder: 1, 3(old), 3(new), 4):
         * 1. orderReady (SortOrder 1)
         * 2. orderPendingOld (SortOrder 3, mais antigo)
         * 3. orderPendingNew (SortOrder 3, mais recente)
         * 4. orderAwaitingPayment (SortOrder 4)
         */

        // 1. Status Ready (SortOrder 1)
        assertEquals(orderReady.getId(), sortedList.get(0).getId());

        // 2. Status Pending (SortOrder 3) - Mais antigo
        assertEquals(orderPendingOld.getId(), sortedList.get(1).getId());

        // 3. Status Pending (SortOrder 3) - Mais recente (desempate por data)
        assertEquals(orderPendingNew.getId(), sortedList.get(2).getId());

        // 4. Status Awaiting Payment (SortOrder 4)
        assertEquals(orderAwaitingPayment.getId(), sortedList.get(3).getId());
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se o Gateway não retornar pedidos")
    void shouldReturnEmptyListIfGatewayReturnsEmpty() {
        // ARRANGE
        when(orderGateway.getAll()).thenReturn(List.of());

        // ACT
        List<CompleteOrderEntity> result = getAllOrdersUseCase.run();

        // ASSERT
        verify(orderGateway, times(1)).getAll();
        assertEquals(0, result.size());
    }
}