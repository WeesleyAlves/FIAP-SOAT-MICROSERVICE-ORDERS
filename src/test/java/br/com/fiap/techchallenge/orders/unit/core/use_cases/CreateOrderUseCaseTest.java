package br.com.fiap.techchallenge.orders.unit.core.use_cases;

import br.com.fiap.techchallenge.orders.api.exceptions.ProductNotFoundException;
import br.com.fiap.techchallenge.orders.application.dtos.in.NewOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.in.OrderProductInDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderGateway;
import br.com.fiap.techchallenge.orders.application.gateways.ProductGateway;
import br.com.fiap.techchallenge.orders.core.entities.CompleteOrderEntity;
import br.com.fiap.techchallenge.orders.core.entities.OrderProductItemEntity;
import br.com.fiap.techchallenge.orders.core.use_cases.CreateOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para CreateOrderUseCase")
class CreateOrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private NewOrderDTO validNewOrderDTO;
    private OrderProductItemEntity mockProductEntity1;
    private OrderProductItemEntity mockProductEntity2;
    private final Integer validOrderNumber = 100;
    private final UUID customerId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        OrderProductInDTO productInDTO1 = new OrderProductInDTO(UUID.randomUUID(), 2);
        OrderProductInDTO productInDTO2 = new OrderProductInDTO(UUID.randomUUID(), 1);

        validNewOrderDTO = new NewOrderDTO(
                customerId,
                List.of(productInDTO1, productInDTO2),
                "Sem picles"
        );

        mockProductEntity1 = new OrderProductItemEntity(productInDTO1.id(), "Produto A", BigDecimal.valueOf(10.00));
        mockProductEntity2 = new OrderProductItemEntity(productInDTO2.id(), "Produto B", BigDecimal.valueOf(30.00));
    }

    @Test
    @DisplayName("Deve criar e salvar o pedido com sucesso e calcular o preço total corretamente")
    void shouldCreateAndSaveOrderSuccessfully() {

        BigDecimal expectedTotalPrice = BigDecimal.valueOf(50.00);

        when(productGateway.getAllByIds(anyList())).thenReturn(List.of(mockProductEntity1, mockProductEntity2));

        CompleteOrderEntity savedEntityMock = new CompleteOrderEntity(validOrderNumber, "AWAITING_PAYMENT", expectedTotalPrice, customerId);
        when(orderGateway.save(any(CompleteOrderEntity.class))).thenReturn(savedEntityMock);

        ArgumentCaptor<CompleteOrderEntity> orderCaptor = ArgumentCaptor.forClass(CompleteOrderEntity.class);

        CompleteOrderEntity result = createOrderUseCase.run(validNewOrderDTO, validOrderNumber);

        verify(productGateway, times(1)).getAllByIds(anyList());
        verify(orderGateway, times(1)).save(orderCaptor.capture());

        CompleteOrderEntity capturedOrder = orderCaptor.getValue();
        assertEquals(expectedTotalPrice, capturedOrder.getOriginalPrice());
        assertEquals("Sem picles", capturedOrder.getNotes());

        assertEquals(2, capturedOrder.getProducts().size());

        OrderProductItemEntity p1 = capturedOrder.getProducts().stream().filter(p -> p.getId().equals(mockProductEntity1.getId())).findFirst().orElseThrow();
        assertEquals(2, p1.getQuantity()); // Qtd 2 do DTO
        assertEquals(BigDecimal.valueOf(20.00), p1.getTotalValue()); // 2 * 10.00

        assertEquals(expectedTotalPrice, result.getOriginalPrice());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se a lista de produtos for vazia")
    void shouldThrowExceptionIfProductListIsEmpty() {
        NewOrderDTO emptyDto = new NewOrderDTO(customerId, Collections.emptyList(), null);

        assertThrows(IllegalArgumentException.class, () -> createOrderUseCase.run(emptyDto, validOrderNumber),
                "A lista de produtos não pode estar vazia.");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se a lista de produtos for nula")
    void shouldThrowExceptionIfProductListIsNull() {
        NewOrderDTO emptyDto = new NewOrderDTO(customerId, null, null);

        assertThrows(IllegalArgumentException.class, () -> createOrderUseCase.run(emptyDto, validOrderNumber),
                "A lista de produtos não pode estar vazia.");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se a quantidade de um produto for nula")
    void shouldThrowExceptionIfProductQuantityIsNull() {
        OrderProductInDTO productInDTO = new OrderProductInDTO(UUID.randomUUID(), null);
        NewOrderDTO dtoWithNullQty = new NewOrderDTO(customerId, List.of(productInDTO), null);

        when(productGateway.getAllByIds(anyList())).thenReturn(List.of(mockProductEntity1));

        assertThrows(ProductNotFoundException.class, () -> createOrderUseCase.run(dtoWithNullQty, validOrderNumber),
                "A quantidade de produtos não pode estar vazia.");
    }

    @Test
    @DisplayName("Deve lançar ProductNotFoundException se um produto do DTO não for encontrado pelo Gateway")
    void shouldThrowProductNotFoundExceptionWhenProductIsMissing() {
        // Simula que o Gateway retornou apenas o Produto A, mas o DTO pedia A e B.
        when(productGateway.getAllByIds(anyList())).thenReturn(List.of(mockProductEntity1));

        assertThrows(ProductNotFoundException.class, () -> createOrderUseCase.run(validNewOrderDTO, validOrderNumber),
                "Produto não encontrado para o ID:");

        verify(orderGateway, times(0)).save(any());
    }
}