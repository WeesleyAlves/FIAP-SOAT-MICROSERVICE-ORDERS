package br.com.fiap.techchallenge.orders.unit.utils;

import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.utils.comparators.OrderComparator;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderComparatorTest {

    private CompleteOrderEntity orderPendingOld;      // sortOrder 3, mais antigo
    private CompleteOrderEntity orderPendingNew;      // sortOrder 3, mais recente
    private CompleteOrderEntity orderReady;           // sortOrder 1 (Maior prioridade)
    private Comparator<CompleteOrderEntity> comparator;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        comparator = OrderComparator.run();

        // 1. Pronto (sortOrder=1) - MAIOR PRIORIDADE
        orderReady = new CompleteOrderEntity(
                UUID.randomUUID(), 4, OrderStatus.READY.getDescription(), BigDecimal.TEN, now.plusMinutes(20), now.plusMinutes(20)
        );

        // 2. Recebido (sortOrder=3) - MAIS ANTIGO
        orderPendingOld = new CompleteOrderEntity(
                UUID.randomUUID(), 2, OrderStatus.PENDING.getDescription(), BigDecimal.TEN, now.plusMinutes(5), now.plusMinutes(5)
        );

        // 3. Recebido (sortOrder=3) - MAIS RECENTE
        orderPendingNew = new CompleteOrderEntity(
                UUID.randomUUID(), 5, OrderStatus.PENDING.getDescription(), BigDecimal.TEN, now.plusMinutes(10), now.plusMinutes(10)
        );
    }

    // -------------------------------------------------------------------------
    // Testes de Lógica do Comparator (Prioridade por Sort Order)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar negativo se o sortOrder do order1 for menor (maior prioridade)")
    void shouldReturnNegativeIfOrder1HasHigherPriority() {
        // ARRANGE: orderReady (sortOrder=1) vs orderPendingOld (sortOrder=3)
        int result = comparator.compare(orderReady, orderPendingOld);

        // ASSERT: orderReady deve vir antes
        assertTrue(result < 0, "Pedidos com menor sortOrder (maior prioridade) devem vir antes.");
    }

    @Test
    @DisplayName("Deve retornar positivo se o sortOrder do order1 for maior (menor prioridade)")
    void shouldReturnPositiveIfOrder1HasLowerPriority() {
        // ARRANGE: orderPendingOld (sortOrder=3) vs orderReady (sortOrder=1)
        int result = comparator.compare(orderPendingOld, orderReady);

        // ASSERT: orderPendingOld deve vir depois
        assertTrue(result > 0, "Pedidos com maior sortOrder (menor prioridade) devem vir depois.");
    }

    // -------------------------------------------------------------------------
    // Testes de Lógica do Comparator (Desempate por Data)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve usar createdAt para desempate quando o status for o mesmo (order1 > order2)")
    void shouldUseCreatedAtForTieBreakerIfStatusIsSame_NewerVsOlder() {
        // ARRANGE: orderPendingNew (10:10) vs orderPendingOld (10:05). Ambos sortOrder=3.
        int result = comparator.compare(orderPendingNew, orderPendingOld);

        // ASSERT: orderPendingNew (mais recente) deve vir depois do orderPendingOld
        assertTrue(result > 0, "Pedido mais recente deve vir depois quando o status for igual.");
    }

    @Test
    @DisplayName("Deve usar createdAt para desempate quando o status for o mesmo (order2 < order1)")
    void shouldUseCreatedAtForTieBreakerIfStatusIsSame_OlderVsNewer() {
        // ARRANGE: orderPendingOld (10:05) vs orderPendingNew (10:10). Ambos sortOrder=3.
        int result = comparator.compare(orderPendingOld, orderPendingNew);

        // ASSERT: orderPendingOld (mais antigo) deve vir antes do orderPendingNew
        assertTrue(result < 0, "Pedido mais antigo deve vir antes quando o status for igual.");
    }

    @Test
    @DisplayName("Deve retornar zero se status e createdAt forem iguais")
    void shouldReturnZeroIfStatusAndCreatedAtAreSame() {
        CompleteOrderEntity orderCopy = new CompleteOrderEntity(
                orderPendingOld.getId(), orderPendingOld.getOrderNumber(), orderPendingOld.getStatus(), orderPendingOld.getOriginalPrice(), orderPendingOld.getCreatedAt(), orderPendingOld.getUpdatedAt()
        );

        int result = comparator.compare(orderPendingOld, orderCopy);

        assertEquals(0, result, "Comparação deve ser zero se status e data forem iguais.");
    }
}