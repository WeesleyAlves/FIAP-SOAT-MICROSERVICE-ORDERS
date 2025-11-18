package br.com.fiap.techchallenge.orders.unit.utils;

import br.com.fiap.techchallenge.orders.api.exceptions.OrderStatusNotFoundException;
import br.com.fiap.techchallenge.orders.utils.constants.OrderConstants;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    // -------------------------------------------------------------------------
    // Testes de Atributos (Getter)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve verificar os atributos de uma constante do ENUM")
    void shouldCheckEnumAttributes() {
        OrderStatus status = OrderStatus.PENDING;

        assertEquals(2, status.getId());
        assertEquals("Recebido", status.getDescription());
        assertEquals(3, status.getSortOrder());
    }

    // -------------------------------------------------------------------------
    // Testes para getById(int)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar o OrderStatus correto ao buscar por ID")
    void shouldReturnCorrectStatusWhenSearchingById() {
        OrderStatus status = OrderStatus.getById(4); // READY

        assertEquals(OrderStatus.READY, status);
        assertEquals("Pronto", status.getDescription());
    }

    @Test
    @DisplayName("Deve lançar OrderStatusNotFoundException ao buscar por ID inexistente")
    void shouldThrowExceptionWhenSearchingByNonExistentId() {
        int nonExistentId = 99;

        OrderStatusNotFoundException exception = assertThrows(OrderStatusNotFoundException.class, () ->
                OrderStatus.getById(nonExistentId)
        );

        String expectedMessage = String.format(OrderConstants.ORDER_STATUS_NOT_FOUND, nonExistentId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Testes para getByDescription(String)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve retornar o OrderStatus correto ao buscar por descrição")
    void shouldReturnCorrectStatusWhenSearchingByDescription() {
        OrderStatus status = OrderStatus.getByDescription("Em Preparação"); // IN_PREPARATION

        assertEquals(OrderStatus.IN_PREPARATION, status);
        assertEquals(3, status.getId());
    }

    @Test
    @DisplayName("Deve lançar OrderStatusNotFoundException ao buscar por descrição inexistente")
    void shouldThrowExceptionWhenSearchingByNonExistentDescription() {
        String nonExistentDescription = "Status Invalido";

        OrderStatusNotFoundException exception = assertThrows(OrderStatusNotFoundException.class, () ->
                OrderStatus.getByDescription(nonExistentDescription)
        );

        String expectedMessage = String.format(OrderConstants.ORDER_DESCRIPTION_NOT_FOUND, nonExistentDescription);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar OrderStatusNotFoundException ao buscar por descrição nula ou vazia")
    void shouldThrowExceptionWhenSearchingByNullOrEmptyDescription() {
        assertThrows(OrderStatusNotFoundException.class, () ->
                OrderStatus.getByDescription(null)
        );

        assertThrows(OrderStatusNotFoundException.class, () ->
                OrderStatus.getByDescription("")
        );
    }
}