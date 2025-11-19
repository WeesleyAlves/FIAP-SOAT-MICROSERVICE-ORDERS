package br.com.fiap.techchallenge.orders.unit.application.gateways;

import br.com.fiap.techchallenge.orders.api.exceptions.OrderNumberNotFound;
import br.com.fiap.techchallenge.orders.application.dtos.out.OrderNumberDTO;
import br.com.fiap.techchallenge.orders.application.gateways.OrderNumberGateway;
import br.com.fiap.techchallenge.orders.core.entities.OrderNumberEntity;
import br.com.fiap.techchallenge.orders.infrastructure.datasources.OrderDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para OrderNumberGateway")
class OrderNumberGatewayTest {

    @Mock
    private OrderDatasource orderDatasource;

    @InjectMocks
    private OrderNumberGateway orderNumberGateway;

    private OrderNumberEntity mockEntity;
    private OrderNumberDTO mockDto;

    @BeforeEach
    void setUp() {
        mockEntity = new OrderNumberEntity(1L, 1);
        mockDto = new OrderNumberDTO(1L, 1);
    }

    @Test
    @DisplayName("Deve salvar uma entidade e retornar a entidade convertida corretamente")
    void shouldSaveEntityAndReturnConvertedEntity() {

        when(orderDatasource.saveOrderNumber(any(OrderNumberDTO.class))).thenReturn(mockDto);


        OrderNumberEntity result = orderNumberGateway.save(mockEntity);


        verify(orderDatasource, times(1)).saveOrderNumber(any(OrderNumberDTO.class));

        assertEquals(mockEntity.getId(), result.getId(), "O ID deve ser o mesmo");
        assertEquals(mockEntity.getCurrentValue(), result.getCurrentValue(), "O valor deve ser o mesmo");
    }


    @Test
    @DisplayName("Deve buscar e retornar a entidade OrderNumber quando encontrada")
    void shouldFindAndReturnOrderNumberEntity() {
        when(orderDatasource.findTopOrderNumber()).thenReturn(mockDto);

        OrderNumberEntity result = orderNumberGateway.findTopByOrderByIdAsc();

        verify(orderDatasource, times(1)).findTopOrderNumber();

        assertEquals(mockEntity.getId(), result.getId(), "O ID da entidade deve ser o do DTO");
        assertEquals(mockEntity.getCurrentValue(), result.getCurrentValue(), "O valor deve ser o do DTO");
    }

    @Test
    @DisplayName("Deve lançar OrderNumberNotFound se o Datasource retornar nulo")
    void shouldThrowExceptionWhenOrderNumberNotFound() {
        when(orderDatasource.findTopOrderNumber()).thenReturn(null);

        assertThrows(OrderNumberNotFound.class, () -> {
            orderNumberGateway.findTopByOrderByIdAsc();
        }, "Deve lançar OrderNumberNotFound quando o resultado for nulo");

        verify(orderDatasource, times(1)).findTopOrderNumber();
    }
}