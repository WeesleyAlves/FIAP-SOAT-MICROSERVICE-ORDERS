package br.com.fiap.techchallenge.orders.unit.core.entities;

import br.com.fiap.techchallenge.orders.core.entities.OrderNumberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderNumberEntityTest {

    private Long validId;
    private Integer validCurrentValue;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validCurrentValue = 100;
    }

    // -------------------------------------------------------------------------
    // Testes de Construtor
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes do Construtor")
    class ConstructorTests {

        @Test
        @DisplayName("Deve construir a entidade com sucesso quando os parâmetros são válidos")
        void shouldConstructSuccessfullyWhenParametersAreValid() {
            OrderNumberEntity entity = new OrderNumberEntity(validId, validCurrentValue);
            assertNotNull(entity);
            assertEquals(validId, entity.getId());
            assertEquals(validCurrentValue, entity.getCurrentValue());
        }

        @Test
        @DisplayName("Deve lançar exceção se o ID for nulo")
        void shouldThrowExceptionIfIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new OrderNumberEntity(null, validCurrentValue),
                    "O ID de pedido não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o valor atual for nulo")
        void shouldThrowExceptionIfCurrentValueIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new OrderNumberEntity(validId, null),
                    "O valor atual do número de pedido não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o valor atual for negativo")
        void shouldThrowExceptionIfCurrentValueIsNegative() {
            assertThrows(IllegalArgumentException.class, () -> new OrderNumberEntity(validId, -1),
                    "O valor atual do número de pedido não pode ser negativo.");
        }
    }

    // -------------------------------------------------------------------------
    // Testes de Setters
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes de Setters")
    class SetterTests {
        private OrderNumberEntity entity;

        @BeforeEach
        void setup() {
            entity = new OrderNumberEntity(validId, validCurrentValue);
        }

        @Test
        @DisplayName("Deve configurar o ID com sucesso")
        void shouldSetIdSuccessfully() {
            Long newId = 2L;
            entity.setId(newId);
            assertEquals(newId, entity.getId());
        }

        @Test
        @DisplayName("Deve lançar exceção se tentar configurar o ID como nulo")
        void shouldThrowExceptionIfSettingIdToNull() {
            assertThrows(IllegalArgumentException.class, () -> entity.setId(null),
                    "O ID de pedido não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve configurar o valor atual com sucesso")
        void shouldSetCurrentValueSuccessfully() {
            Integer newValue = 101;
            entity.setCurrentValue(newValue);
            assertEquals(newValue, entity.getCurrentValue());
        }

        @Test
        @DisplayName("Deve lançar exceção se tentar configurar o valor atual como nulo")
        void shouldThrowExceptionIfSettingCurrentValueToNull() {
            assertThrows(IllegalArgumentException.class, () -> entity.setCurrentValue(null),
                    "O valor atual do número de pedido não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se tentar configurar o valor atual como negativo")
        void shouldThrowExceptionIfSettingCurrentValueToNegative() {
            assertThrows(IllegalArgumentException.class, () -> entity.setCurrentValue(-5),
                    "O valor atual do número de pedido não pode ser negativo.");
        }
    }
}