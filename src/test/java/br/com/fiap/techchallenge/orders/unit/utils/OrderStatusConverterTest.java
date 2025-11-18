package br.com.fiap.techchallenge.orders.unit.utils;

import br.com.fiap.techchallenge.orders.api.exceptions.OrderStatusNotFoundException;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import br.com.fiap.techchallenge.orders.utils.converters.OrderStatusConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusConverterTest {

    private OrderStatusConverter converter;

    @BeforeEach
    void setUp() {
        converter = new OrderStatusConverter();
    }

    // -------------------------------------------------------------------------
    // Testes para convertToDatabaseColumn (Entidade -> Banco)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve converter um OrderStatus para o seu ID inteiro")
    void shouldConvertOrderStatusToId() {
        OrderStatus status = OrderStatus.PENDING;

        Integer dbValue = converter.convertToDatabaseColumn(status);

        assertEquals(2, dbValue);
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter um OrderStatus nulo")
    void shouldReturnNullWhenConvertingNullStatus() {
        Integer dbValue = converter.convertToDatabaseColumn(null);

        assertNull(dbValue);
    }

    // -------------------------------------------------------------------------
    // Testes para convertToEntityAttribute (Banco -> Entidade)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve converter o ID inteiro para o OrderStatus correto")
    void shouldConvertIdToOrderStatus() {
        Integer statusId = 3;

        OrderStatus status = converter.convertToEntityAttribute(statusId);

        assertEquals(OrderStatus.IN_PREPARATION, status);
        assertEquals("Em Preparação", status.getDescription());
    }

    @Test
    @DisplayName("Deve retornar nulo ao converter um ID nulo")
    void shouldReturnNullWhenConvertingNullId() {
        OrderStatus status = converter.convertToEntityAttribute(null);

        assertNull(status);
    }

    @Test
    @DisplayName("Deve lançar exceção se o ID for inválido (e OrderStatus.getById lançar)")
    void shouldThrowExceptionIfIdIsInvalid() {
        Integer invalidId = 999;

        assertThrows(OrderStatusNotFoundException.class, () ->
                converter.convertToEntityAttribute(invalidId)
        );
    }
}