package br.com.fiap.techchallenge.orders.unit.core.entities;

import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.entities.OrderPaymentEntity;
import br.com.fiap.techchallenge.orders.core.entities.OrderProductItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompleteOrderEntityTest {

    private UUID validId;
    private Integer validOrderNumber;
    private String validStatus;
    private BigDecimal validPrice;
    private LocalDateTime validDate;
    private UUID validCustomerId;
    private List<OrderProductItemEntity> validProducts;

    @BeforeEach
    void setUp() {
        validId = UUID.randomUUID();
        validOrderNumber = 10;
        validStatus = "RECEBIDO";
        validPrice = BigDecimal.valueOf(100.00);
        validDate = LocalDateTime.now();
        validCustomerId = UUID.randomUUID();
        validProducts = List.of(
                new OrderProductItemEntity(
                        UUID.randomUUID(), "Item", BigDecimal.TEN
                )
        );
    }

    // -------------------------------------------------------------------------
    // Testes para o Construtor Completo (Para Entidades Persistidas)
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes do Construtor Completo")
    class FullConstructorTests {

        @Test
        @DisplayName("Deve construir a entidade com sucesso quando todos os parâmetros são válidos")
        void shouldConstructSuccessfullyWhenParametersAreValid() {
            CompleteOrderEntity entity = new CompleteOrderEntity(
                    validId, validOrderNumber, validStatus, validPrice, validDate, validDate
            );

            assertNotNull(entity);
            assertEquals(validId, entity.getId());
            assertEquals(validOrderNumber, entity.getOrderNumber());
            assertEquals(validStatus, entity.getStatus());
            assertEquals(validPrice, entity.getOriginalPrice());
            assertEquals(validDate, entity.getCreatedAt());
        }

        @Test
        @DisplayName("Deve lançar exceção se o ID for nulo")
        void shouldThrowExceptionIfIdIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    null, validOrderNumber, validStatus, validPrice, validDate, validDate
            ), "O ID do pedido não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o número do pedido for nulo")
        void shouldThrowExceptionIfOrderNumberIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, null, validStatus, validPrice, validDate, validDate
            ), "O número do pedido não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o número do pedido for zero ou negativo")
        void shouldThrowExceptionIfOrderNumberIsZeroOrNegative() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, 0, validStatus, validPrice, validDate, validDate
            ), "O número do pedido deve ser um valor positivo.");

            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, -1, validStatus, validPrice, validDate, validDate
            ), "O número do pedido deve ser um valor positivo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o status for nulo ou vazio")
        void shouldThrowExceptionIfStatusIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, validOrderNumber, null, validPrice, validDate, validDate
            ), "O status não pode ser nulo ou vazio.");

            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, validOrderNumber, " ", validPrice, validDate, validDate
            ), "O status não pode ser nulo ou vazio.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o preço original for nulo ou negativo")
        void shouldThrowExceptionIfOriginalPriceIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, validOrderNumber, validStatus, null, validDate, validDate
            ), "O preço original não pode ser nulo.");

            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, validOrderNumber, validStatus, BigDecimal.valueOf(-10.00), validDate, validDate
            ), "O preço original não pode ser negativo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se a data de criação for nula")
        void shouldThrowExceptionIfCreatedAtIsNull() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validId, validOrderNumber, validStatus, validPrice, null, validDate
            ), "A data de criação não pode ser nula.");
        }
    }

    // -------------------------------------------------------------------------
    // Testes para o Construtor Simplificado (Para Criação)
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes do Construtor Simplificado")
    class CreationConstructorTests {

        @Test
        @DisplayName("Deve construir a entidade com sucesso para criação")
        void shouldConstructSuccessfullyForCreation() {
            CompleteOrderEntity entity = new CompleteOrderEntity(
                    validOrderNumber, validStatus, validPrice, validCustomerId
            );

            assertNotNull(entity);
            assertEquals(validOrderNumber, entity.getOrderNumber());
            assertEquals(validStatus, entity.getStatus());
            assertEquals(validPrice, entity.getOriginalPrice());
            assertEquals(validCustomerId, entity.getCustomerId());

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
        }

        @Test
        @DisplayName("Deve lançar exceção se o número do pedido for inválido na criação")
        void shouldThrowExceptionIfOrderNumberIsInvalidOnCreation() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    0, validStatus, validPrice, validCustomerId
            ), "O número do pedido deve ser um valor positivo e não pode ser nulo.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o status for inválido na criação")
        void shouldThrowExceptionIfStatusIsInvalidOnCreation() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validOrderNumber, null, validPrice, validCustomerId
            ), "O status do pedido não pode ser nulo ou vazio.");
        }

        @Test
        @DisplayName("Deve lançar exceção se o preço for inválido na criação")
        void shouldThrowExceptionIfPriceIsInvalidOnCreation() {
            assertThrows(IllegalArgumentException.class, () -> new CompleteOrderEntity(
                    validOrderNumber, validStatus, BigDecimal.valueOf(-10), validCustomerId
            ), "O preço original não pode ser nulo ou negativo.");
        }
    }

    // -------------------------------------------------------------------------
    // Testes de Setters e Validações
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Testes de Setters")
    class SetterTests {
        private CompleteOrderEntity entity;

        @BeforeEach
        void setup() {
            entity = new CompleteOrderEntity(
                    validId, validOrderNumber, validStatus, validPrice, validDate, validDate
            );
        }

        @Test
        @DisplayName("Deve configurar o Customer ID com sucesso")
        void shouldSetCustomerIdSuccessfully() {
            UUID newCustomerId = UUID.randomUUID();
            entity.setCustomerId(newCustomerId);
            assertEquals(newCustomerId, entity.getCustomerId());
        }

        @Test
        @DisplayName("Deve configurar as Notes com sucesso")
        void shouldSetNotesSuccessfully() {
            String newNotes = "Sem cebola";
            entity.setNotes(newNotes);
            assertEquals(newNotes, entity.getNotes());
        }

        @Test
        @DisplayName("Deve configurar o Payment com sucesso")
        void shouldSetPaymentSuccessfully() {
            OrderPaymentEntity newPayment = new OrderPaymentEntity(UUID.randomUUID(), "QR");
            entity.setPayment(newPayment);
            assertEquals(newPayment, entity.getPayment());
        }

        @Test
        @DisplayName("Deve configurar Products com sucesso e validar seu conteúdo")
        void shouldSetProductsSuccessfully() {
            entity.setProducts(validProducts);
            assertEquals(validProducts, entity.getProducts());
            assertFalse(entity.getProducts().isEmpty());
        }

        @Test
        @DisplayName("Deve lançar exceção se Products for nula ou vazia")
        void shouldThrowExceptionIfProductsListIsInvalid() {
            assertThrows(IllegalArgumentException.class, () -> entity.setProducts(null),
                    "A lista de produtos não pode ser nula.");

            assertThrows(IllegalArgumentException.class, () -> entity.setProducts(Collections.emptyList()),
                    "A lista de produtos não pode estar vazia.");
        }

        @Test
        @DisplayName("Deve configurar Status com sucesso")
        void shouldSetStatusSuccessfully() {
            String newStatus = "Em Preparação";
            entity.setStatus(newStatus);
            assertEquals(newStatus, entity.getStatus());
        }

        @Test
        @DisplayName("Deve lançar exceção se Status for nulo ou vazio")
        void shouldThrowExceptionIfStatusIsInvalidInSetter() {
            assertThrows(IllegalArgumentException.class, () -> entity.setStatus(null),
                    "O status do pedido não pode ser nulo ou vazio.");

            assertThrows(IllegalArgumentException.class, () -> entity.setStatus(" "),
                    "O status do pedido não pode ser nulo ou vazio.");
        }
    }
}