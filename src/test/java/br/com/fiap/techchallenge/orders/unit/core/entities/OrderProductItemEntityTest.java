package br.com.fiap.techchallenge.orders.unit.core.entities;

import br.com.fiap.techchallenge.orders.core.entities.OrderProductItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderProductItemEntityTest {

    private UUID validId;
    private String validName;
    private BigDecimal validPrice;
    private int validQuantity;
    private BigDecimal validTotalValue;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        validName = "Hamburguer";
        validPrice = BigDecimal.valueOf(25.00);
        validQuantity = 2;
        validTotalValue = BigDecimal.valueOf(50.00);
    }

    // -------------------------------------------------------------------------
    // Testes de Construtor Completo
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes do Construtor Completo (ID, Name, Price)")
    class FullConstructorTests {

        @Test
        @DisplayName("Deve construir a entidade com sucesso quando os parâmetros são válidos")
        void shouldConstructSuccessfullyWhenParametersAreValid() {
            OrderProductItemEntity entity = new OrderProductItemEntity(validId, validName, validPrice);

            assertNotNull(entity);
            assertEquals(validId, entity.getId());
            assertEquals(validName, entity.getName());
            assertEquals(validPrice, entity.getPrice());
        }

        @Test
        @DisplayName("Deve lançar exceção se o ID for nulo")
        void shouldThrowExceptionIfIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new OrderProductItemEntity(null, validName, validPrice),
                    "O ID do produto não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o nome for nulo ou vazio")
        void shouldThrowExceptionIfNameIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> new OrderProductItemEntity(validId, null, validPrice),
                    "O nome do produto não pode ser nulo ou vazio.");

            assertThrows(IllegalArgumentException.class, () -> new OrderProductItemEntity(validId, " ", validPrice),
                    "O nome do produto não pode ser nulo ou vazio.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o preço for nulo ou menor/igual a zero")
        void shouldThrowExceptionIfPriceIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> new OrderProductItemEntity(validId, validName, null),
                    "O preço unitário do produto não pode ser nulo.");

            assertThrows(IllegalArgumentException.class, () -> new OrderProductItemEntity(validId, validName, BigDecimal.ZERO),
                    "O preço unitário do produto deve ser maior que zero.");

            assertThrows(IllegalArgumentException.class, () -> new OrderProductItemEntity(validId, validName, BigDecimal.valueOf(-1.00)),
                    "O preço unitário do produto deve ser maior que zero.");
        }
    }

    // -------------------------------------------------------------------------
    // Testes de Construtor Simplificado
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes do Construtor Simplificado (Apenas ID)")
    class SimpleConstructorTests {
        @Test
        @DisplayName("Deve construir a entidade com sucesso com apenas o ID")
        void shouldConstructSuccessfullyWithOnlyId() {
            OrderProductItemEntity entity = new OrderProductItemEntity(validId);
            assertNotNull(entity);
            assertEquals(validId, entity.getId());
        }

        @Test
        @DisplayName("Deve lançar exceção se o ID for nulo")
        void shouldThrowExceptionIfIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new OrderProductItemEntity(null),
                    "O ID do produto não pode ser nulo.");
        }
    }

    // -------------------------------------------------------------------------
    // Testes de Setters
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes de Setters")
    class SetterTests {
        private OrderProductItemEntity entity;

        @BeforeEach
        void setup() {
            entity = new OrderProductItemEntity(validId, validName, validPrice);
        }

        @Test
        @DisplayName("Deve configurar a quantidade com sucesso")
        void shouldSetQuantitySuccessfully() {
            entity.setQuantity(validQuantity);
            assertEquals(validQuantity, entity.getQuantity());
        }

        @Test
        @DisplayName("Deve lançar exceção se a quantidade for zero ou negativa")
        void shouldThrowExceptionIfQuantityIsZeroOrNegative() {
            assertThrows(IllegalArgumentException.class, () -> entity.setQuantity(0),
                    "A quantidade do produto deve ser maior que zero.");

            assertThrows(IllegalArgumentException.class, () -> entity.setQuantity(-5),
                    "A quantidade do produto deve ser maior que zero.");
        }

        @Test
        @DisplayName("Deve configurar o valor total com sucesso")
        void shouldSetTotalValueSuccessfully() {
            entity.setTotalValue(validTotalValue);
            assertEquals(validTotalValue, entity.getTotalValue());
        }

        @Test
        @DisplayName("Deve lançar exceção se o valor total for nulo, zero ou negativo")
        void shouldThrowExceptionIfTotalValueIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> entity.setTotalValue(null),
                    "O valor total do produto não pode ser nulo.");

            assertThrows(IllegalArgumentException.class, () -> entity.setTotalValue(BigDecimal.ZERO),
                    "O valor total do produto não pode ser zero.");

            assertThrows(IllegalArgumentException.class, () -> entity.setTotalValue(BigDecimal.valueOf(-10.00)),
                    "O valor total do produto não pode ser zero.");
        }
    }
}