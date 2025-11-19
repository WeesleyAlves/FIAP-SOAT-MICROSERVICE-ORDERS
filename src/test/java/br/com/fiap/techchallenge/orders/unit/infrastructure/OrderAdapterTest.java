package br.com.fiap.techchallenge.orders.unit.infrastructure;

import br.com.fiap.techchallenge.orders.api.exceptions.OrderNotFoundException;
import br.com.fiap.techchallenge.orders.api.exceptions.OrderNumberSequenceNotInitializedException;
import br.com.fiap.techchallenge.orders.application.dtos.out.CompleteOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.CreateOrderDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderNumberDTO;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderProductItemOutDTO;
import br.com.fiap.techchallenge.orders.infrastructure.adapters.OrderAdapter;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderEntityJPA;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderNumberEntityJPA;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderProductIdJPA;
import br.com.fiap.techchallenge.orders.infrastructure.entities.OrderProductsEntityJPA;
import br.com.fiap.techchallenge.orders.infrastructure.repositories.OrderNumberSequenceRepository;
import br.com.fiap.techchallenge.orders.infrastructure.repositories.OrdersRepository;
import br.com.fiap.techchallenge.orders.utils.constants.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.techchallenge.orders.utils.constants.OrderConstants.ORDER_SEQUENCE_NOT_INITIALIZED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para OrderAdapter")
class OrderAdapterTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderNumberSequenceRepository sequenceRepository;

    @InjectMocks
    private OrderAdapter orderAdapter;

    private UUID validOrderId;
    private OrderEntityJPA mockOrderJPA;
    private OrderNumberEntityJPA mockOrderNumberJPA;

    @BeforeEach
    void setUp() {
        validOrderId = UUID.randomUUID();

        // Dados de Produto Mock
        OrderProductIdJPA productPk = new OrderProductIdJPA(validOrderId, UUID.randomUUID());
        OrderProductsEntityJPA productJPA = new OrderProductsEntityJPA(
                productPk, "Hamburguer", 2, BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00)
        );
        List<OrderProductsEntityJPA> mockProductsJPA = List.of(productJPA);

        // Dados de Pedido Mock
        mockOrderJPA = new OrderEntityJPA();
        mockOrderJPA.setId(validOrderId);
        mockOrderJPA.setOrderNumber(100);
        mockOrderJPA.setStatus(OrderStatus.PENDING);
        mockOrderJPA.setOriginalPrice(BigDecimal.valueOf(20.00));
        mockOrderJPA.setCreatedAt(LocalDateTime.now());
        mockOrderJPA.setProducts(mockProductsJPA);
        productJPA.setOrder(mockOrderJPA);

        // Dados de OrderNumber Mock
        mockOrderNumberJPA = new OrderNumberEntityJPA();
        mockOrderNumberJPA.setId(1L);
        mockOrderNumberJPA.setCurrentValue(50);
    }

    // -------------------------------------------------------------------------
    // Testes de findOrderByID
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve encontrar pedido por ID e mapear para CompleteOrderDTO")
    void shouldFindOrderByIdAndMapToDTO() {
        when(ordersRepository.findById(validOrderId)).thenReturn(Optional.of(mockOrderJPA));

        CompleteOrderDTO result = orderAdapter.findOrderByID(validOrderId);

        assertNotNull(result);
        assertEquals(validOrderId, result.id());
        assertEquals("Recebido", result.status()); // Verifica o mapeamento do ENUM
        assertFalse(result.products().isEmpty());
        assertEquals(1, result.products().orElseThrow().size());
        verify(ordersRepository, times(1)).findById(validOrderId);
    }

    @Test
    @DisplayName("Deve lançar OrderNotFoundException se findById falhar")
    void shouldThrowOrderNotFoundExceptionWhenOrderIsNotFound() {
        when(ordersRepository.findById(validOrderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderAdapter.findOrderByID(validOrderId));
    }

    // -------------------------------------------------------------------------
    // Testes de findOrderByStatusIn
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve encontrar pedidos por lista de status e mapear para lista de DTOs")
    void shouldFindOrdersByStatusInAndMapToList() {
        List<OrderStatus> statuses = List.of(OrderStatus.PENDING);
        when(ordersRepository.findByStatusInWithProducts(statuses)).thenReturn(List.of(mockOrderJPA));

        List<CompleteOrderDTO> results = orderAdapter.findOrderByStatusIn(statuses);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(validOrderId, results.get(0).id());
        verify(ordersRepository, times(1)).findByStatusInWithProducts(statuses);
    }

    // -------------------------------------------------------------------------
    // Testes de findAllOrders
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve encontrar todos os pedidos e mapear para lista de DTOs")
    void shouldFindAllOrdersAndMapToList() {
        when(ordersRepository.findAll()).thenReturn(List.of(mockOrderJPA));

        List<CompleteOrderDTO> results = orderAdapter.findAllOrders();

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(ordersRepository, times(1)).findAll();
    }

    // -------------------------------------------------------------------------
    // Testes de saveOrder
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve salvar o pedido, mapeando CreateOrderDTO para JPA e de volta para CompleteOrderDTO")
    void shouldSaveOrderAndMapCorrectly() {
        // ARRANGE
        UUID customerId = UUID.randomUUID();
        OrderProductItemOutDTO productDTO = new OrderProductItemOutDTO(
                UUID.randomUUID(), "Item", BigDecimal.TEN, 1, BigDecimal.TEN
        );

        CreateOrderDTO inputDto = new CreateOrderDTO(
                customerId, "Observação", 101, BigDecimal.TEN, "Recebido", List.of(productDTO)
        );

        when(ordersRepository.save(any(OrderEntityJPA.class))).thenReturn(mockOrderJPA);

        ArgumentCaptor<OrderEntityJPA> orderCaptor = ArgumentCaptor.forClass(OrderEntityJPA.class);

        CompleteOrderDTO result = orderAdapter.saveOrder(inputDto);

        verify(ordersRepository, times(1)).save(orderCaptor.capture());

        OrderEntityJPA savedJPA = orderCaptor.getValue();
        assertEquals(OrderStatus.PENDING, savedJPA.getStatus()); // Verifica a conversão de String para ENUM
        assertEquals(1, savedJPA.getProducts().size());

        assertNotNull(result);
        assertEquals(mockOrderJPA.getId(), result.id());
    }

    // -------------------------------------------------------------------------
    // Testes de updateOrder
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve atualizar o status do pedido e salvar")
    void shouldUpdateOrderStatusAndSave() {
        CompleteOrderDTO updateDto = new CompleteOrderDTO(
                validOrderId, 100, OrderStatus.IN_PREPARATION.getDescription(), null, null, null, null, null, null, null, null
        );

        when(ordersRepository.findById(validOrderId)).thenReturn(Optional.of(mockOrderJPA));
        when(ordersRepository.save(any(OrderEntityJPA.class))).thenReturn(mockOrderJPA);

        ArgumentCaptor<OrderEntityJPA> orderCaptor = ArgumentCaptor.forClass(OrderEntityJPA.class);

        orderAdapter.updateOrder(updateDto);

        verify(ordersRepository, times(1)).findById(validOrderId);
        verify(ordersRepository, times(1)).save(orderCaptor.capture());

        OrderEntityJPA updatedJPA = orderCaptor.getValue();
        assertEquals(OrderStatus.IN_PREPARATION, updatedJPA.getStatus());
    }

    @Test
    @DisplayName("Deve lançar OrderNotFoundException ao tentar atualizar pedido inexistente")
    void shouldThrowOrderNotFoundExceptionOnUpdate() {
        CompleteOrderDTO updateDto = new CompleteOrderDTO(
                validOrderId, 100, OrderStatus.IN_PREPARATION.getDescription(), null, null, null, null, null, null, null, null
        );
        when(ordersRepository.findById(validOrderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderAdapter.updateOrder(updateDto));
    }

    // -------------------------------------------------------------------------
    // Testes de OrderNumber (Sequência)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve salvar a sequência de número de pedido")
    void shouldSaveOrderNumberSequence() {
        OrderNumberDTO inputDto = new OrderNumberDTO(1L, 51);
        when(sequenceRepository.save(any(OrderNumberEntityJPA.class))).thenReturn(mockOrderNumberJPA);

        OrderNumberDTO result = orderAdapter.saveOrderNumber(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(50, result.currentValue());
        verify(sequenceRepository, times(1)).save(any(OrderNumberEntityJPA.class));
    }

    @Test
    @DisplayName("Deve encontrar o último número de pedido")
    void shouldFindTopOrderNumber() {
        when(sequenceRepository.findTopByOrderByIdAsc()).thenReturn(Optional.of(mockOrderNumberJPA));

        OrderNumberDTO result = orderAdapter.findTopOrderNumber();

        assertNotNull(result);
        assertEquals(50, result.currentValue());
        verify(sequenceRepository, times(1)).findTopByOrderByIdAsc();
    }

    @Test
    @DisplayName("Deve lançar OrderNumberSequenceNotInitializedException se a sequência não existir")
    void shouldThrowExceptionIfSequenceNotInitialized() {
        when(sequenceRepository.findTopByOrderByIdAsc()).thenReturn(Optional.empty());

        OrderNumberSequenceNotInitializedException exception = assertThrows(OrderNumberSequenceNotInitializedException.class,
                () -> orderAdapter.findTopOrderNumber()
        );
        assertEquals(ORDER_SEQUENCE_NOT_INITIALIZED, exception.getMessage());
    }

    // -------------------------------------------------------------------------
    // Teste de @PostConstruct (initSequence)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Deve inicializar a sequência se a contagem for zero")
    void shouldInitializeSequenceIfCountIsZero() {
        when(sequenceRepository.count()).thenReturn(0L);

        orderAdapter.initSequence();

        verify(sequenceRepository, times(1)).count();
        verify(sequenceRepository, times(1)).save(any(OrderNumberEntityJPA.class));
    }

    @Test
    @DisplayName("Não deve inicializar a sequência se a contagem for maior que zero")
    void shouldNotInitializeSequenceIfCountIsGreaterThanZero() {
        when(sequenceRepository.count()).thenReturn(1L);

        orderAdapter.initSequence();

        verify(sequenceRepository, times(1)).count();
        verify(sequenceRepository, times(0)).save(any(OrderNumberEntityJPA.class));
    }
}